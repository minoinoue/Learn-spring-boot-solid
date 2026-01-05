package com.example.learn_spring_framework.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.learn_spring_framework.dto.response.ApiResponse;

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
	public ApiResponse<Void> handValidationException(MethodArgumentNotValidException ex) {
		String errorMessage = "Dữ liệu không hợp lệ";
		//Check if errors exist
		if(ex.getBindingResult().hasErrors()) {
			FieldError error = ex.getBindingResult().getFieldError(); //Get the first errors, field error return first error in  
			if (error != null) {
				errorMessage = error.getDefaultMessage(); 
				//get the message from annotation validation in dto 
			}
		}
		return new ApiResponse<Void>(LocalDateTime.now(), 400, errorMessage); //return the DTO to client
	}
	
	@ExceptionHandler(IllegalStateException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse<Void> handleConflict(IllegalStateException ex) {
		return new ApiResponse<Void>(LocalDateTime.now(), 409, ex.getMessage());
	}
	
	@ExceptionHandler(NoResultException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ApiResponse<Void> handleNotFound(NoResultException ex) {
		return new ApiResponse<Void>(LocalDateTime.now(), 404, ex.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleBadRequest(IllegalArgumentException ex) {
        return new ApiResponse<Void>(LocalDateTime.now(), 400, ex.getMessage());
    }
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse<Void> handleISError(Exception ex) {
		return new ApiResponse<Void>(LocalDateTime.now(), 500, ex.getMessage());
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ApiResponse<Void> handleBadCredential(BadCredentialsException ex) {
		return new ApiResponse<Void>(LocalDateTime.now(), 401, "Tên đăng nhập hoặc mật khẩu không đúng. Vui lòng thử lại.");
	}
	
	@ExceptionHandler(DisabledException.class) 
	@ResponseStatus(HttpStatus.CONFLICT)
	public ApiResponse<Void> handleDisabled(DisabledException ex) {
		return new ApiResponse<Void>(LocalDateTime.now(), 409, ex.getMessage());
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ApiResponse<Void> handleForbidden(AccessDeniedException ex) {
		return new ApiResponse<Void>(LocalDateTime.now(), 403, "Bạn không có quyền thực hiện thao tác này.");
	}
}

