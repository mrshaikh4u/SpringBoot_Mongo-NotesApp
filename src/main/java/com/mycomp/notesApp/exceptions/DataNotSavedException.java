package com.mycomp.notesApp.exceptions;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Unable to save data")
@Data
@AllArgsConstructor
@ApiModel(description = "Exception to be thrown when Unable to update data in the system")
public class DataNotSavedException extends SQLException {

	private String errorMessage;
	private Throwable exception;
	/**
	 * Unique ID for Serialized object
	 */
	private static final long serialVersionUID = -8790211652911971729L;

	public DataNotSavedException(String message) {
		super(message);
	}
}
