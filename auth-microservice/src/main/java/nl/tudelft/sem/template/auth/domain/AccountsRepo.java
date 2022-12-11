package nl.tudelft.sem.template.auth.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepo extends CrudRepository<AccountCredentials, String> {
}
