package com.mycomp.notesApp.to;

/**
 * Any class implementing this interface assume to be validatable entity
 * 
 * @author Rahil
 *
 */
public interface Validatable {
	public boolean validate() throws Exception;
}
