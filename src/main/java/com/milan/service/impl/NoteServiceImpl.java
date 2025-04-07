package com.milan.service.impl;

import com.milan.dto.CategoryDto;
import com.milan.dto.NotesDto;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.Notes;
import com.milan.repository.CategoryRepository;
import com.milan.repository.NoteRepository;
import com.milan.service.NoteService;
import com.milan.util.Validation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final ModelMapper mapper;

    private final CategoryRepository categoryRepository;

    private final Validation validation;

    @Override
    public Boolean saveNote(NotesDto notesDto) throws Exception {

        validation.notesValidation(notesDto); // Throws ValidationException if invalid

        //category validation check if category exists or not before saving
        checkCategoryExist(notesDto.getCategory());

        //convert dto to entity
        Notes notes = mapper.map(notesDto, Notes.class);

        Notes save = noteRepository.save(notes);

        //returns true if it's not empty otherwise false
        return !ObjectUtils.isEmpty(save);
    }

    private void checkCategoryExist(NotesDto.CategoryDto category) throws ResourceNotFoundException {
        //check if exist otherwise  throw resource exception
        categoryRepository.findById(category.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Category id is invalid"));
    }

    @Override
    public List<NotesDto> getAllNotes() {
        //convert to dto
      return  noteRepository.findAll().stream()
              .map(note -> mapper.map(note, NotesDto.class)).collect(Collectors.toList());
    }


}
