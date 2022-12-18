package nl.tudelft.sem.template.notification.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Tests for H2Config class.
 */
public class H2ConfigTests {
    private transient H2Config h2Config;
    private transient Environment environment;

    /**
     * General setup for tests.
     */
    @BeforeEach
    public void setup() {
        environment = Mockito.mock(Environment.class);
        h2Config = new H2Config(environment);
    }

    @Test
    public void constructorTest() {
        assertThat(h2Config).isNotNull();
    }

    @Test
    public void getEnvironmentTest() {
        assertThat(h2Config.getEnvironment()).isEqualTo(environment);
    }

    @Test
    public void dataSourceTest() {
        when(environment.getProperty("jdbc.driverClassName")).thenReturn("org.h2.Driver");
        assertThat(h2Config.dataSource()).isInstanceOf(DriverManagerDataSource.class);
    }
}
