package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.TypeOfActivity;

public class TypeOfActivityHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        if (matchFilter.getActivityApp().getType() == TypeOfActivity.TRAINING) {
            return true;
        }

        if (next != null) {
            return next.handle(matchFilter);
        }

        return false;
    }
}
