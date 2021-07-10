package com.mycomp.notesApp.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.mycomp.notesApp.exceptions.DataNotFoundException;
import com.mycomp.notesApp.exceptions.DataNotSavedException;
import com.mycomp.notesApp.service.NoteService;
import com.mycomp.notesApp.to.NoteTO;
import com.mycomp.notesApp.to.NotesSummaryTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Controller class for notes management APIs
 * 
 * @author Rahil
 *
 */
@RestController
@RequestMapping("/api/v1")
@Api(value = "Notes Management APIs", description = "Operations for notes management")
public class NotesController {

	@Autowired
	protected NoteService notesService;

	@PostMapping("/notes")
	@ApiOperation(value = "Add new Note in the system,returns added note", response = NoteTO.class)
	public NoteTO createNote(
	        @Valid @RequestBody @ApiParam(value = "input notes's details that needs to be added", required = true) NoteTO inputNote)
	        throws DataNotSavedException {
		return notesService.createNote(inputNote);
	}

	@GetMapping("/notes/{noteID}")
	@ApiOperation(value = "Search note,returns note if found", response = NoteTO.class)
	public NoteTO searchNote(@PathVariable @ApiParam(value = "note id to search") String noteID)
	        throws DataNotFoundException {
		return notesService.searchNote(noteID);
	}

	@GetMapping("/notes/stats/{noteID}")
	@ApiOperation(value = "returns notes statistics", response = NoteTO.class)
	public Map<String, Integer> notesStats(@PathVariable @ApiParam(value = "notes id to search") String noteID)
	        throws DataNotFoundException {
		return notesService.statsPerNote(noteID);
	}
	
	@GetMapping("/notes")
	@ApiOperation(value = "Fetch all available notes in the system,returns available notes")
	public List<NoteTO> listNotes() throws DataNotFoundException {
		return notesService.listNotes();
	}

	@GetMapping("/notes/summary")
	@ApiOperation(value = "Fetch all available notes in the system only title and text , supports pagination, filter by tags and sort by create date descending")
	public List<NotesSummaryTO> listNotesSummary(@Param(value = "page") int page, 
	@Param(value = "size") int size,@Param(value="filters") String[] filters) throws DataNotFoundException {
		return notesService.listNotesSummary(page,size,filters);
	}

	@GetMapping("/notes/text/{noteID}")
	@ApiOperation(value = "returns note's text", response = NoteTO.class)
	public String notesText(@PathVariable @ApiParam(value = "note id to search") String noteID)
	        throws DataNotFoundException {
		return notesService.getNotesText(noteID);
	}

	@PutMapping("/notes/{noteID}")
	@ApiOperation(value = "Update note in the system ,returns modified note details", response = NoteTO.class)
	public NoteTO updateNote(@Valid @RequestBody @ApiParam(value = "Note to update") NoteTO input,
	        @PathVariable @ApiParam(value = "noteID which needs to be updated") String noteID)
	        throws DataNotSavedException {
		input.setNoteID(noteID);
		return notesService.updateNote(input);
	}

	@DeleteMapping("/notes/{noteID}")
	@ApiOperation(value = "Delete note in the system ,returns 200 success OK upon succesful deletion")
	void deleteNote(@PathVariable @ApiParam(value = "note id to be deleted") String noteID)
	        throws DataNotSavedException {
		notesService.deleteNote(noteID);
	}

}
