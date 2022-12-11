package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;

public interface FilteringHandler {

    public void setNext(FilteringHandler handler);

    public boolean handle(MatchFilter matchFilter);
}
