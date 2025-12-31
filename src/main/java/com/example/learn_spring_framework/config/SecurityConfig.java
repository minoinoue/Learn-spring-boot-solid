package com.example.learn_spring_framework.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.learn_spring_framework.filter.AuthTokenFilter;
import com.example.learn_spring_framework.security.AuthEntryPointJwtSecurity;
import com.example.learn_spring_framework.service.UserService;

import org.springframework.security.authentication.AuthenticationManager;



/*Spring Security provide authentication and authorization and protection
 * 
 * + AuthN : verifies user identity
 * + Authorization: Controls access using roles and authorities
 * + Filter Chain: Handle request, response between client and dispatcher servlet
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
	private final AuthEntryPointJwtSecurity unauthorizeHandler;
	private final AuthTokenFilter authTokenFilter;
	
	@Autowired
	public SecurityConfig(AuthEntryPointJwtSecurity unauthorizeHandler, UserService userService, PasswordEncoder bcrypt, AuthTokenFilter authTokenFilter) {
		this.unauthorizeHandler = unauthorizeHandler;
		this.authTokenFilter = authTokenFilter;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(
			AuthenticationConfiguration authenticationConfiguration
			) throws Exception {
			return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	/*
	 * SecurityFilterChain object will be used for security of application
	 * 
	 * "HttpSecurity http" is a builder object helps you to configure principles like who can
	 * access this site or how to sign in.
	 * 
	 * --> Purpose: Create Security API flow 
	 */
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		//  Disable CSRF to test postman
        	.csrf(csrf -> csrf.disable())
        	.cors(cors -> cors.disable())
        	.exceptionHandling(exceptionHandling ->
        			exceptionHandling.authenticationEntryPoint(unauthorizeHandler)
        	)
        	.sessionManagement(sessionManagement -> 
        			sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	)
			.authorizeHttpRequests((requests) -> requests 
					/*Start to configure AuthZ for HTTP requests
					 */
					.requestMatchers("/api/auth/**").permitAll()
					.requestMatchers("/api/students/add_student").permitAll()
					/* Choose requests that send to main page (/) or (/index)
					 * 
					 * permitAll(): allow everyone
					 * 
					 * -> Purpose: Public this site, customer do not need to sign in to access this site
					 * Using for login page or loading page 
					 */
					.anyRequest().authenticated()
					/* Other requests must to sign in
					 * 
					 * -> Purpose: Protect all lefts of the application.
					 */
			);
			http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

			return http.build(); //return the configure object to Spring
	}
}
