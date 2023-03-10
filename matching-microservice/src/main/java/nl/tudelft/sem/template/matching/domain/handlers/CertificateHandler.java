package nl.tudelft.sem.template.matching.domain.handlers;

import nl.tudelft.sem.template.matching.domain.MatchFilter;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;


public class CertificateHandler implements FilteringHandler {

    private transient FilteringHandler next;
    private transient CertificateRepo certificateRepo;

    public CertificateHandler(CertificateRepo certificateRepo) {
        this.certificateRepo = certificateRepo;
    }

    @Override
    public void setNext(FilteringHandler handler) {
        this.next = handler;
    }

    @Override
    public boolean handle(MatchFilter matchFilter) {
        if (!matchFilter.getUserPreferences().getPosition().equals("cox")) {
            if (next != null) {
                return next.handle(matchFilter);
            } else {
                return true;
            }
        }

        long certificateIdUser = certificateRepo.getCertificateByName(
                matchFilter.getUserPreferences().getUser().getCertificate()).get().getId();
        long certificateIdActivity = certificateRepo.getCertificateByName(
                matchFilter.getActivityApp().getProperties().getCertificate()).get().getId();


        if (certificateIdActivity <= certificateIdUser) {
            if (next != null) {
                return next.handle(matchFilter);
            } else {
                return true;
            }
        }
        return false;
    }
}
