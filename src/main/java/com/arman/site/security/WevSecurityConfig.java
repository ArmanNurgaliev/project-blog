package com.arman.site.security;

import com.arman.site.security.oauth.CustomOAuth2UserService;
import com.arman.site.security.oauth.OAuth2LoginSuccessHandler;
import com.arman.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WevSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private DataSource dataSource;
    private CustomOAuth2UserService customOAuth2UserService;
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;

    @Autowired
    public WevSecurityConfig(UserService userService, PasswordEncoder passwordEncoder, DataSource dataSource, CustomOAuth2UserService customOAuth2UserService, OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                    .authorizeRequests()
                    .antMatchers("/", "/registration", "/static/**", "/blog", "/blog/*", "/about", "/oauth2/**").permitAll()
                   // .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .defaultSuccessUrl("/", true)
                    .permitAll()
               .and()
                    .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint().userService(customOAuth2UserService)
                    .and()
                    .successHandler(oAuth2LoginSuccessHandler)
                .and()
                    .logout()
                .and()
                    .rememberMe()
                    .tokenRepository(persistentTokenRepository());
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
        db.setDataSource(dataSource);
        return db;
    }
}
