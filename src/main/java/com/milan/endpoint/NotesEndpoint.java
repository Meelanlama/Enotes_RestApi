package com.milan.endpoint;

import com.milan.dto.NotesDto;
import com.milan.dto.NotesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jdk.jfr.ContentType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.milan.util.Constants.*;

@RequestMapping(path = "/api/v1/notes")
@Tag(name = "CATEGORY APIs",description = "API for managing all notes")
public interface NotesEndpoint {

    @Operation(summary = "Save Notes", tags = { "Notes", "User" }, description = "User Save Notes")
    @PostMapping(value = "/save-notes",consumes = "multipart/form-date")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<?> saveNotes(@RequestParam @Parameter(description = "IN JSON STRING NOTES",required = true,
                                        content = @Content(schema = @Schema(implementation = NotesRequest.class))) String notes,
                                @RequestParam(required = false) MultipartFile file) throws Exception;


    @Operation(summary = "Get All Notes", tags = { "Notes" }, description = "Get All Notes by Admin")
    //for admin-> all notes
    @GetMapping("/")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> getAllNotes();

    @Operation(summary = "Get All Notes of their own notes only", tags = { "Notes","User" }, description = "Get All Notes by Use")
    @GetMapping("/user-notes")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO ) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);


    @Operation(summary = "Search Notes", tags = { "Notes", "User" }, description = "User Search Notes their own notes,Admin searches all notes")
    @GetMapping("/search")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> searchNotes(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                         @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

    @Operation(summary = "Download the uploaded file of note", tags = { "Notes", "User" }, description = "Download file ")
    @GetMapping("/download-file/{fileId}")
    @PreAuthorize(ROLE_ADMIN_USER)
    ResponseEntity<?> downloadFile(@PathVariable Integer fileId) throws Exception;

    @Operation(summary = "Delete Notes by User", tags = { "Notes", "User" }, description = "Delete their own notes by user")
    //soft delete
    @GetMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "Restore their deleted notes by User", tags = { "Notes", "User" }, description = "Restore their own notes by user")
    @GetMapping("/restore/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "GET deleted Notes by User in recycle bin", tags = { "Notes", "User" }, description = "GET their own deleted notes")
    @GetMapping("/recycle-bin")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getUserRecycleBinNotes() throws Exception;

    @Operation(summary = "DELETE NOTES PERMANENTLY", tags = { "Notes", "User" }, description = "DELETE NOTES FROM DATABASE")
    @DeleteMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @Operation(summary = "DELETE ALL NOTES AT SAME TIME FROM RECYCLE BIN", tags = { "Notes", "User" }, description = "DELETE NOTES FROM DATABASE")
    @DeleteMapping("/delete-recycleBin")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> emptyUserRecycleBin() throws Exception;

    @Operation(summary = "MAKE FAVOURITE NOTES", tags = { "Notes", "User" }, description = "FAV NOTES BY USER")
    @GetMapping("/fav/{noteId}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception;

    @Operation(summary = "UN FAVOURITE NOTES USER NOTES", tags = { "Notes", "User" }, description = "UN FAV NOTES BY USER")
    @DeleteMapping("/un-fav/{favNoteId}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNoteId) throws Exception;

    @Operation(summary = "GET ALL THEIR OWN FAVOURITE NOTES", tags = { "Notes", "User" }, description = "GET FAV NOTES BY USER")
    @GetMapping("/fav-note")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getUserFavouriteNote() throws Exception;

    @Operation(summary = "Copy Notes", tags = { "Notes", "User" }, description = "Copy Notes")
    @GetMapping("/copy/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> copyNotes(@PathVariable Integer id) throws Exception;

}
