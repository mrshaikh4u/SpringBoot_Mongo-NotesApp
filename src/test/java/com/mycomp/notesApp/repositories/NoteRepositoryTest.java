package com.mycomp.notesApp.repositories;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import com.mycomp.notesApp.domain.Note;

import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NoteRepositoryTest {
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private NotesRepository NoteRepository;

	
    @Disabled
    @Test
	public void whenSaveNote_thenReturnSavedNote() {
		Note Note = new Note();
		Note.setNoteTitle("testNoteTitle");
		Note persistedNote = entityManager.persist(Note);
		entityManager.flush();
		Optional<Note> retrievedNote = NoteRepository.findByNoteID(persistedNote.getNoteID());
		assertTrue(retrievedNote.get()
		                           .getNoteTitle()
		                           .equals(Note.getNoteTitle()));
	}

	@Disabled
    @Test
	public void whenDeleteNote_thenReturnNull() {
		Note Note = new Note();
		Note.setNoteTitle("testNoteTitle");
		Note persistedNote = entityManager.persist(Note);
		entityManager.flush();
		Optional<Note> retrievedNote = NoteRepository.findByNoteID(persistedNote.getNoteID());
		String savedNoteID = retrievedNote.get()
		                                      .getNoteID();

		NoteRepository.deleteByNoteID(savedNoteID);
		Optional<Note> findById = NoteRepository.findByNoteID(savedNoteID);
		assertFalse(findById.isPresent());
	}

}
