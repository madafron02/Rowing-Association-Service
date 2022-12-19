package nl.tudelft.sem.template.matching.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CertificateTest {

    private Certificate certificate;

    @BeforeEach
    void setUp() {
        certificate = new Certificate("C4");
    }

    @Test
    void testConstructor() {
        assertThat(certificate).isNotNull();
    }

    @Test
    void getId() {
        assertThat(certificate.getId()).isEqualTo(0L);
    }

    @Test
    void getName() {
        assertThat(certificate.getName()).isEqualTo("C4");
    }
}