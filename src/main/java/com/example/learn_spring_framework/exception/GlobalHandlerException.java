package com.example.learn_spring_framework.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.learn_spring_framework.dto.response.ErrorResponse;

import jakarta.persistence.NoResultException;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;

/*This class provides global way to handle exceptions and customize error responses
 * across @RestController classes in an application. 
 * 
 * Annotated with @ResponseBody so any value return from its method
 * are serialized into the HTTP response body.
 */
@RestControllerAdvice
public class GlobalHandlerException {
	
	@ExceptionHandler(MethodArgumentNotValidException.class) //Define what's error in this method will be captured
	@ResponseStatus(HttpStatus.BAD_REQUEST) //Define type of status that returns to client
	public ErrorResponse handValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = "Dữ liệu không hợp lệ";
		//Check if errors exist
		if(ex.getBindingResult().hasErrors()) {
			FieldError error = ex.getBindingResult().getFieldError(); //Get the first errors, field error return first error in  
			if (error != null) {
				errorMessage = error.getDefaultMessage(); 
				//get the message from annotation validation in dto 
			}
		}
		return new ErrorResponse(400, LocalDateTime.now(), errorMessage); //return the DTO to client
	}
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleConflict(IllegalStateException ex) {
		return new ErrorResponse(409, LocalDateTime.now(), ex.getMessage());
	}
	
	@ExceptionHandler(NoResultException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleNotFound(NoResultException ex) {
		return new ErrorResponse(404, LocalDateTime.now(), ex.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(IllegalArgumentException ex) {
        return new ErrorResponse(400, LocalDateTime.now(), ex.getMessage());
    }
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorResponse handleISError(Exception ex) {
		return new ErrorResponse(500, LocalDateTime.now(), ex.getMessage());
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorResponse handleBadCredential(BadCredentialsException ex) {
		return new ErrorResponse(401, LocalDateTime.now(), "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
	}
	
	@ExceptionHandler(DisabledException.class) 
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleDisabled(DisabledException ex) {
		return new ErrorResponse(409, LocalDateTime.now(), ex.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleForbidden(AccessDeniedException ex) {
		return new ErrorResponse(403, LocalDateTime.now(), "Bạn không có quyền thực hiện thao tác này.");
	}
}

