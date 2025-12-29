package com.example.learn_spring_framework.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

/*Spring Security provide authentication and authorization and protection
 * 
 * + AuthN : verifies user identity
 * + Authorization: Controls access using roles and authorities
 * + Filter Chain: Handle request, response between client and dispatcher servlet
 */

@Configuration
@EnableWebSecurity
public class SecurityConfig{
	
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
			.authorizeHttpRequests((requests) -> requests 
					/*Start to configure AuthZ for HTTP requests
					 */
					.requestMatchers("/", "index").permitAll()
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
			)
			.httpBasic(Customizer.withDefaults());
					/*Configure AuthN method
					 * 
					 * -> Purpose: When customer access unlock site, website will
					 * show you a pop-up that requires you to sign in (default)
					 */
			return http.build(); //return the configure object to Spring
	}
	
	/*
	 * Manage user data
	 * 
	 * Purpose: This is an interface checks that user X is truly exist or not
	 * If it truly has, so what are there user name, password or role?
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.withDefaultPasswordEncoder()
				/*Create an user with default password to test
				 * 
				 */
				.username("username")
				.password("password")
				.roles("USER") // -> this is AuthZ
				.build(); // -> return UserDetails object
		return new InMemoryUserDetailsManager(user);
		//Create a management system that store in RAM
	}
}
