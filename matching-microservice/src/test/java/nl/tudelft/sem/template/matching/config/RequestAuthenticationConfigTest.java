package nl.tudelft.sem.template.matching.config;

import nl.tudelft.sem.template.matching.authentication.JwtAuthenticationEntryPoint;
import nl.tudelft.sem.template.matching.authentication.JwtRequestFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RequestAuthenticationConfigTest {

    private RequestAuthenticationConfig requestAuthenticationConfig;
    @Mock
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Mock
    private JwtRequestFilter jwtRequestFilter;
    @Mock
    private ObjectPostProcessor objectPostProcessor;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @BeforeEach
    void setUp() {
        requestAuthenticationConfig = new RequestAuthenticationConfig(jwtAuthenticationEntryPoint,
                jwtRequestFilter);
    }

    @Test
    void constructor() {
        assertThat(requestAuthenticationConfig).isNotNull();
    }

    @Test
    void configure() {
        HttpSecurity httpSecurity = new HttpSecurity(objectPostProcessor, authenticationManagerBuilder, new HashMap<>());

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