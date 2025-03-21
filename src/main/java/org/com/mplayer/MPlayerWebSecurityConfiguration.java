package org.com.mplayer;

import jakarta.servlet.DispatcherType;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class MPlayerWebSecurityConfiguration {
    private final MPlayerWebSecurityRequestFilterConfiguration MPlayerWebSecurityRequestFilterConfiguration;

    @Bean
    public SecurityFilterChain configureUserSecurityConfig(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(req ->
                req.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                    .requestMatchers(RoutesConfig.Public.REGISTER).permitAll()
                    .requestMatchers(RoutesConfig.Public.FORGOT_PASSWORD).permitAll()
                    .requestMatchers(RoutesConfig.Public.LOGIN).permitAll()
                    .anyRequest().authenticated()
            );

        http.addFilterBefore(MPlayerWebSecurityRequestFilterConfiguration, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
