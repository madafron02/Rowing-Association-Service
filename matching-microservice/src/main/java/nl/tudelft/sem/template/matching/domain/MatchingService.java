package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.application.ActivityCommunication;
import nl.tudelft.sem.template.matching.application.NotificationCommunication;
import nl.tudelft.sem.template.matching.application.UsersCommunication;
import nl.tudelft.sem.template.matching.authentication.AuthManager;
import nl.tudelft.sem.template.matching.models.ActivityReponse;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelOwner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A DDD service for matching a user.
 */
@Service
public class MatchingService {

    private final transient AuthManager auth;
    private final transient MatchingRepo matchingRepo;
    private final transient UsersCommunication usersCommunication;
    private final transient NotificationCommunication notificationCommunication;
    private final transient ActivityCommunication activityCommunication;


    /**
     * Constructor of the matching service.
     *
     * @param auth                      the authentication manager
     * @param repo                      the repository of matches
     * @param usersCommunication        communication to the user service
     * @param notificationCommunication communication to the notification service
     * @param activityCommunication     communication to the activity service
     */
    public MatchingService(AuthManager auth,
                           MatchingRepo repo,
                           UsersCommunication usersCommunication,
                           NotificationCommunication notificationCommunication,
                           ActivityCommunication activityCommunication) {
        this.auth = auth;
        this.matchingRepo = repo;
        this.usersCommunication = usersCommunication;
        this.notificationCommunication = notificationCommunication;
        this.activityCommunication = activityCommunication;
    }

    /**
     * Facade for gathering user details, activities, filtering,
     * and returning a DTO containing the list of matched activities.
     *
     * @param timeslot submitted by the user
     * @param position submitted by the user
     * @return DTO containing the list of matched activities
     */
    public MatchingResponseModel submitAvailability(TimeslotApp timeslot, String position) {
        UserApp user = usersCommunication.getUserDetails(auth.getUserId());
        List<ActivityApp> activities = activityCommunication.getActivitiesByAvailability(timeslot).getAvailableActivities();
        return new MatchingResponseModel(filterActivities(activities, user, position));
    }

    /**
     * Method for filtering the activities based on different constraints
     * in order to match a user.
     *
     * @param activities the activities given by the Activity microservice
     * @param user       the user requesting activities
     * @param position   teh position they can fill in
     * @return the positions the user is matched with
     */
    public List<ActivityReponse> filterActivities(List<ActivityApp> activities, UserApp user, String position) {
        return activities.stream().filter(a -> a.getPositions().containsKey(position))
                .filter(a -> {
                    switch (a.getType()) {
                        case COMPETITION:
                            return a.getGender().equals(user.getGender())
                                    && a.getOrganisation().equals(user.getOrganisation())
                                    && (!a.isCompetitiveness() || user.isCompetitiveness());
                        default:
                            return true;

                    }
                }).map(a -> matchUserToActivity(user, position, a))
                .collect(Collectors.toList());
    }

    /**
     * Method for matching a user to a given activity and add it to the repository.
     *
     * @param user     the user matched
     * @param position the position requested
     * @param activity the activity they've been matched with
     * @return the ActivityResponse entity to be sent to the client
     */
    private ActivityReponse matchUserToActivity(UserApp user, String position, ActivityApp activity) {
        Match matchMade = new Match(user.getId(), activity.getActivityId(), activity.getPublisherId(), position);
        matchingRepo.save(matchMade);
        return new ActivityReponse(matchMade.getMatchId(), activity.getType(), activity.getTimeslot());
    }

    /**
     * Method for verifying the identity of the user making a request by using the SecurityContext
     * in the auth manager.
     *
     * @param matchId the id of the match
     * @return true if the user making the request has the same userIs as the userId of the match given
     *         false otherwise
     */
    private boolean verifyUser(long matchId) {
        return auth.getUserId().equals(matchingRepo.getMatchByMatchId(matchId).get().getParticipantId());
    }

    /**
     * Method for verifying the existence of the match.
     *
     * @param matchId the id of the match
     * @return true if it exists in the database a match with the id given
     *         false otherwise
     */
    public boolean verifyMatch(long matchId) {
        return matchingRepo.getMatchByMatchId(matchId).isPresent() && verifyUser(matchId);
    }

    /**
     * Method called by the api request for picking an activity which retrieves the match by the matchId
     * sets the status as Pending, saves the Match and then notifies the owner of a new match being made.
     *
     * @param matchId the id of a match
     */
    public void pickActivity(long matchId) {
        Match match = matchingRepo.getMatchByMatchId(matchId).get();
        match.setStatus(Status.PENDING);
        matchingRepo.save(match);
        notifyOwner(match);
    }

    /**
     * Method for notifying an owner of the match made by calling the method in the NotificationCommunication.
     *
     * @param match the Match entity made
     */
    private void notifyOwner(Match match) {
        notificationCommunication
                .sendReminderToOwner(new NotificationRequestModelOwner(match.getOwnerId(),
                        match.getParticipantId(),
                        match.getActivityId(),
                        activityCommunication.getActivityTimeslotById(match.getActivityId())));
    }

    /**
     * Method for getting the pending requests by the current userId acting as owner of activities.
     *
     * @return the List of matches being in pending for the owner (client making a request)
     */
    public List<Match> getPendingRequests() {
        return matchingRepo.getMatches(auth.getUserId());
    }
}
