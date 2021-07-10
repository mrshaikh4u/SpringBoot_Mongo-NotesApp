package com.mycomp.notesApp.to;

import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.validation.constraints.NotEmpty;
import com.mycomp.notesApp.exceptions.InputParameterInvalidException;
import com.mycomp.notesApp.utils.CommonUtils;
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
@ApiModel(description = "All details about a note")
@AllArgsConstructor
@NoArgsConstructor
public class NoteTO implements Validatable {


	@ApiModelProperty(notes = "Notes's id")
	private String noteID;
	
	@ApiModelProperty(notes = "Notes's title")
	@NotEmpty(message = "Note title can not be empty")
	private String noteTitle;

	@ApiModelProperty(notes = "notes's created date")
	private LocalDateTime createDate;

	@ApiModelProperty(notes = "notes's created date")
	private LocalDateTime updateDate;

	@ApiModelProperty(notes = "Notes's Text")
	@NotEmpty(message = "Note text can not be empty")
	private String noteText;

	@ApiModelProperty(notes = "Notes's tags")
	private ArrayList<String>  tags;

	/**
	 * more business validations can be added currently just checking for title
	 */
	@Override
	public boolean validate() throws InputParameterInvalidException {
		if (noteTitle == null || noteTitle.trim().isEmpty()) {
			throw new InputParameterInvalidException("NoteTO", "notes title not passed in input", "title",
			        "null/empty");
		}
		if (noteText== null || noteText.trim().isEmpty()) {
			throw new InputParameterInvalidException("NoteTO", "notes text not passed in input", "text",
			        "null/empty");
		}
		if (tags != null && !tags.stream().allMatch(CommonUtils::hasValidTag)) {
			throw new InputParameterInvalidException("NoteTO", "notes tags are not valid", "tags",
			        "null/empty/invalid value");
		}
		return true;
	}

}
