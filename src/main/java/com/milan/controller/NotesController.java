package com.milan.controller;

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

        Boolean b = noteService.saveNote(notes,file);

        if(b) {
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
}