package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.application.ActivityCommunication;
import nl.tudelft.sem.template.matching.application.NotificationCommunication;
import nl.tudelft.sem.template.matching.application.UsersCommunication;
import nl.tudelft.sem.template.matching.authentication.AuthManager;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.domain.database.MatchingRepo;
import nl.tudelft.sem.template.matching.domain.handlers.CertificateHandler;
import nl.tudelft.sem.template.matching.domain.handlers.CompetitivenessHandler;
import nl.tudelft.sem.template.matching.domain.handlers.FilteringHandler;
import nl.tudelft.sem.template.matching.domain.handlers.GenderHandler;
import nl.tudelft.sem.template.matching.domain.handlers.OrganisationHandler;
import nl.tudelft.sem.template.matching.domain.handlers.PositionHandler;
import nl.tudelft.sem.template.matching.domain.handlers.TimeConstraintHandler;
import nl.tudelft.sem.template.matching.domain.handlers.TypeOfActivityHandler;
import nl.tudelft.sem.template.matching.models.ActivityReponse;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import nl.tudelft.sem.template.matching.models.NotificationActivityModified;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelParticipant;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
    public transient FilteringHandler filteringHandler;
    private final transient CertificateRepo certificateRepo;


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
                           ActivityCommunication activityCommunication,
                           CertificateRepo certificateRepo) {
        this.auth = auth;
        this.matchingRepo = repo;
        this.usersCommunication = usersCommunication;
        this.notificationCommunication = notificationCommunication;
        this.activityCommunication = activityCommunication;
        this.certificateRepo = certificateRepo;
        filteringHandlerSetUp();
    }


    /**
     * Function for setting up the Chain of Responsibility pattern implemented for filtering.
     */
    public final void filteringHandlerSetUp() {
        this.filteringHandler = new PositionHandler();
        FilteringHandler certificateHandler = new CertificateHandler(certificateRepo);
        this.filteringHandler.setNext(certificateHandler);
        FilteringHandler timeConstraintHandler = new TimeConstraintHandler();
        certificateHandler.setNext(timeConstraintHandler);
        FilteringHandler typeOfActivityHandler = new TypeOfActivityHandler();
        timeConstraintHandler.setNext(typeOfActivityHandler);
        FilteringHandler organisationHandler = new OrganisationHandler();
        typeOfActivityHandler.setNext(organisationHandler);
        FilteringHandler genderHandler = new GenderHandler();
        organisationHandler.setNext(genderHandler);
        FilteringHandler competitivenessHandler = new CompetitivenessHandler();
        genderHandler.setNext(competitivenessHandler);

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
        return new MatchingResponseModel(filterActivities(activities, timeslot, user, position));
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
    public List<ActivityReponse> filterActivities(List<ActivityApp> activities,
                                                  TimeslotApp timeslot,
                                                  UserApp user,
                                                  String position) {
        return activities.stream().filter(a -> this.filteringHandler.handle(new MatchFilter(a, user, position, timeslot)))
                .map(a -> matchUserToActivity(user, position, a))
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
        return matchingRepo.getMatchesByOwnerIdAndStatus(auth.getUserId(), Status.PENDING);
    }

    /**
     * Method for accepting or denying a request.
     *
     * @param matchId  the id of the match to set a decision to
     * @param decision of the owner
     * @return true iff the request was valid and the notification was sent
     */
    public boolean acceptOrDenyRequest(long matchId, boolean decision) {
        Optional<Match> match = matchingRepo.getMatchByMatchId(matchId);
        if (match.isEmpty()) {
            return false;
        }
        if (!match.get().getOwnerId().equals(auth.getUserId())) {
            return false;
        }
        if (!match.get().getStatus().equals(Status.PENDING)) {
            return false;
        }
        Match newMatch = match.get();
        if (decision) {
            newMatch.setStatus(Status.ACCEPTED);
            activityCommunication.updateActivity(newMatch.getActivityId(), newMatch.getPosition());
        } else {
            newMatch.setStatus(Status.DECLINE);
        }
        matchingRepo.save(newMatch);
        notificationCommunication.sendNotificationToParticipant(
                new NotificationRequestModelParticipant(newMatch.getParticipantId(),
                        newMatch.getActivityId(),
                        activityCommunication.getActivityTimeslotById(newMatch.getActivityId()),
                        decision));
        return true;
    }

    /**
     * Method for retrieving all matches of the user with the specified status.
     *
     * @param status of the required activities
     * @return list of required matches
     */
    public List<Match> getMatches(Status status) {
        String userId = auth.getUserId();
        return matchingRepo.getMatchesByParticipantIdAndStatus(userId, status);
    }

    /**
     * Method for verifying whether a position is valid.
     *
     * @param position to check
     * @return true iff the position exists
     */
    public boolean verifyPosition(String position) {
        switch (position) {
            case "cox" : return true;
            case "starboard" : return true;
            case "coach" : return true;
            case "port" : return true;
            case "sculling" : return true;
            default : return false;
        }
    }

    /**
     * Method for discarding the matches done with the activities having the specified activityId.
     *
     * @param activityId the id of the activity that was modified
     */
    public void discardMatchesByActivity(Long activityId) {
        List<Match> matchesModifiedByActivityChange = matchingRepo.getMatchesByActivityId(activityId);
        matchesModifiedByActivityChange
                .stream()
                .filter(match -> match.getStatus() == Status.ACCEPTED)
                .forEach(match ->
                        notificationCommunication
                                .activityModifiedNotification(new NotificationActivityModified(match.getParticipantId(),
                                        activityId)));
        matchingRepo.deleteMatchesByActivityId(activityId);
    }
}
