package com.milan.controller;

import com.milan.dto.FavouriteNoteDto;
import com.milan.dto.NotesDto;
import com.milan.dto.NotesResponse;
import com.milan.endpoint.NotesEndpoint;
import com.milan.model.FileDetails;
import com.milan.service.NoteService;
import com.milan.util.CommonUtil;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class NotesController implements NotesEndpoint {

    private final NoteService noteService;

    @Override
    public ResponseEntity<?> saveNotes(String notes,MultipartFile file) throws Exception {

        Boolean saveNote = noteService.saveNote(notes,file);

        if(saveNote) {
            return CommonUtil.createBuildResponseMessage("Notes saved.", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = noteService.getAllNotes();

        if(CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllNotesByUser(Integer pageNo,Integer pageSize) {

        NotesResponse notes = noteService.getAllNotesByUser(pageNo,pageSize);
//        if(ObjectUtils.isEmpty(notes)) {
//            return ResponseEntity.noContent().build();
//        }
        return CommonUtil.createBuildResponse(notes,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchNotes(String keyword, Integer pageNo,Integer pageSize) {
        NotesResponse notes = noteService.getNotesByUserSearch(pageNo, pageSize,keyword);
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> downloadFile(Integer fileId) throws Exception {

            //get the file details first
            FileDetails fileDetails = noteService.getFileDetails(fileId);

            // Retrieve the file as a byte array from the service layer
            byte[] downloadFile = noteService.downloadFile(fileDetails);

             // Create HTTP headers for the response
             HttpHeaders header = new HttpHeaders();

            // Determine the file's content type with original name before download
            String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());

            //Convert string type into media type
            header.setContentType(MediaType.parseMediaType(contentType));
            // header.setContentLength(file.length()); // Uncomment if needed
            header.setContentDispositionFormData("Attachment", fileDetails.getOriginalFileName()); // Suggests file download

            // Return the file with appropriate headers
            return ResponseEntity.ok().headers(header).body(downloadFile);
    }

    @Override
    public ResponseEntity<?> deleteNotes(Integer id) throws Exception {
        noteService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Deleted. Check in recycle bin", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> restoreNotes(Integer id) throws Exception {
        noteService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restored", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        List<NotesDto> notes = noteService.getUserRecycleBinNotes();
        if (CollectionUtils.isEmpty(notes)) {
            return CommonUtil.createBuildResponseMessage("Recycle Bin is Empty.", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> hardDeleteNotes(Integer id) throws Exception {
        noteService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Deleted Permanently", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception {
        noteService.emptyRecycleBin();
        return CommonUtil.createBuildResponseMessage("All notes deleted permanently", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> favoriteNote(Integer noteId) throws Exception {
        noteService.favoriteNotes(noteId);
        return CommonUtil.createBuildResponseMessage("Note added Favorite", HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> unFavoriteNote(Integer favNoteId) throws Exception {
        noteService.unFavoriteNotes(favNoteId);
        return CommonUtil.createBuildResponseMessage("Note Removed From Favorite", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getUserFavouriteNote() throws Exception {

        List<FavouriteNoteDto> userFavoriteNotes = noteService.getUserFavoriteNotes();
        if (CollectionUtils.isEmpty(userFavoriteNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
    }

}