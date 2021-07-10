package com.mycomp.notesApp.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API status
 * 
 * @author Rahil
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "All details about API Status")
public class Status {
	@ApiModelProperty(notes = "status code")
	private ErrorCode statusCode;
	@ApiModelProperty(notes = "status description")
	private String description;
}
