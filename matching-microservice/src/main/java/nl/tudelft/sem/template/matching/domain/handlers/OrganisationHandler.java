package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;

public class OrganisationHandler implements FilteringHandler {

    private transient FilteringHandler next;

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        if (matchFilter.getActivityApp().getOrganisation().equals(matchFilter
                .getUserPreferences().getUser().getOrganisation())) {
            if (next != null) {
                return next.handle(matchFilter);
            } else {
                return true;
            }

        }
        return false;
    }
}
