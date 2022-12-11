package nl.tudelft.sem.template.matching.database;

import nl.tudelft.sem.template.matching.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRepo extends JpaRepository<Certificate, Long> {
    public long getCertificateByName(String name);
}
