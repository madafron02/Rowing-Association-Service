package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;

import java.time.LocalDateTime;

public class TimeConstraintHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        switch (matchFilter.getActivityApp().getType()) {
            case TRAINING: {
                if (LocalDateTime.now().plusMinutes(30)
                        .isBefore(matchFilter.getActivityApp().getTimeslot().getStartTime())) {
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
                if (LocalDateTime.now().plusDays(1)
                        .isBefore(matchFilter.getActivityApp().getTimeslot().getStartTime())) {
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
