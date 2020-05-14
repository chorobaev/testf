package io.flaterlab.testf.spring;

import io.flaterlab.testf.security.jwt.JwtTokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.GenericFilterBean;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationEntryPoint authenticationEntryPoint;
    private GenericFilterBean authenticationFilter;

    public SecurityConfig(
        AuthenticationEntryPoint authenticationEntryPoint,
        JwtTokenAuthenticationFilter authenticationFilter
    ) {
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
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

    @SuppressWarnings("ELValidationInJSP")
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //noinspection SpringElInspection
        http
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .httpBasic().disable()
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1/auth/signin").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/auth/signup").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/tests/{testId}/**")
                    .access("hasRole('CREATE_PRIVILEGE') and " +
                        "@userSecurity.hasAccessToEditTest(authentication, #testId)")
                .antMatchers(HttpMethod.GET, "/v1/tests").permitAll()
                .antMatchers(HttpMethod.PUT, "/v1/tests/**").hasAuthority("CREATE_PRIVILEGE")
                .anyRequest().authenticated();
    }
}
