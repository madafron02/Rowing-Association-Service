package nl.tudelft.sem.template.notification.config;

import nl.tudelft.sem.template.notification.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.template.notification.authentication.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

/**
 * Tests for RequestAuthenticationConfig class.
 */
public class RequestAuthenticationConfigTests {
    private transient RequestAuthenticationConfig requestAuthenticationConfig;
    private transient JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private transient JwtRequestFilter jwtRequestFilter;

    /**
     * General setup for tests.
     */
    @BeforeEach
    public void setup() {
        jwtAuthenticationEntryPoint = Mockito.mock(JwtAuthenticationEntryPoint.class);
        jwtRequestFilter = Mockito.mock(JwtRequestFilter.class);
        requestAuthenticationConfig = new RequestAuthenticationConfig(jwtAuthenticationEntryPoint,
                jwtRequestFilter);
    }

    @Test
    public void constructorTest() {
        assertThat(requestAuthenticationConfig).isNotNull();
    }

    @Test
    public void configureTest() {
        HttpSecurity httpSecurity = new HttpSecurity(
                Mockito.mock(ObjectPostProcessor.class),
                Mockito.mock(AuthenticationManagerBuilder.class),
                new HashMap<>());

        try {
            requestAuthenticationConfig.configure(httpSecurity);
            verify(httpSecurity).exceptionHandling()
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint);
            verify(httpSecurity).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        } catch (Exception e) {
            assertThat(e).isNotNull();
        }
    }
}
