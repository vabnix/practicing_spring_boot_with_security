package com.vaibhav.security.config;

import com.vaibhav.security.jwt.AuthEntryPointJwt;
import com.vaibhav.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MySecurityConfiguration {

    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt authEntryPointJwt;

    @Bean
    public AuthTokenFilter authJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                (request)
                        ->request
                        .requestMatchers("/auth/signin").permitAll()
                        .anyRequest().authenticated());
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.exceptionHandling(exception->exception.authenticationEntryPoint(authEntryPointJwt));
        //httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.headers(headers->headers.frameOptions(frameOptions->frameOptions.sameOrigin()));

        httpSecurity.csrf(csrf->csrf.disable());

        httpSecurity.addFilterBefore(authJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        if(!jdbcUserDetailsManager.userExists("admin")) {
            UserDetails admin = User.withUsername("admin")
                    .password(passwordEncoder().encode("admin"))
                    .roles("ADMIN")
                    .build();

            jdbcUserDetailsManager.createUser(admin);
        }

        if(!jdbcUserDetailsManager.userExists("readonly")) {
            UserDetails readonly = User.withUsername("readonly")
                    .password(passwordEncoder().encode("readonly"))
                    .roles("READONLY")
                    .build();

            jdbcUserDetailsManager.createUser(readonly);
        }

        return jdbcUserDetailsManager;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
