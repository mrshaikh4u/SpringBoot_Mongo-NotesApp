package com.mycomp.notesApp.service;

import com.mycomp.notesApp.exceptions.InputParameterInvalidException;
import com.mycomp.notesApp.to.Validatable;

import lombok.Data;

/**
 * This is generic class for Validation service it rely on strategy design
 * pattern where at runtime caller can decide specific validation strategy. it
 * favors composition over inheritance (eg.Validatable ) this way it is easy to
 * plug any validatable object at runtime and code doesn't need to be touched (
 * hence code is closed for modification but open for extension )
 * 
 * @author Rahil
 *
 */
@Data
public abstract class ValidationService {

	private Validatable validatableObj;

	public abstract boolean validate() throws InputParameterInvalidException;

}
