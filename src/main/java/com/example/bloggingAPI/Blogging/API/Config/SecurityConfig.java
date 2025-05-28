package com.example.bloggingAPI.Blogging.API.Config;

//import com.example.bloggingAPI.Blogging.API.Service.UserDetailServiceImpl;
import com.example.bloggingAPI.Blogging.API.Service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(customizer -> customizer.disable())

                .authorizeHttpRequests(requests -> requests

                        // User registration endpoint is public
                        .requestMatchers("/user/auth/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
                        .requestMatchers("/auth/api/**").permitAll() //for login and logout

                        .requestMatchers("/admin/auth/**").hasRole("ADMIN")
                        .requestMatchers("/admin/api/**").hasRole("ADMIN") //to fetch all the users.

                        .requestMatchers("/post/name/**").hasAnyRole("ADMIN", "USER")
                        .requestMatchers("/post/**").permitAll()
                        .requestMatchers("/public/all-post/**").permitAll()
                        .anyRequest().authenticated())


                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                
                .build();
    }



    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setPasswordEncoder(NoOpPasswordEncoder.getInstance());
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(customService);
        return provider;
    }
    //and this bean which is AuthenticationProvider will communicate with the database
    //to fetch the data.

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    //this bean will talk to authentication provider.

}

//provider.setPasswordEncoder(new BCryptPasswordEncoder(12)) - in this way we can still put our password after
//converted into hash value.