package nl.tudelft.sem.template.matching.domain.database;

import nl.tudelft.sem.template.matching.domain.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificateRepo extends JpaRepository<Certificate, Long> {
    public Optional<Long> getCertificateByName(String name);
}
