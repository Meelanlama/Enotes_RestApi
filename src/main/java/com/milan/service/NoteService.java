package com.milan.service;

import com.milan.dto.NotesDto;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.FileDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NoteService {

    Boolean saveNote(String notes, MultipartFile file) throws Exception;

    List<NotesDto> getAllNotes();

    byte[] downloadFile(FileDetails fileDetails) throws ResourceNotFoundException, IOException;

    FileDetails getFileDetails(Integer fileId) throws ResourceNotFoundException;
}
