package com.mycomp.notesApp.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.mycomp.notesApp.errors.ApiValidationError;
import com.mycomp.notesApp.errors.AppError;
import com.mycomp.notesApp.exceptions.DataMappingException;
import com.mycomp.notesApp.exceptions.DataNotFoundException;
import com.mycomp.notesApp.exceptions.DataNotSavedException;
import com.mycomp.notesApp.exceptions.InputParameterInvalidException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * This class handles application exceptions globally. each method is intend to
 * provide handling for specific exception and provides meaningful errorCode and
 * errorMessage and returns HTTP status code
 * 
 * @author Rahil
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the AppError object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new AppError(BAD_REQUEST, error, ex));
	}

	@ExceptionHandler(InputParameterInvalidException.class)
	protected ResponseEntity<Object> handleInputParameterInvalidException(InputParameterInvalidException ex) {
		AppError error = new AppError(HttpStatus.BAD_REQUEST);
		error.setMessage(ex.getMessage());
		error.setSubErrors(Stream
		                         .of(new ApiValidationError(ex.getObjectName(), ex.getFieldName(), ex.getFieldValue(),
		                                 ex.getErrorMessage()))
		                         .collect(Collectors.toList()));
		if (ex.getException() != null)
			error.setDebugMessage(ex.getException()
			                        .getLocalizedMessage());
		return buildResponseEntity(error);
	}

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {
		return super.handleMissingPathVariable(ex, headers, status, request);
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the AppError object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		AppError error = new AppError(BAD_REQUEST);
		error.setMessage("Validation error");
		error.addValidationErrors(ex.getBindingResult()
		                            .getFieldErrors());
		error.addValidationError(ex.getBindingResult()
		                           .getGlobalErrors());
		return buildResponseEntity(error);
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the AppError object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		AppError error = new AppError(BAD_REQUEST);
		error.setMessage("Validation error 1");
		error.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(error);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleGenericExceptions(Exception ex) {
		AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setMessage("Internal error");
		error.setDebugMessage(ex.getLocalizedMessage());
		return buildResponseEntity(error);
	}

	/**
	 * Handles EntityNotFoundException. Created to encapsulate errors with more
	 * detail than javax.persistence.EntityNotFoundException.
	 *
	 * @param ex the EntityNotFoundException
	 * @return the AppError object
	 */
	@ExceptionHandler(DataNotFoundException.class)
	protected ResponseEntity<Object> handleEntityNotFound(DataNotFoundException ex) {
		AppError error = new AppError(NOT_FOUND);
		error.setMessage(ex.getMessage());
		if (ex.getException() != null)
			error.setDebugMessage(ex.getException()
			                        .getLocalizedMessage());
		return buildResponseEntity(error);
	}

	@ExceptionHandler(DataMappingException.class)
	protected ResponseEntity<Object> handleDataMapping(DataMappingException ex) {
		AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setMessage(ex.getMessage());
		if (ex.getException() != null)
			error.setDebugMessage(ex.getException()
			                        .getLocalizedMessage());
		return buildResponseEntity(error);
	}

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      HttpMessageNotReadableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the AppError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		ServletWebRequest servletWebRequest = (ServletWebRequest) request;
		log.info("{} to {}", servletWebRequest.getHttpMethod(), servletWebRequest.getRequest()
		                                                                         .getServletPath());
		String error = "Malformed JSON request";
		return buildResponseEntity(new AppError(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle HttpMessageNotWritableException.
	 *
	 * @param ex      HttpMessageNotWritableException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the AppError object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
	        HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Error writing JSON output";
		return buildResponseEntity(new AppError(HttpStatus.INTERNAL_SERVER_ERROR, error, ex));
	}

	/**
	 * Handle NoHandlerFoundException.
	 *
	 * @param ex
	 * @param headers
	 * @param status
	 * @param request
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
	        HttpStatus status, WebRequest request) {
		AppError error = new AppError(BAD_REQUEST);
		error.setMessage(
		        String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
		error.setDebugMessage(ex.getMessage());
		return buildResponseEntity(error);
	}

	/**
	 * Handle Exception, handle generic Exception.class
	 *
	 * @param ex the Exception
	 * @return the AppError object
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
	        WebRequest request) {
		AppError error = new AppError(BAD_REQUEST);
		error.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
		        ex.getName(), ex.getValue(), ex.getRequiredType()
		                                       .getSimpleName()));
		error.setDebugMessage(ex.getMessage());
		return buildResponseEntity(error);
	}

	private ResponseEntity<Object> buildResponseEntity(AppError error) {
		return new ResponseEntity<>(error, error.getStatus());
	}

	@ExceptionHandler(DataNotSavedException.class)
	protected ResponseEntity<Object> handleDataNotSavedException(DataNotSavedException ex) {
		AppError error = new AppError(HttpStatus.INTERNAL_SERVER_ERROR);
		error.setMessage(ex.getMessage());
		if (ex.getException() != null)
			error.setDebugMessage(ex.getException()
			                        .getLocalizedMessage());
		return buildResponseEntity(error);
	}

}
