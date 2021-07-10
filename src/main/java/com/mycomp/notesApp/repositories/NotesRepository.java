package com.mycomp.notesApp.repositories;

import java.util.List;
import java.util.Optional;

import com.mycomp.notesApp.domain.Note;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends MongoRepository<Note, Long> {

  Optional<Note> findByNoteTitle(String title);

  Optional<Note> findByNoteID(String noteID);

  void deleteByNoteID(String noteID);

  boolean existsByNoteID(String id);
  
  @Query(value = "{ 'tags' : {$all : [?0] }}")
  Page findAnyOfTheseValues(List<String> tags, Pageable pageable);

}