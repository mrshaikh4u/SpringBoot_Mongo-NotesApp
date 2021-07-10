package com.mycomp.notesApp.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.Id;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Note {

	@ApiModelProperty(notes = "Notes's id")
	@Id
	private String noteID;
	
	@ApiModelProperty(notes = "Notes's title")
	private String noteTitle;

	@ApiModelProperty(notes = "Note's create date")
	private LocalDateTime createDate;

	@ApiModelProperty(notes = "Notes's update id")
	private LocalDateTime updateDate;

	@ApiModelProperty(notes = "Notes's Text")
	private String noteText;

	@ApiModelProperty(notes = "Notes's tags")
	private List<String>  tags;

}
