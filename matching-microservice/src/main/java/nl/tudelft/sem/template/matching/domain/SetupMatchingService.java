package nl.tudelft.sem.template.matching.domain;

import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.domain.handlers.CertificateHandler;
import nl.tudelft.sem.template.matching.domain.handlers.CompetitivenessHandler;
import nl.tudelft.sem.template.matching.domain.handlers.FilteringHandler;
import nl.tudelft.sem.template.matching.domain.handlers.GenderHandler;
import nl.tudelft.sem.template.matching.domain.handlers.OrganisationHandler;
import nl.tudelft.sem.template.matching.domain.handlers.PositionHandler;
import nl.tudelft.sem.template.matching.domain.handlers.TimeConstraintHandler;
import nl.tudelft.sem.template.matching.domain.handlers.TypeOfActivityHandler;

public class SetupMatchingService {


    /**
     * Function for setting up the Chain of Responsibility pattern implemented for filtering.
     */
    public static FilteringHandler filteringHandlerSetUp(CertificateRepo certificateRepo) {
        FilteringHandler filteringHandler = new PositionHandler();
        FilteringHandler certificateHandler = new CertificateHandler(certificateRepo);
        filteringHandler.setNext(certificateHandler);
        FilteringHandler timeConstraintHandler = new TimeConstraintHandler();
        certificateHandler.setNext(timeConstraintHandler);
        FilteringHandler typeOfActivityHandler = new TypeOfActivityHandler();
        timeConstraintHandler.setNext(typeOfActivityHandler);
        FilteringHandler organisationHandler = new OrganisationHandler();
        typeOfActivityHandler.setNext(organisationHandler);
        FilteringHandler genderHandler = new GenderHandler();
        organisationHandler.setNext(genderHandler);
        FilteringHandler competitivenessHandler = new CompetitivenessHandler();
        genderHandler.setNext(competitivenessHandler);

        return filteringHandler;
    }
}
