package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserAppTest {
    UserApp user;

    @BeforeEach
    void setup() {
        user = new UserApp("test", "C4+", "Male", "SEM", true);
    }

    @Test
    void getId() {
        assertThat(user.getEmail()).isEqualTo("test");
    }

    @Test
    void getCertificate() {
        assertThat(user.getCertificate()).isEqualTo("C4+");
    }

    @Test
    void getGender() {
        assertThat(user.getGender()).isEqualTo("Male");
    }

    @Test
    void getOrganisation() {
        assertThat(user.getOrganisation()).isEqualTo("SEM");
    }

    @Test
    void isCompetitiveness() {
        assertThat(user.isCompetitive()).isTrue();
    }

    @Test
    void setId() {
        user.setEmail("test2");
        assertThat(user.getEmail()).isEqualTo("test2");
    }

    @Test
    void setCertificate() {
        user.setCertificate("B4+");
        assertThat(user.getCertificate()).isEqualTo("B4+");
    }

    @Test
    void setGender() {
        user.setGender("Female");
        assertThat(user.getGender()).isEqualTo("Female");
    }

    @Test
    void setOrganisation() {
        user.setOrganisation("Org");
        assertThat(user.getOrganisation()).isEqualTo("Org");
    }

    @Test
    void setCompetitiveness() {
        user.setCompetitive(false);
        assertThat(user.isCompetitive()).isFalse();
    }

    @Test
    void testEquals() {
        UserApp user2 = new UserApp("test", "C4+", "Male", "SEM", true);
        assertThat(user).isEqualTo(user2);
        UserApp user3 = new UserApp("test2", "C4+", "Male", "SEM", true);
        assertThat(user).isNotEqualTo(user3);

    }

    @Test
    void testHashCode() {
        UserApp user2 = new UserApp("test", "C4+", "Male", "SEM", true);
        assertThat(user.hashCode()).isEqualTo(user2.hashCode());
        UserApp user3 = new UserApp("test2", "C4+", "Male", "SEM", true);
        assertThat(user.hashCode()).isNotEqualTo(user3.hashCode());
    }

    @Test
    void testToString() {
        assertThat(user.toString())
                .isEqualTo("UserApp(email=test, certificate=C4+,"
                        + " gender=Male, organisation=SEM, competitive=true)");
    }
}