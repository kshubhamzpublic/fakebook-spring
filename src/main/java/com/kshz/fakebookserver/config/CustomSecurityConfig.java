package com.kshz.fakebookserver.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.kshz.fakebookserver.jwt.JWTAuthenticationEntryPoint;
import com.kshz.fakebookserver.jwt.JWTRequestFilter;

@Configuration
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JWTAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JWTRequestFilter jwtRequestFilter;
	
	@Value("${cors.origin.allow}")
	private String allowedOrigins;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.cors().configurationSource(request -> {
			CorsConfiguration cors = new CorsConfiguration();
			if (allowedOrigins != null) {
				Arrays.asList(allowedOrigins.split(","))
					.stream()
					.forEach(origin -> cors.addAllowedOrigin(origin));
			}
			return cors;
		});
		
		http.antMatcher("/**").authorizeRequests() // authorize all rquest
			.antMatchers("/auth/**", "/post/all").permitAll() // except auth one
			.anyRequest()
			.authenticated();
		
		// use stateless session; session won't be used to
		// store user's state.
		http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// Add a filter to validate the tokens with every request
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
