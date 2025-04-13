package com.milan.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.milan.util.Constants.*;

@RequestMapping(path = "/api/v1/notes")
public interface NotesEndpoint {

    @PostMapping("/save-notes")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception;

    //for admin-> all notes
    @GetMapping("/")
    @PreAuthorize(ROLE_ADMIN)
    ResponseEntity<?> getAllNotes();

    @GetMapping("/user-notes")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO ) Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

    @GetMapping("/search")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> searchNotes(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                         @RequestParam(name = "pageNo", defaultValue = DEFAULT_PAGE_NO) Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize);

    @GetMapping("/download-file/{fileId}")
    @PreAuthorize(ROLE_ADMIN_USER)
    ResponseEntity<?> downloadFile(@PathVariable Integer fileId) throws Exception;

    //soft delete
    @GetMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/restore/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/recycle-bin")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getUserRecycleBinNotes() throws Exception;

    @DeleteMapping("/delete/{id}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/delete-recyleBin")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> emptyUserRecycleBin() throws Exception;

    @GetMapping("/fav/{noteId}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception;

    @DeleteMapping("/un-fav/{favNoteId}")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNoteId) throws Exception;

    @GetMapping("/fav-note")
    @PreAuthorize(ROLE_USER)
    ResponseEntity<?> getUserFavouriteNote() throws Exception;
}
