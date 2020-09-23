package com.jm.springBootstrap.config;

import com.jm.springBootstrap.security.AuthProviderImpl;
import com.jm.springBootstrap.security.LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthProviderImpl authProvider;

    public WebSecurityConfig(AuthProviderImpl authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("us@us.ru").password("us").roles("USER")
                .and()
                .withUser("admin@admin.ru").password("admin").roles("ADMIN")
                .and()
                .withUser("sup@sup.ru").password("s").roles("ADMIN", "USER");

        auth.authenticationProvider(authProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/login").anonymous()

                .antMatchers("/").anonymous()

                .antMatchers("/admin/**", "/users/**", "/api/**").hasAnyRole("ADMIN")
//                .antMatchers("/admin/**", "/users/**", "/api/**").anonymous()

                .antMatchers("/user/**").hasAnyRole("USER")
                .antMatchers("/hello").access("hasAnyRole('ADMIN', 'USER')")
                .anyRequest().authenticated()
                .and()
                .formLogin() //.usernameParameter("email")
                .loginPage("/login")
                .successHandler(new LoginSuccessHandler())
                .and()
                .logout()
                .permitAll()
                .and().csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
