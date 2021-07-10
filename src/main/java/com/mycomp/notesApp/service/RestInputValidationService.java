package com.mycomp.notesApp.service;

import com.mycomp.notesApp.exceptions.InputParameterInvalidException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * This is implementation of Generic ValidationService providing validations for
 * REST input
 * 
 * this demonstrate the benefit of Composition over inheritance existing code
 * doesn't need to be changed for enhancements
 * 
 * @author Rahil
 *
 */
@Service
@Slf4j
@Qualifier("RESTInputValidator")
public class RestInputValidationService extends ValidationService {

	@Override
	public boolean validate() throws InputParameterInvalidException {
		boolean isValid;
		try {
			isValid = this.getValidatableObj()
			              .validate();
		} catch (Exception e) {
			log.error("Input validation failed");
			throw new InputParameterInvalidException("Invalid REST input", e);
		}
		return isValid;
	}

}
