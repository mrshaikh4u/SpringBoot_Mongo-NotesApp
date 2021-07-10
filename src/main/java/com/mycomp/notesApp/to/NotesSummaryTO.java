package com.mycomp.notesApp.to;

import java.time.LocalDateTime;
import javax.validation.constraints.NotEmpty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Notes Transfer object
 * @author Rahil
 */
@Data
@ApiModel(description = "Transfer Object for notes summary")
@AllArgsConstructor
@NoArgsConstructor
public class NotesSummaryTO{


	@ApiModelProperty(notes = "Notes's id")
	private String noteID;
	
	@ApiModelProperty(notes = "Notes's title")
	@NotEmpty(message = "Note title can not be empty")
	private String noteTitle;

	@ApiModelProperty(notes = "notes's created date")
	private LocalDateTime createDate;
	

}
