package com.example.learn_spring_framework.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.learn_spring_framework.exception.InvalidJwtException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.security.SignatureException;

/*file util stores methods, class, or reuse code
 * purpose -> handle string, date time, file data
 * 
 * JWTUtil has responsibility for create Token when sign in and check
 * token when client send request. 
 * 
 * It holds secret key to sign and authentication
 * 
 * Flow:
 * -> Login: User send user/pass -> if Server checks OK -> Call JWTUtil.generateToken(username) 
 * -> return token to user
 * -> User send request + token -> AuthTokenFilter call validateJWT to check token
 * -> call getUsername to know who is calling -> acp request.
 */
@Component 
public class JWTUtil {
	@Value("${jwt.secret}") //inject the value in application.properties to keep security
	private String jwtSecret;
	
	@Value("${jwt.expiration}")
	private int jwtExpirationsMs;
	
	private SecretKey key; //hold secret key after encode to sign and decode
	/*
	 * initializes the key after the class is instantiated and the jwtSecret
	 * is preventing the repeated creation of the key and enhancing
	 * performance (medium.com)
	 */
	
	//Marks a method that should run exactly once after the initialization of bean properties
	@PostConstruct
	public void init() {
		//transform jwtSecret from String to object SecretKey by HMAC-SHA algorithm
		//optimize -> not to create key again every time we take request
		this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
	
	//Generate JWT Token
	public String generateToken(String userName) {
		return Jwts.builder()
				.setSubject(userName) //save user name into token (Payload) to check who is author of token
				.setIssuedAt(new Date()) //Create at: right now
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationsMs)) //Expiration in: now + ms configuration
				.signWith(key, SignatureAlgorithm.HS256) //sign with secret key and HS256
				.compact(); // convert into String token
	}
	
	//Get username from JWT token
	/*
	 * When client send token, we use this method to know who is authorize this token.
	 */
	public String getUserNameFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key).build() //give key to open token
				.parseClaimsJws(token) //read and check sign
				.getBody() 
				.getSubject(); //get information of the author of token.
	}
	
	//Validate JWT Token -> Like a scanner
	/*
	 * Check validation of token
	 * if open -> true
	 * if token was expired or changed -> throw exceptions
	 * 
	 */
	public void validateJwtToken(String token) {
		try {
		Jwts.parserBuilder().setSigningKey(key).build()
		.parseClaimsJws(token);
		} catch (SignatureException e) {
			throw new InvalidJwtException("Chữ ký Token không hợp lệ (Invalid Signature)", e);
		} catch (MalformedJwtException e) {
			throw new InvalidJwtException("Token không hợp lệ (Malformed)", e);
		} catch (ExpiredJwtException e) {
			throw new InvalidJwtException("Token đã hết hạn (Expired)", e);
		} catch (UnsupportedJwtException e) {
			throw new InvalidJwtException("Token không được hỗ trợ (Unsupported)", e);
		} catch (IllegalArgumentException e) {
			throw new InvalidJwtException("Token rỗng hoặc lỗi tham số (Illegal Argument)", e);
		}
	}
}
