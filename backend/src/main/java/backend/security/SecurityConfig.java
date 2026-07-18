package backend.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password-ka user-ka wuxuu u beddelayaa BCrypt hash.
     *
     * Bean-kan ayaa xallinaya error-ka:
     * No qualifying bean of type PasswordEncoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Security rules-ka application-ka.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                /*
                 * JWT system-ku session ma isticmaalo,
                 * sidaas darteed CSRF waa la daminayaa.
                 */
                .csrf(AbstractHttpConfigurer::disable)

                /*
                 * Waxay u diyaarinaysaa backend-ka
                 * requests-ka frontend-ka React.
                 */
                .cors(Customizer.withDefaults())

                /*
                 * Server-ku user session ma kaydinayo.
                 * Request kasta JWT token ayuu wataa.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /*
                 * Register iyo login waa public.
                 * Endpoint-yada kale JWT ayay u baahan yihiin.
                 */
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(
                                        "/api/auth/register",
                                        "/api/auth/login"
                                )
                                .permitAll()

                                /*
                                 * Spring Boot error endpoint.
                                 */
                                .requestMatchers("/error")
                                .permitAll()

                                /*
                                 * Endpoint kasta oo kale
                                 * user login sameeyay ayuu rabaa.
                                 */
                                .anyRequest()
                                .authenticated()
                )

                /*
                 * JWT filter-ka waxaa la ordiyaa ka hor
                 * username/password filter-ka Spring Security.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}