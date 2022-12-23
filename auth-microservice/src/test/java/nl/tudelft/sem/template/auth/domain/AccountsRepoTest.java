package nl.tudelft.sem.template.auth.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the account repository.
 */
@DataJpaTest
public class AccountsRepoTest {

    @Autowired
    private AccountsRepo accountsRepo;

    @Test
    void saveAccountTest() {
        AccountCredentials account = new AccountCredentials("Foo", "Bar");
        accountsRepo.save(account);
        assertThat(accountsRepo.count()).isEqualTo(1);
    }

    @Test
    void saveMultipleTest() {
        AccountCredentials account = new AccountCredentials("Foo", "Bar");
        AccountCredentials account2 = new AccountCredentials("Hello", "World");
        accountsRepo.save(account);
        accountsRepo.save(account2);
        assertThat(accountsRepo.count()).isEqualTo(2);
    }

    @Test
    void noDuplicatesTest() {
        AccountCredentials account = new AccountCredentials("Foo", "Bar");
        AccountCredentials account2 = new AccountCredentials("Foo", "Baz");
        accountsRepo.save(account);
        accountsRepo.save(account2);
        assertThat(accountsRepo.count()).isEqualTo(1);
    }

    @Test
    void retrieveTest() {
        AccountCredentials account = new AccountCredentials("Foo", "Bar");
        accountsRepo.save(account);
        Optional<AccountCredentials> retrieved  = accountsRepo.findById("Foo");
        assertThat(retrieved.get()).isEqualTo(account);
    }
}
