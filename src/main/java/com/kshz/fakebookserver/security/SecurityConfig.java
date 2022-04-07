package com.kshz.fakebookserver.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http.antMatcher("/**").authorizeRequests() // authorize all rquest
			.antMatchers("/auth/**").permitAll() // except auth one
			.anyRequest()
			.authenticated();
		
		http.formLogin();
		http.httpBasic();
	}
}
