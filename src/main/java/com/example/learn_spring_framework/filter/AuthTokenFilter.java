package com.example.learn_spring_framework.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.learn_spring_framework.exception.InvalidJwtException;
import com.example.learn_spring_framework.service.UserService;
import com.example.learn_spring_framework.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {
	
	@Autowired
	private JWTUtil jwtUtils;
	
	@Autowired
	private  UserService userService;

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain filterChain
			) throws ServletException, IOException {
		try {
			String jwt = parseJwt(request);
			if (jwt != null) {
				jwtUtils.validateJwtToken(jwt);
				String userName = jwtUtils.getUserNameFromToken(jwt);
				UserDetails userDetails = userService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
								userDetails,
								null,
								userDetails.getAuthorities()
						);
						authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			filterChain.doFilter(request, response);
		} catch (InvalidJwtException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	        response.setContentType("application/json;charset=UTF-8");
	        response.getWriter().write("{\"status\": 401, \"message\": \"" + e.getMessage() + "\"}");	
		} catch (Exception e) {
			logger.error("Không thể thiết lập quyền truy cập người dùng: {}", e);
            filterChain.doFilter(request, response);
		}
		
	}
	
	private String parseJwt(HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
	
	

}
