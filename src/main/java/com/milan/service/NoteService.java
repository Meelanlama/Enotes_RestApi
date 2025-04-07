package com.milan.service;

import com.milan.dto.NotesDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NoteService {

    Boolean saveNote(String notes, MultipartFile file) throws Exception;

    List<NotesDto> getAllNotes();
}
