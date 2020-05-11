package io.flaterlab.testf.spring;

import io.flaterlab.testf.security.jwt.JwtSecurityConfigurer;
import io.flaterlab.testf.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/v1/auth/signin").permitAll()
            .antMatchers(HttpMethod.POST, "/v1/auth/signup").permitAll()
            .antMatchers(HttpMethod.GET, "/tests/**").permitAll()
            .antMatchers(HttpMethod.DELETE, "/tests/**").hasRole("ADMIN")
            .antMatchers(HttpMethod.GET, "/v1/tests/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .apply(securityConfigurer());
    }

    @Bean
    public JwtSecurityConfigurer securityConfigurer() {
        return new JwtSecurityConfigurer(jwtTokenProvider);
    }
}
