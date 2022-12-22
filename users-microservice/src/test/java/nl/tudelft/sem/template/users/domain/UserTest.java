package nl.tudelft.sem.template.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {

    private static User user;

    @BeforeEach
    void setUp() {
        user = new User("ealstad@tudelft.nl", "male", true, "C4", "proteus");
    }

    @Test
    void testNoArgsConstructor() {
        assertThat(new User()).isNotNull();
    }

    @Test
    void testConstructor() {
        assertThat(user).isNotNull();
    }

    @Test
    void testGetNetId() {
        assertThat(user.getEmail()).isEqualTo("ealstad@tudelft.nl");
    }

    @Test
    void testGetGender() {
        assertThat(user.getGender()).isEqualTo("male");
    }

    @Test
    void testIsCompetitive() {
        assertThat(user.isCompetitive()).isTrue();
    }

    @Test
    void testGetCertificate() {
        assertThat(user.getCertificate()).isEqualTo("C4");
    }

    @Test
    void testGetOrganisation() {
        assertThat(user.getOrganisation()).isEqualTo("proteus");
    }

    @Test
    void testSetGender() {
        user.setGender("female");
        assertThat(user.getGender()).isEqualTo("female");
    }

    @Test
    void testSetCompetitiveness() {
        user.setCompetitiveness(false);
        assertThat(user.isCompetitive()).isFalse();
    }

    @Test
    void testSetCertificate() {
        user.setCertificate("4+");
        assertThat(user.getCertificate()).isEqualTo("4+");
    }

    @Test
    void testSetOrganisation() {
        user.setOrganisation("laga");
        assertThat(user.getOrganisation()).isEqualTo("laga");
    }

    @Test
    void testEqualsSelf() {
        assertThat(user.equals(user)).isTrue();
    }

    @Test
    void testEqualsOther() {
        User otherUser = new User("ealstad@tudelft.nl", "female", false, "8+", "rowpro");
        assertThat(user.equals(otherUser)).isTrue();
    }

    @Test
    void testNotEqual() {
        User otherUser = new User("hstyles@tudelft.nl", "female", false, "8+", "rowpro");
        assertThat(user.equals(otherUser)).isFalse();
    }

    @Test
    void testNotEqualNull() {
        assertThat(user.equals(null)).isFalse();
    }

    @Test
    void testNotEqualOtherClass() {
        assertThat(user.equals(new Organisation())).isFalse();
    }

    @Test
    void testHash() {
        User otherUser = new User("ealstad@tudelft.nl", "female", false, "8+", "rowpro");
        assertThat(user.hashCode()).isEqualTo(otherUser.hashCode());
    }
}
