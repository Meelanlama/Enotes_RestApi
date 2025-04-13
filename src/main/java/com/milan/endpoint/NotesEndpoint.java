package com.milan.endpoint;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(path = "/api/v1/notes")
public interface NotesEndpoint {

    @PostMapping("/save-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file) throws Exception;

    //for admin-> all notes
    @GetMapping("/")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNotes();

    @GetMapping("/user-notes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllNotesByUser(
            @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> searchNotes(@RequestParam(name = "keyword",defaultValue = "") String keyword,
                                         @RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize);

    @GetMapping("/download-file/{fileId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> downloadFile(@PathVariable Integer fileId) throws Exception;

    //soft delete
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/restore/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception;

    @GetMapping("/recycle-bin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception;

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception;

    @DeleteMapping("/delete-recyleBin")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> emptyUserRecycleBin() throws Exception;

    @GetMapping("/fav/{noteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> favoriteNote(@PathVariable Integer noteId) throws Exception;

    @DeleteMapping("/un-fav/{favNoteId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unFavoriteNote(@PathVariable Integer favNoteId) throws Exception;

    @GetMapping("/fav-note")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserFavouriteNote() throws Exception;
}
