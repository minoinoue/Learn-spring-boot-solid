package com.example.learn_spring_framework.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.learn_spring_framework.filter.AuthTokenFilter;
import com.example.learn_spring_framework.security.AuthEntryPointJwtSecurity;

import org.springframework.security.authentication.AuthenticationManager;



/*Spring Security provide authentication and authorization and protection
 * 
 * + AuthN : verifies user identity
 * + Authorization: Controls access using roles and authorities
 * + Filter Chain: Handle request, response between client and dispatcher servlet
 * 
 *Flow: 
 *-> It goes through SecurityFilterChain
 *-> Meet AuthTokenFilter to check token jwt, if right -> acp authentication
 *-> goes to authorizeHttpRequest to check if url can be access
 *-> If everything OK -> request goes to Controller, otherwise, AuthEntryPointJwtSecurity return error 
 *
 *Purpose: To establish security rules: disable CSRF/CORS, define which APIs are public ( permitAll) 
 *or need to be secure ( authenticated), disable Session (Stateless), 
 *and arrange the position of filters ( AuthTokenFilterrun first).
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //enable to use @PreAuthorize in Controller to check authorization before run method
public class SecurityConfig{
	
	//Handle error when user try to get information when not sign in yet 
	private final AuthEntryPointJwtSecurity unauthorizeHandler;
	//Custom Filter that hold request, check suitable token before send token inside the system 
	private final AuthTokenFilter authTokenFilter;
	
	@Autowired
	public SecurityConfig(AuthEntryPointJwtSecurity unauthorizeHandler, AuthTokenFilter authTokenFilter) {
		this.unauthorizeHandler = unauthorizeHandler;
		this.authTokenFilter = authTokenFilter;
	}
	
	@Bean
	//main component to do sign in API -> public in Bean so that we could call it in other Bean
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
        	.csrf(csrf -> csrf.disable()) //turn on when use Session/Cookies
          //.cors(cors -> cors.disable()) //turn on when you authorize Front end calls API
        	//configure exception
        	.exceptionHandling(exceptionHandling ->
        			exceptionHandling.authenticationEntryPoint(unauthorizeHandler)
        			//if user doesn't sign in and tries to call security API -> Spring call unauthorizeHandler to return error 401.
        	)
        	//setting not save session, told Security not to create HTTP Session.
        	//1 request goes with token beside
        	.sessionManagement(sessionManagement -> 
        			sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        	)
        	//authorization
			.authorizeHttpRequests((requests) -> requests 
					/*Start to configure AuthZ for HTTP requests
					 */
					.requestMatchers("/api/auth/signin", "/api/auth/refresh-token").permitAll()
					/* Choose requests that send to main page (/) or (/index)
					 * 
					 * permitAll(): allow everyone
					 * 
					 * -> Purpose: Public this site, customer do not need to sign in to access this site
					 * Using for login page or loading page 
					 */
					.requestMatchers("/api/auth/logout").authenticated()
					.anyRequest().authenticated()
					/* Other requests must to sign in
					 * 
					 * -> Purpose: Protect all lefts of the application.
					 */
			);
			// run custom filter jwt before default sign in filter.
			http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

			return http.build(); //return the configure object to Spring
	}
}
