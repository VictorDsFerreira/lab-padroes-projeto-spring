package one.digitalinnovation.gof.controller;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import one.digitalinnovation.gof.service.impl.ResourceNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", HttpStatus.NOT_FOUND.value());
		body.put("error", "Not Found");
		body.put("message", ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Validation failed");
		body.put("message", ex.getBindingResult().getFieldErrors());
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Map<String, Object>> handleConstraintViolation(ConstraintViolationException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", HttpStatus.BAD_REQUEST.value());
		body.put("error", "Constraint violation");
		body.put("message", ex.getMessage());
		return ResponseEntity.badRequest().body(body);
	}
}


