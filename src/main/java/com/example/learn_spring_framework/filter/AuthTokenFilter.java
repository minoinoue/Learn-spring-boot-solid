package com.example.learn_spring_framework.filter;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.learn_spring_framework.dto.response.ErrorResponse;
import com.example.learn_spring_framework.exception.InvalidJwtException;
import com.example.learn_spring_framework.service.UserService;
import com.example.learn_spring_framework.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*This class AuthTokenFilteracts as a "Security Checkpoint" . 
 * It intercepts every request sent to the server 
 * to verify whether the user is carrying a valid "token".
 * 
 * -> Security like a security system design drawing
 * -> JWT Util like a card scanner
 * -> AuthTokenFilter like a security guard hold the JWTUtil to do specific tasks
 */
@Component
//extends OncePerRequestFilter to make sure that filter run only 1 for 1 request
public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JWTUtil jwtUtils;
	
	@Autowired
	//take detail informations from user from database base on username from token
	private  UserService userService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
			) throws ServletException, IOException {
		
		try {
			String jwt = parseJwt(request); //take token string from header
			if (jwt != null) {
				jwtUtils.validateJwtToken(jwt);
				// check token if valid or expired?
				
				String userName = jwtUtils.getUserNameFromToken(jwt);
				UserDetails userDetails = userService.loadUserByUsername(userName);
				//get username from token and get all details of user
				
				//create authentication object
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null, //null becasuse we authentication through token, no need to check pass
								userDetails.getAuthorities() //get all roles
						);
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
						//notify that user is sign in successful 
			}
			filterChain.doFilter(request, response);
			//move request to next filter or to controller to handle
		} catch (InvalidJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json;charset=UTF-8");
			ErrorResponse errorResponse = new ErrorResponse(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    LocalDateTime.now(),
                    e.getMessage() // Lấy thông báo lỗi chi tiết (Hết hạn, sai chữ ký...)
            );
			final ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.writeValue(response.getOutputStream(), errorResponse);
		} 
	}
	
	/*In RESTful standards, tokens are typically sent in the header in
	 * Authorization: Bearer xxxxx.yyyyy.zzzzz
	 * This function checks for the word "Bearer" at the beginning
	 * then removes the first 7 characters ("Bearer " + space) to extract the token code that follows.
	 */
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (headerAuth != null && headerAuth.startsWith("Bearer")) {
	        if (headerAuth.length() >= 7) {
	             return headerAuth.substring(7);
	        } else {
	             return ""; // violate null error validateJwtToken
	        }
	    }
	    return null;
	}
	

}
