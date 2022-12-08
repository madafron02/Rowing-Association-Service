package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.models.ActivityReponse;
import org.h2.engine.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A DDD service for matching a user.
 */
@Service
public class MatchingService {

    private final transient MatchingRepo matchingRepo;

    /**
     * Constructor of the matching service.
     *
     * @param repo the repository of matches
     */
    public MatchingService(MatchingRepo repo) {
        this.matchingRepo = repo;
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
                                    && a.isCompetitiveness() == user.isCompetitiveness();
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

}
