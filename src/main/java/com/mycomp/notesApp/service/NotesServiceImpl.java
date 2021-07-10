package com.mycomp.notesApp.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.mycomp.notesApp.domain.Note;
import com.mycomp.notesApp.domain.TAGS;
import com.mycomp.notesApp.exceptions.DataNotFoundException;
import com.mycomp.notesApp.exceptions.DataNotSavedException;
import com.mycomp.notesApp.exceptions.InputParameterInvalidException;
import com.mycomp.notesApp.repositories.NotesRepository;
import com.mycomp.notesApp.to.NoteTO;
import com.mycomp.notesApp.to.NotesSummaryTO;
import com.mycomp.notesApp.utils.CommonUtils;
import com.mycomp.notesApp.utils.DTOMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * This is Notes service implementation class supporting operations on note
 * 
 * @author Rahil
 *
 */
@Service
@Slf4j
public class NotesServiceImpl implements NoteService {

	@Autowired
	protected DTOMapper dtoMapper;

	@Autowired
	@Qualifier("RESTInputValidator")
	protected ValidationService restValidationService;

	@Autowired
	protected NotesRepository notesRepo;

	/**
	 * This operation adds new note supports writable transaction and performs input
	 * validation before saving
	 * 
	 * performs title check as same title note is not allowed as of now
	 * 
	 * @param inputNote - Service input
	 * @return NoteTO - transfer object
	 */
	@Override
	@Transactional
	public NoteTO createNote(NoteTO inputNote) throws InputParameterInvalidException, DataNotSavedException {
		// TODO User tracking can be added in future
		log.info("User XYZ Trying to add note" + inputNote);
		validateInput(inputNote);
		Optional<Note> notesByTitle;
		try {
			notesByTitle = notesRepo.findByNoteTitle(inputNote.getNoteTitle());
		} catch (Exception ex) {
			log.error("Error while fetching note by title ");
			throw new DataNotFoundException("Error while fetching note by title", ex);
		}
		if (notesByTitle.isPresent()) {
			log.error("note by title already exists ");
			throw new InputParameterInvalidException("note by title already exists");
		}
		Note savedNote = null;
		try {
			Note note = dtoMapper.getMapper().map(inputNote, Note.class);
			note.setCreateDate(LocalDateTime.now());
			log.info("User XYZ Trying to add note" + note);
			savedNote = notesRepo.save(note);

		} catch (Exception ex) {
			log.error("Unable to add note " + inputNote);
			throw new DataNotSavedException("Unable to add note ", ex);
		}
		return dtoMapper.getMapper().map(savedNote, NoteTO.class);

	}

	/**
	 * This method performs REST input validation as this method is programmed to
	 * interface ValidationService, implementation can be changed at any time in
	 * future without touching this code
	 * 
	 * @param inputNote
	 */
	private void validateInput(NoteTO inputNote) {
		restValidationService.setValidatableObj(inputNote);
		boolean isValid = restValidationService.validate();
		if (isValid) {
			log.info("input validation passed");
		}
	}

	/**
	 * This operation supports note modification supports writable transaction first
	 * fetches the existing note with given id then change it as per input and
	 * updates the DB
	 * 
	 * @param inputNote - Service input
	 * @return NoteTO - transfer object
	 */
	@Override
	@Transactional
	public NoteTO updateNote(NoteTO inputNote) throws DataNotSavedException, InputParameterInvalidException {
		log.info("Trying to update note" + inputNote);
		validateInput(inputNote);
		Optional<Note> findByNoteID;
		try {
			findByNoteID = notesRepo.findByNoteID(inputNote.getNoteID());
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while fetching note ").append(inputNote.getNoteID())
					.toString();
			// String errorMsg = "error";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!findByNoteID.isPresent()) {
			String errorMsg = "Note with id : " + inputNote.getNoteID() + " not found";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg);
		}
		Note savedNote = findByNoteID.get();
		prepareNoteToUpdate(savedNote, inputNote);
		log.info("updated note : " + savedNote);
		Note updatedNote = null;
		try {
			updatedNote = notesRepo.save(savedNote);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while modifying note ").append(updatedNote).toString();
			// String errorMsg = "error";
			log.error(errorMsg);
			throw new DataNotSavedException(errorMsg, ex);
		}
		return dtoMapper.getMapper().map(updatedNote, NoteTO.class);

	}

	/**
	 * This method maps note's details passed in input to entity to be modified
	 * 
	 * @param savedNote - Entity to be modified
	 * @param inputNote - input to the service
	 */
	private void prepareNoteToUpdate(Note savedNote, NoteTO inputNote) {
		log.info(new StringBuilder("preparing object to be modified using ").append(inputNote).toString());
		// log.info("error");
		if (!CommonUtils.isStringNullorEmpty(inputNote.getNoteTitle())) {
			savedNote.setNoteTitle(inputNote.getNoteTitle());
		}
		if (!CommonUtils.isStringNullorEmpty(inputNote.getNoteText())) {
			savedNote.setNoteText(inputNote.getNoteText());
		}
		if (!CommonUtils.isListEmpty(inputNote.getTags())) {
			savedNote.setTags(inputNote.getTags());
		}
		savedNote.setUpdateDate(LocalDateTime.now());
	}

	/**
	 * This operation deletes note in the system if exists
	 * 
	 * @return - true if deletion success , false otherwise
	 */
	@Override
	@Transactional
	public boolean deleteNote(String noteID) throws DataNotSavedException {
		// TODO user tracking in future
		log.info("User XYZ trying to delete note " + noteID);
		validateInput(noteID);
		Optional<Note> findByNoteID;
		try {
			findByNoteID = notesRepo.findByNoteID(noteID);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while fetching notes ").append(noteID).toString();
			// String errorMsg = "";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!findByNoteID.isPresent()) {
			throw new DataNotFoundException("Note id " + noteID + "Not found");
		}
		try {
			notesRepo.deleteByNoteID(noteID);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while deleting notes ").append(noteID).toString();
			// String errorMsg = "";
			log.error(errorMsg);
			throw new DataNotSavedException(errorMsg, ex);
		}
		return true;
	}

	/**
	 * Helper method for input validation
	 * 
	 * @param noteID
	 */
	private void validateInput(String noteID) {
		if (noteID == null) {
			throw new InputParameterInvalidException("Note id not passed to delete note", "noteID", "null/empty");
		}
	}

	/**
	 * This operation fetches available notes,supports read only transaction
	 */
	@Override
	@Transactional(readOnly = true)
	public List<NoteTO> listNotes() throws DataNotFoundException {
		// TODO user tracking in future can be added
		log.info("User XYZ trying to fetch all notes");
		List<Note> allNotes;
		try {
			allNotes = notesRepo.findAll();
		} catch (Exception ex) {
			String errorMsg = "Error occured while fetching notes ";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (allNotes == null || allNotes.isEmpty()) {
			log.error("No notes found");
			throw new DataNotFoundException("No notes found");
		}
		List<NoteTO> lstNotesTO = allNotes.stream().map(e -> dtoMapper.getMapper().map(e, NoteTO.class))
				.collect(Collectors.toList());

		return lstNotesTO;
	}

	/**
	 * This operation checks if note exists or not ,supports read only transaction
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean noteExists(String id) throws DataNotFoundException, InputParameterInvalidException {
		validateInput(id);
		boolean isExists;
		try {
			isExists = notesRepo.existsByNoteID(id);
		} catch (Exception ex) {
			String errorMsg = "error occured while fetching notes";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		return isExists;
	}

	/**
	 * This operation searches for note with passed id,supports read only
	 * transaction
	 */
	@Override
	@Transactional(readOnly = true)
	public NoteTO searchNote(String noteID) throws DataNotFoundException {
		// TODO User tracking can be added in future
		log.info("User XYZ trying to search notes" + noteID);
		validateInput(noteID);
		Optional<Note> optionalNote;
		try {
			optionalNote = notesRepo.findByNoteID(noteID);
		} catch (Exception ex) {
			// String errorMsg = new StringBuilder("Error occured while fetching notes
			// ").append(noteID)
			// .toString();
			String errorMsg = "error";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!optionalNote.isPresent()) {
			throw new DataNotFoundException("Note id " + noteID + " Not found");
		}
		return dtoMapper.getMapper().map(optionalNote.get(), NoteTO.class);
	}

	@Override
	public Map<String, Integer> statsPerNote(String noteID) throws DataNotFoundException {
		log.info("User XYZ trying to check note's statistics" + noteID);
		validateInput(noteID);
		Optional<Note> optionalNote;
		try {
			optionalNote = notesRepo.findByNoteID(noteID);
		} catch (Exception ex) {
			String errorMsg = new StringBuilder("Error occured while fetching notes ").append(noteID).toString();
			// String errorMsg = "error";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!optionalNote.isPresent()) {
			throw new DataNotFoundException("Note id " + noteID + " Not found");
		}
		Note noteReturned = optionalNote.get();
		String text = noteReturned.getNoteText();
		Map<String, Integer> output = new HashMap<String, Integer>();
		for (String word : text.split(" ")) {
			output.put(word, output.getOrDefault(word, 0) + 1);
		}
		return output.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	/**
	 * This operation returns note's listing summary
	 * supports
	 * pagination
	 * filtering by tag
	 * sorting by creation date descending
	 * @param page - page number
	 * @param size - each page size
	 * @param filters - list of tags to filter
	 * @return - List of notes defined as per above description
	 */
	@Override
	public List<NotesSummaryTO> listNotesSummary(int page, int size, String[] filters) throws DataNotFoundException {
		// TODO user tracking in future can be added
		log.info("User XYZ trying to fetch all notes");
		Page<Note> allNotes;
		try {
			if (filters == null || filters.length == 0) {	
				Pageable requestedPage = PageRequest.of(page, size, Sort.by("createDate").descending());
				allNotes = notesRepo.findAll(requestedPage);
			} else {
				List<String> tags = Arrays.asList(filters);
				Pageable requestedPage = PageRequest.of(page, size, Sort.by("createDate").descending());
				log.info("searching for : " + tags);
				allNotes = notesRepo.findAnyOfTheseValues(tags, requestedPage);
			}
		} catch (Exception ex) {
			String errorMsg = "Error occured while fetching notes ";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (allNotes == null || allNotes.isEmpty()) {
			log.error("No notes found");
			throw new DataNotFoundException("No notes found");
		}
		List<NotesSummaryTO> notesList = allNotes.toList().stream()
				.map(e -> dtoMapper.getMapper().map(e, NotesSummaryTO.class)).collect(Collectors.toList());

		return notesList;
	}

	@Override
	public List<NoteTO> listNotesPageable(int page, int size) throws DataNotFoundException {
		// TODO user tracking in future can be added
		log.info("User XYZ trying to fetch all notes");
		Page<Note> allNotes;
		try {
			Pageable requestedPage = PageRequest.of(page, size, Sort.by("createDate").descending());
			allNotes = notesRepo.findAll(requestedPage);
		} catch (Exception ex) {
			String errorMsg = "Error occured while fetching notes ";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (allNotes == null || allNotes.isEmpty()) {
			log.error("No notes found");
			throw new DataNotFoundException("No notes found");
		}
		List<NoteTO> lstNotesTO = allNotes.toList().stream().map(e -> dtoMapper.getMapper().map(e, NoteTO.class))
				.collect(Collectors.toList());

		return lstNotesTO;
	}

	@Override
	public List<NoteTO> listNotesFiltered(List<String> tags) throws DataNotFoundException {
		// TODO user tracking in future can be added
		log.info("User XYZ trying to fetch all notes");
		Page<Note> allNotes;
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("name").in(tags));
			Pageable requestedPage = PageRequest.of(0, 10);
			log.info("input : " + tags);
			allNotes = notesRepo.findAnyOfTheseValues(tags, requestedPage);
		} catch (Exception ex) {
			String errorMsg = "Error occured while fetching notes ";
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (allNotes == null || allNotes.isEmpty()) {
			log.error("No notes found");
			throw new DataNotFoundException("No notes found");
		}
		List<NoteTO> lstNotesTO = allNotes.toList().stream().map(e -> dtoMapper.getMapper().map(e, NoteTO.class))
				.collect(Collectors.toList());

		return lstNotesTO;
	}

	@Override
	public String getNotesText(String noteID) throws DataNotFoundException {
		log.info("User XYZ trying to search notes" + noteID);
		validateInput(noteID);
		Optional<Note> optionalNote;
		try {
			optionalNote = notesRepo.findByNoteID(noteID);
		} catch (Exception ex) {
			 String errorMsg = new StringBuilder("Error occured while fetching notes")
			 .append(noteID)
			 .toString();
			log.error(errorMsg);
			throw new DataNotFoundException(errorMsg, ex);
		}
		if (!optionalNote.isPresent()) {
			throw new DataNotFoundException("Note id " + noteID + " Not found");
		}
		Note noteReturned = optionalNote.get();
		return noteReturned.getNoteText();
	}

}
