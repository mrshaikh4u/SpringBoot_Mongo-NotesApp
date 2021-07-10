package com.mycomp.notesApp.service;

import java.util.List;
import java.util.Map;
import com.mycomp.notesApp.exceptions.DataNotFoundException;
import com.mycomp.notesApp.exceptions.DataNotSavedException;
import com.mycomp.notesApp.to.NoteTO;
import com.mycomp.notesApp.to.NotesSummaryTO;

/**
 * Notes service with supported operations
 * 
 * @author Rahil
 *
 */
public interface NoteService {
	public NoteTO createNote(NoteTO inputNote) throws DataNotSavedException;

	public NoteTO updateNote(NoteTO inputNote) throws DataNotSavedException;

	public boolean deleteNote(String noteID) throws DataNotSavedException;

	public List<NoteTO> listNotes() throws DataNotFoundException;

	public List<NoteTO> listNotesPageable(int page,int size) throws DataNotFoundException;

	public List<NoteTO> listNotesFiltered(List<String> tags) throws DataNotFoundException;
	
	public List<NotesSummaryTO> listNotesSummary(int page, int size, String[] filters) throws DataNotFoundException;

	public boolean noteExists(String id) throws DataNotFoundException;

	public NoteTO searchNote(String noteID) throws DataNotFoundException;

	public Map<String,Integer> statsPerNote(String noteID) throws DataNotFoundException;

	public String getNotesText(String noteID) throws DataNotFoundException;

}
