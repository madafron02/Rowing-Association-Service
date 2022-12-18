package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;

public class TimeConstraintHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = next;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        switch (matchFilter.getActivityApp().getType()) {
            case TRAINING: {
                if (matchFilter.getTimeslot().getStart().plusMinutes(30)
                        .isBefore(matchFilter.getActivityApp().getTimeslot().getStart())) {
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
                if (matchFilter.getTimeslot().getStart().plusDays(1)
                        .isBefore(matchFilter.getActivityApp().getTimeslot().getStart())) {
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
