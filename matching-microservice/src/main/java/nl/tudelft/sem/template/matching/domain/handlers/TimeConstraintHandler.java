package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.TimeslotApp;

public class TimeConstraintHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = next;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        TimeslotApp activityTimeslot = matchFilter.getActivityApp().getTimeslot();
        TimeslotApp userTimeslot = matchFilter.getTimeslot();

        switch (matchFilter.getActivityApp().getType()) {
            case TRAINING: {
                if (userTimeslot.getStart().plusMinutes(30).isBefore(activityTimeslot.getStart())) {
                    if (next != null) {
                        return next.handle(matchFilter);
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            case COMPETITION: {
                if (userTimeslot.getStart().plusDays(1).isBefore(activityTimeslot.getStart())) {
                    if (next != null) {
                        return next.handle(matchFilter);
                    } else {
                        return true;
                    }
                } else {
                    return false;
                }
            }
            default:
                return false;
        }
    }
}
