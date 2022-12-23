package nl.tudelft.sem.template.users.domain.database;

import nl.tudelft.sem.template.users.domain.Organisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepo extends JpaRepository<Organisation, Long> {

    boolean existsOrganisationByName(String name);
}
