package nl.tudelft.sem.template.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepo extends JpaRepository<Organisation, Long> {

    boolean existsOrganisationByName(String name);
}
