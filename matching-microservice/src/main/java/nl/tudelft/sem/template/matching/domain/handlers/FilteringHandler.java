package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.ActivityApp;
import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.UserApp;

public interface FilteringHandler {

    public void setNext(FilteringHandler handler);

    public boolean handle(MatchFilter matchFilter);
}
