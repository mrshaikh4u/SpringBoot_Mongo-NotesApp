package com.mycomp.notesApp.utils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * This is Util class for User transaction mapping it uses ModelMapper
 * 
 * @author Rahil
 *
 */
@Component
@Data
public class UserTransactionMapper {

	private ModelMapper transactionMapper;

	public UserTransactionMapper() {
		transactionMapper = new ModelMapper();
	}

}
