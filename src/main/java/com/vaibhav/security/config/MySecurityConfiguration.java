package com.vaibhav.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class MySecurityConfiguration {

    @Autowired
    DataSource dataSource;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(
                (request)
                        ->request.anyRequest().authenticated());
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.NEVER));
        //httpSecurity.formLogin(Customizer.withDefaults());
        httpSecurity.httpBasic(Customizer.withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public UserDetailsService userDetailsService(){
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        if(!jdbcUserDetailsManager.userExists("admin")) {
            UserDetails admin = User.withUsername("admin")
                    .password("{noop}admin")
                    .roles("ADMIN")
                    .build();

            jdbcUserDetailsManager.createUser(admin);
        }

        if(!jdbcUserDetailsManager.userExists("readonly")) {
            UserDetails readonly = User.withUsername("readonly")
                    .password("{noop}readonly")
                    .roles("READONLY")
                    .build();

            jdbcUserDetailsManager.createUser(readonly);
        }

        return jdbcUserDetailsManager;
    }


}
