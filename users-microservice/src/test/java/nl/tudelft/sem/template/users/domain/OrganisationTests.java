package nl.tudelft.sem.template.users.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OrganisationTests {

    private static Organisation organisation;

    @BeforeEach
    void setUp() {
        organisation = new Organisation("Proteus");
    }

    @Test
    void testConstructor() {
        assertThat(organisation).isNotNull();
    }

    @Test
    void testNoArgsConstructor() {
        assertThat(new Organisation()).isNotNull();
    }

    @Test
    void testGetName() {
        assertThat(organisation.getName()).isEqualTo("Proteus");
    }
}
