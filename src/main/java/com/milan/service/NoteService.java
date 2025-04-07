package com.milan.service;

import com.milan.dto.NotesDto;

import java.util.List;

public interface NoteService {

    Boolean saveNote(NotesDto notesDto) throws Exception;

    List<NotesDto> getAllNotes();
}
