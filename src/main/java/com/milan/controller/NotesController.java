package com.milan.controller;

import com.milan.dto.FavouriteNoteDto;
import com.milan.dto.NotesDto;
import com.milan.dto.NotesResponse;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NoteService noteService;

    private final ModelMapper mapper;

    private final Validation validation;

    @PostMapping("/save-notes")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception {

        Boolean saveNote = noteService.saveNote(notes,file);

        if(saveNote) {
            return CommonUtil.createBuildResponseMessage("Notes saved.", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //for admin-> all notes
    @GetMapping("/")
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = noteService.getAllNotes();

        if(CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes,HttpStatus.OK);
    }

    @GetMapping("/user-notes")
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize
    ) {

        Integer userId = 1;

        NotesResponse notes = noteService.getAllNotesByUser(userId,pageNo,pageSize);

//        if(ObjectUtils.isEmpty(notes)) {
//            return ResponseEntity.noContent().build();
//        }
        return CommonUtil.createBuildResponse(notes,HttpStatus.OK);
    }

    @GetMapping("/download-file/{fileId}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer fileId) throws Exception {

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

    //soft delete
    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {
        noteService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Deleted. Check in recycle bin", HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {
        noteService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Restored", HttpStatus.OK);
    }

    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {
        //get user logged in id
        Integer userId = 1;
        List<NotesDto> notes = noteService.getUserRecycleBinNotes(userId);
        if (CollectionUtils.isEmpty(notes)) {
            return CommonUtil.createBuildResponseMessage("Recycle Bin is Empty.", HttpStatus.OK);
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {
        noteService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Notes Deleted Permanently", HttpStatus.OK);
    }

    @DeleteMapping("/delete-recyleBin")
    public ResponseEntity<?> emptyRecyleBin() throws Exception {
        int userId=1;
        noteService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponseMessage("All notes deleted permanently", HttpStatus.OK);
    }

    @GetMapping("/fav/{noteId}")
    public ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception {
        noteService.favoriteNotes(noteId);
        return CommonUtil.createBuildResponseMessage("Note added Favorite", HttpStatus.CREATED);
    }

    @DeleteMapping("/un-fav/{favNoteId}")
    public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNoteId) throws Exception {
        noteService.unFavoriteNotes(favNoteId);
        return CommonUtil.createBuildResponseMessage("Note Removed From Favorite", HttpStatus.OK);
    }

    @GetMapping("/fav-note")
    public ResponseEntity<?> getUserfavoriteNote() throws Exception {

        List<FavouriteNoteDto> userFavoriteNotes = noteService.getUserFavoriteNotes();
        if (CollectionUtils.isEmpty(userFavoriteNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavoriteNotes, HttpStatus.OK);
    }

}