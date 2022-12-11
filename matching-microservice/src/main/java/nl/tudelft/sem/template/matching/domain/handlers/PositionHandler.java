package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.ActivityApp;
import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.UserApp;

public class PositionHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        if (matchFilter.getActivityApp().getPositions().containsKey(matchFilter.getPosition())) {
            if (next != null) {
                return next.handle(matchFilter);
            } else {
                return true;
            }
        }
        return false;
    }
}
