package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;

import java.time.LocalDateTime;

public class TimeConstraintHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        if (matchFilter.getActivityApp().getType() == TypeOfActivity.TRAINING) {
            return handleTraining(matchFilter);
        } else {
            return handleCompetition(matchFilter);
        }
    }

    /**
     * Method for handling the training.
     *
     * @param matchFilter the MatchFilter entity containing info to be filtered on constraints
     * @return TRUE if match filter complies with the constraints, FALSE otherwise
     */
    public boolean handleTraining(MatchFilter matchFilter) {
        if (!LocalDateTime.now().plusMinutes(30)
                .isBefore(matchFilter.getActivityApp().getTimeslot().getStartTime())) {
            return false;
        }

        if (next != null) {
            return next.handle(matchFilter);
        }

        return true;
    }

    /**
     * Method for handling the competition.
     *
     * @param matchFilter the MatchFilter entity containing info to be filtered on constraints
     * @return TRUE if match filter complies with the constraints, FALSE otherwise
     */
    public boolean handleCompetition(MatchFilter matchFilter) {
        if (!LocalDateTime.now().plusDays(1)
                .isBefore(matchFilter.getActivityApp().getTimeslot().getStartTime())) {
            return false;
        }
        if (next != null) {
            return next.handle(matchFilter);
        }

        return true;
    }
}
