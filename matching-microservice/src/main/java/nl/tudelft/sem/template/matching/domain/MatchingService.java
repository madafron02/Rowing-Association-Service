package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.application.Communication;
import nl.tudelft.sem.template.matching.authentication.AuthManager;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.domain.database.MatchingRepo;
import nl.tudelft.sem.template.matching.domain.handlers.FilteringHandler;
import nl.tudelft.sem.template.matching.models.ActivityResponse;
import nl.tudelft.sem.template.matching.models.MatchingResponseModel;
import nl.tudelft.sem.template.matching.models.NotificationActivityModified;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelOwner;
import nl.tudelft.sem.template.matching.models.NotificationRequestModelParticipant;
import nl.tudelft.sem.template.matching.models.UserPreferences;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A DDD service for matching a user.
 */
@Service
public class MatchingService {

    private final transient AuthManager auth;
    private final transient MatchingRepo matchingRepo;
    private final transient Communication communication;
    public transient FilteringHandler filteringHandler;


    /**
     * Constructor of the matching service.
     *
     * @param auth          the authentication manager
     * @param repo          the repository of matches
     * @param communication communication with other microservices
     */
    public MatchingService(AuthManager auth,
                           MatchingRepo repo,
                           Communication communication,
                           CertificateRepo certificateRepo) {
        this.auth = auth;
        this.matchingRepo = repo;
        this.communication = communication;
        this.filteringHandler = SetupMatchingService.filteringHandlerSetUp(certificateRepo);
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
        UserApp user = communication.getUsersCommunication().getUserDetails(auth.getUserId());
        List<ActivityApp> activities = communication.getActivityCommunication()
                .getActivitiesByAvailability(timeslot).getActivities();
        return new MatchingResponseModel(filterActivities(activities, new UserPreferences(timeslot, user, position)));
    }

    /**
     * Method for filtering the activities based on different constraints
     * in order to match a user.
     *
     * @param activities      the activities given by the Activity microservice
     * @param userPreferences the preferences of the user
     * @return the positions the user is matched with
     */
    public List<ActivityResponse> filterActivities(List<ActivityApp> activities,
                                                   UserPreferences userPreferences) {
        List<ActivityResponse> responses = new ArrayList<>();

        for (ActivityApp activity : activities) {
            if (activity == null
                    || activity.setTypeOfActivity() == null
                    || !this.filteringHandler.handle(new MatchFilter(activity, userPreferences))
                    || !matchingRepo.getMatchesByActivityIdAndParticipantId(activity.getId(),
                    userPreferences.getUser().getEmail()).isEmpty()) {
                continue;
            }
            responses.add(matchUserToActivity(userPreferences.getUser(), userPreferences.getPosition(), activity));
        }

        return responses;
    }

    /**
     * Method for matching a user to a given activity and add it to the repository.
     *
     * @param user     the user matched
     * @param position the position requested
     * @param activity the activity they've been matched with
     * @return the ActivityResponse entity to be sent to the client
     */
    private ActivityResponse matchUserToActivity(UserApp user, String position, ActivityApp activity) {
        Match matchMade = new Match(user.getEmail(), activity.getId(), activity.getOwnerId(), position);
        matchingRepo.save(matchMade);
        return new ActivityResponse(matchMade.getMatchId(), activity.getType(), activity.getTimeslot());
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
        communication.getNotificationCommunication()
                .sendReminderToOwner(new NotificationRequestModelOwner(match.getOwnerId(),
                        match.getParticipantId(),
                        match.getActivityId(),
                        communication.getActivityCommunication().getActivityTimeslotById(match.getActivityId())));
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
        if (!verifySubmission(match)) {
            return false;
        }

        Match newMatch = matchingRepo.getMatchByMatchId(matchId).get();

        if (decision) {
            newMatch.setStatus(Status.ACCEPTED);
            communication.getActivityCommunication().updateActivity(newMatch.getActivityId(), newMatch.getPosition());
        } else {
            newMatch.setStatus(Status.DECLINED);
        }
        matchingRepo.save(newMatch);
        communication.getNotificationCommunication().sendNotificationToParticipant(
                new NotificationRequestModelParticipant(newMatch.getParticipantId(),
                        newMatch.getActivityId(),
                        communication.getActivityCommunication().getActivityTimeslotById(newMatch.getActivityId()),
                        decision));
        return true;
    }

    /**
     * Method for verifying the submission of an owner regarding the application of a user.
     *
     * @param match the match to be verified
     * @return TRUE if the match complies with the sanitisation constraints, FALSE otherwise
     */
    public boolean verifySubmission(Optional<Match> match) {
        return match.isPresent()
                && match.get().getOwnerId().equals(auth.getUserId())
                && match.get().getStatus().equals(Status.PENDING);
    }


    /**
     * Method for discarding the matches done with the activities having the specified activityId.
     *
     * @param activityId the id of the activity that was modified
     */
    public void discardMatchesByActivity(Long activityId) {
        List<Match> matchesModifiedByActivityChange = matchingRepo.getMatchesByActivityId(activityId);
        for (Match match : matchesModifiedByActivityChange) {
            if (match.getStatus() == Status.ACCEPTED) {
                communication.getNotificationCommunication()
                        .activityModifiedNotification(new NotificationActivityModified(match.getParticipantId(),
                                activityId, communication.getActivityCommunication().getActivityTimeslotById(activityId)));
            }
            matchingRepo.deleteById(match.getMatchId());
        }
    }
}
