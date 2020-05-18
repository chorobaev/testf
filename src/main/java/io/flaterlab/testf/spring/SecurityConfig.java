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
                // Authentication
                .antMatchers(HttpMethod.POST, "/v1/auth/signin").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/auth/signup").permitAll()

                // Getting tests
                .antMatchers(HttpMethod.GET, "/v1/tests").hasAuthority("READ_PRIVILEGE")
                .antMatchers(HttpMethod.GET, "/v1/tests/{testId}/**")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToTest(authentication, #testId)")

                // Creating tests
                .antMatchers(HttpMethod.PUT, "/v1/tests").hasAuthority("CREATE_PRIVILEGE")
                .antMatchers(HttpMethod.PUT, "/v1/tests/{testId}/questions")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToTest(authentication, #testId)")
                .antMatchers(HttpMethod.PUT, "/v1/tests/questions/{questionId}/answers")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToQuestion(authentication, #questionId)")

                // Updating tests
                .antMatchers(HttpMethod.PUT, "/v1/tests/{testId}")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToTest(authentication, #testId)")
                .antMatchers(HttpMethod.PUT, "/v1/tests/questions/{questionId}")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToQuestion(authentication, #questionId)")
                .antMatchers(HttpMethod.PUT, "/v1/tests/questions/answers/{answerId}")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToAnswer(authentication, #answerId)")

                // Test attempting
                .antMatchers(HttpMethod.GET, "/v1/attempt/{testId}").hasAuthority("ATTEMPT_PRIVILEGE")
                .antMatchers(HttpMethod.POST, "/v1/attempt/{attemptId}")
                    .access("hasAuthority('ATTEMPT_PRIVILEGE') and @userSecurity.hasAccessToAttempt(authentication, #attemptId)")

                // Test deleting
                .antMatchers(HttpMethod.DELETE, "/v1/tests/{testId}")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToTest(authentication, #testId)")
                .antMatchers(HttpMethod.DELETE, "/v1/tests/questions/{questionId}")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToQuestion(authentication, #questionId)")
                .antMatchers(HttpMethod.DELETE, "/v1/tests/questions/answers/{answerId}")
                    .access("hasAuthority('CREATE_PRIVILEGE') and @userSecurity.hasAccessToAnswer(authentication, #answerId)")

                // All others
                .anyRequest().authenticated();
    }
}
