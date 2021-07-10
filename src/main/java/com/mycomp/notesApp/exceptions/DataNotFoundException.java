package com.mycomp.notesApp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Requested data not found")
@Data
@AllArgsConstructor
@ApiModel(description = "Exception to be thrown when Unable to find requested data in the system")
@EqualsAndHashCode(callSuper = false)
public class DataNotFoundException extends RuntimeException {

	private String errorMessage;
	private Throwable exception;
	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -8790211652911971729L;

	public DataNotFoundException(String errorMessage) {
		super(errorMessage);
	}

}
