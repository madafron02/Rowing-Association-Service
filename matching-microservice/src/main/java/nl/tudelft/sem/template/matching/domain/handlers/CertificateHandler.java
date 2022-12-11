package nl.tudelft.sem.template.matching.domain.handlers;

import lombok.AllArgsConstructor;
import nl.tudelft.sem.template.matching.domain.database.CertificateRepo;
import nl.tudelft.sem.template.matching.domain.MatchFilter;
import org.springframework.beans.factory.annotation.Autowired;


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
        if (!matchFilter.getPosition().equals("cox")) {
            if (next != null) {
                return next.handle(matchFilter);
            } else {
                return true;
            }
        }

        long certificateIdUser = certificateRepo.getCertificateByName(matchFilter.getUser().getCertificate());
        long certificateIdActivity = certificateRepo.getCertificateByName(matchFilter.getActivityApp().getCertificate());


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
