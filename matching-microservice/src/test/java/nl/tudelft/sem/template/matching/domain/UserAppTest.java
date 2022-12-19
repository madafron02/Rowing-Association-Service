package nl.tudelft.sem.template.matching.domain;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserAppTest {
    UserApp user;

    @BeforeEach
    void setup() {
        user = new UserApp("test", "C4+", "Male", "SEM", true);

    }
    @Test
    void getId() {
        assertThat(user.getId()).isEqualTo("test");
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
        assertThat(user.isCompetitiveness()).isTrue();
    }

    @Test
    void setId() {
        user.setId("test2");
        assertThat(user.getId()).isEqualTo("test2");
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
        user.setCompetitiveness(false);
        assertThat(user.isCompetitiveness()).isFalse();
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
        assertThat(user.toString()).isEqualTo("UserApp(id=test, certificate=C4+, gender=Male, organisation=SEM, competitiveness=true)");
    }
}