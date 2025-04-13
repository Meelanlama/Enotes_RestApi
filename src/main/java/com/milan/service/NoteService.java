package com.milan.service;

import com.milan.dto.FavouriteNoteDto;
import com.milan.dto.NotesDto;
import com.milan.dto.NotesResponse;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.FileDetails;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface NoteService {

    Boolean saveNote(String notes, MultipartFile file) throws Exception;

    List<NotesDto> getAllNotes();

    byte[] downloadFile(FileDetails fileDetails) throws ResourceNotFoundException, IOException;

    FileDetails getFileDetails(Integer fileId) throws ResourceNotFoundException;

    NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize);

    NotesResponse getNotesByUserSearch(Integer pageNo, Integer pageSize,String keyword);

    void softDeleteNotes(Integer id) throws ResourceNotFoundException;

    void restoreNotes(Integer id) throws ResourceNotFoundException;

    List<NotesDto> getUserRecycleBinNotes();

    void hardDeleteNotes(Integer id) throws ResourceNotFoundException;

    void emptyRecycleBin();

    void favoriteNotes(Integer noteId) throws Exception;

    void unFavoriteNotes(Integer favNoteId) throws Exception;

    List<FavouriteNoteDto> getUserFavoriteNotes() throws Exception;

    Boolean copyNotes(Integer id) throws Exception;

}
