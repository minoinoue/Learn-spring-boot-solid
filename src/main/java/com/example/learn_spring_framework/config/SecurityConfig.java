package com.example.learn_spring_framework.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.learn_spring_framework.service.UserService;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
	
	private final PasswordEncoder bcrypt;
	
	@Autowired
	public SecurityConfig(PasswordEncoder bcrypt) {
		this.bcrypt = bcrypt;
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
			.authorizeHttpRequests((requests) -> requests 
					/*Start to configure AuthZ for HTTP requests
					 */
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
			)
			.httpBasic(Customizer.withDefaults());
					/*Configure AuthN method
					 * 
					 * -> Purpose: When customer access unlock site, website will
					 * show you a pop-up that requires you to sign in (default)
					 */
			return http.build(); //return the configure object to Spring
	}

	/*Configure AuthenticationProvider
	 * 
	 * Purpose: verifing user credentials during authenticaion process (maybe combine with LDAP, Oauth, SAML)
	 * 
	 * DaoAuthenticationProvider: 
	 * JwtAuthenticationProvider
	 * RememberMeAuthenticationProvider
	 * LdapAuthenticationProvider
	 * OpenIDAuthenticaitonProvider
	 * 
	 */
    @Bean
    public AuthenticationProvider authenticationProvider(UserService user) {
    	//DAP This likes brain that combines UserDetailsService (find user) and PasswordEncoder (check pass)
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(user); // find user from service
        provider.setPasswordEncoder(bcrypt);     // checking pass
        return provider;
    }
}
