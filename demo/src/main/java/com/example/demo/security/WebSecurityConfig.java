package com.example.demo.security;

import com.example.demo.service.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthEntryPointJwt unauthorizedHandler;


    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests().antMatchers("/api/auth/**",
                        "/api/test/all","/api/category/list",
                        "/api/order/add",
                        "/api/category/details/**",
                        "/api/order/confirmOrder",
                         "/api/display/**",
                         "/api/User/checkUsername/**",
                         "/api/urlProduct/list/**",
                         "/api/order/list",
                        "/api/auth/signup",
                        "/api/auth/signin",
                        "/api/order/getCharts",
                        "/api/order/getCharts/**",
                        "/api/order/YearOrder/**",
                        "/api/order/MonthOrder/**",
                        "/api/reviews/showAll",
                        "/api/reviews/add",
                        "/api/forbiddenword/getAll",
                        "/api/forbiddenword/add",
                        "/api/reviews/showAll/**",
                        "/api/order/details/**",
                        "/api/reviews/details/**").permitAll()
                .antMatchers("/api/forbiddenword/add","/api/forbiddenword/delete","/api/forbiddenword/getAll").hasAnyAuthority("ROLE_MODERATOR","ROLE_ADMIN")
                .antMatchers("/api/test/admin","/api/User/findUserBand","/api/User/delete/**","/api/category/delete/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/api/test/user").hasAnyAuthority("ROLE_USER")
                .antMatchers("/api/User/findAllUserAdmin/**").hasAnyAuthority("ROLE_ADMIN","ROLE_MODERATOR")
                .antMatchers("/api/User/delete/**").hasAnyAuthority("ROLE_ADMIN")
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
