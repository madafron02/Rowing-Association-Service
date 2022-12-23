package nl.tudelft.sem.template.auth.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the AccountCredentials entity.
 */
public class AccountCredentialsTest {

    private AccountCredentials accountCredentials;

    @BeforeEach
    void setUp() {
        accountCredentials = new AccountCredentials("Name", "Password");
    }

    @Test
    void testConstructor() {
        assertThat(accountCredentials).isNotNull();
    }

    @Test
    void testEmptyConstructor() {
        assertThat(new AccountCredentials()).isNotNull();
    }

    @Test
    void getUserIdTest() {
        assertThat(accountCredentials.getUserId()).isNotNull();
        assertThat(accountCredentials.getUserId()).isEqualTo("Name");
    }

    @Test
    void getPasswordTest() {
        assertThat(accountCredentials.getPassword()).isNotNull();
        assertThat(accountCredentials.getPassword()).isEqualTo("Password");
    }

    @Test
    void testEqualsTrue() {
        AccountCredentials other = new AccountCredentials("Name", "Password");
        assertThat(accountCredentials.equals(other)).isTrue();
    }

    @Test
    void testEqualsSame() {
        assertThat(accountCredentials.equals(accountCredentials)).isTrue();
    }

    @Test
    void testEqualsOtherInstance() {
        String other = "string";
        assertThat(accountCredentials.equals(other)).isFalse();
    }

    @Test
    void testEqualsFalseUserId() {
        AccountCredentials other = new AccountCredentials("Foo", "Password");
        assertThat(accountCredentials.equals(other)).isFalse();
    }

    @Test
    void testEqualsFalsePassword() {
        AccountCredentials other = new AccountCredentials("Name", "Bar");
        assertThat(accountCredentials.equals(other)).isFalse();
    }

    @Test
    void testEqualsFalse() {
        AccountCredentials other = new AccountCredentials("Foo", "Bar");
        assertThat(accountCredentials.equals(other)).isFalse();
    }

    @Test
    void testHashCode() {
        assertThat(accountCredentials.hashCode()).isInstanceOf(Integer.class);
    }

    @Test
    void testToString() {
        assertThat(accountCredentials.toString()).isEqualTo("AccountCredentials{userId='Name',"
                + " password='Password'}");
    }
}
