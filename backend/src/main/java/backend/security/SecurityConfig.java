package backend.security;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    /*
     * JWT authentication filter.
     */
    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    /*
     * Password-ka user-ka BCrypt hash
     * ayuu ka dhigayaa.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /*
     * CORS configuration.
     *
     * Waxay React frontend-ka localhost:5173
     * u oggolaanaysaa inuu backend-ka
     * localhost:8070 request u diro.
     */
    @Bean
    public CorsConfigurationSource
    corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        /*
         * Frontend origin-ka la oggol yahay.
         */
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173"
                )
        );

        /*
         * HTTP methods-ka la oggol yahay.
         */
        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        /*
         * Headers-ka React frontend-ku
         * backend-ka u diri karo.
         */
        configuration.setAllowedHeaders(
                List.of(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );

        /*
         * Browser-ka wuxuu akhrisan karaa
         * Authorization header haddii loo baahdo.
         */
        configuration.setExposedHeaders(
                List.of(
                        "Authorization"
                )
        );

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        /*
         * CORS rules-ka ku dabaq
         * dhammaan backend endpoints-ka.
         */
        source.registerCorsConfiguration(
                "/**",
                configuration
        );

        return source;
    }

    /*
     * Security rules-ka application-ka.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                /*
                 * JWT session ma isticmaalo.
                 */
                .csrf(
                        AbstractHttpConfigurer::disable
                )

                /*
                 * Isticmaal CORS configuration-ka sare.
                 */
                .cors(cors ->
                        cors.configurationSource(
                                corsConfigurationSource()
                        )
                )

                /*
                 * Server-ku session ma kaydinayo.
                 */
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                /*
                 * Public iyo protected endpoints.
                 */
                .authorizeHttpRequests(authorize ->
                        authorize

                                /*
                                 * Browser preflight request.
                                 */
                                .requestMatchers(
                                        HttpMethod.OPTIONS,
                                        "/**"
                                )
                                .permitAll()

                                /*
                                 * Register waa public.
                                 */
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/auth/register"
                                )
                                .permitAll()

                                /*
                                 * Login waa public.
                                 */
                                .requestMatchers(
                                        HttpMethod.POST,
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
                                 * JWT ayuu u baahan yahay.
                                 */
                                .anyRequest()
                                .authenticated()
                )

                /*
                 * JWT filter-ka ka hor mari
                 * Spring Security default filter.
                 */
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}