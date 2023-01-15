package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;

public class CompetitivenessHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        if (!matchFilter.getActivityApp().getProperties().isCompetition() || matchFilter.getUserPreferences().getUser().isCompetitive()) {
            if (next != null) {
                return next.handle(matchFilter);
            } else {
                return true;
            }

        }
        return false;
    }
}
