package com.milan.controller;

import com.milan.dto.NotesDto;
import com.milan.repository.NoteRepository;
import com.milan.service.NoteService;
import com.milan.util.CommonUtil;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/notes")
@RequiredArgsConstructor
public class NotesController {

    private final NoteService noteService;

    private final ModelMapper mapper;

    private final Validation validation;

    @PostMapping("/save-notes")
    public ResponseEntity<?> saveNotes(@RequestBody NotesDto notesDto) throws Exception {

        Boolean b = noteService.saveNote(notesDto);

        if(b) {
            return CommonUtil.createBuildResponseMessage("Notes saved.", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("Notes not saved.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllNotes() {
        List<NotesDto> notes = noteService.getAllNotes();

        if(CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes,HttpStatus.OK);
    }

}
