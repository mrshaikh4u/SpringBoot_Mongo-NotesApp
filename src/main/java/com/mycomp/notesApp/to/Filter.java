package com.mycomp.notesApp.to;

import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(description = "All details about a note")
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    private List<String> tags;
}
