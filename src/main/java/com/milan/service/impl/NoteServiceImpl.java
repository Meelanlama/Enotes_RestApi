package com.milan.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milan.dto.CategoryDto;
import com.milan.dto.NotesDto;
import com.milan.dto.NotesResponse;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.FileDetails;
import com.milan.model.Notes;
import com.milan.repository.CategoryRepository;
import com.milan.repository.FileRepository;
import com.milan.repository.NoteRepository;
import com.milan.service.NoteService;
import com.milan.util.Validation;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;

    private final ModelMapper mapper;

    private final CategoryRepository categoryRepository;

    private final Validation validation;

    private final FileRepository fileRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public Boolean saveNote(String notes, MultipartFile file) throws Exception {

        //convert string to dto using object mapper
        ObjectMapper objMapper = new ObjectMapper();
        NotesDto notesDto = objMapper.readValue(notes, NotesDto.class);

        validation.notesValidation(notesDto); // Throws ValidationException if invalid

        //category validation check if category exists or not before saving
        checkCategoryExist(notesDto.getCategory());

        //convert dto to entity
        Notes saveNote = mapper.map(notesDto, Notes.class);

        //Call methods to save
        FileDetails fileDetails = saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)) {
            saveNote.setFileDetails(fileDetails);
        }else {
            saveNote.setFileDetails(null);
        }

        Notes save = noteRepository.save(saveNote);

        //returns true if it's not empty otherwise false
        return !ObjectUtils.isEmpty(save);
    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {

            String originalFilename = file.getOriginalFilename();
            String extension = FilenameUtils.getExtension(originalFilename);

            //Only allow these extensions
            List<String> extensionAllow = Arrays.asList("pdf", "xlsx", "jpg", "png","docx");
            if (!extensionAllow.contains(extension)) {
                throw new IllegalArgumentException("invalid file format ! Upload only .pdf , .xlsx,.jpg");
            }

            String rndString = UUID.randomUUID().toString();
            String uploadfileName = rndString + "." + extension; // sdfsafbhkljsf.pdf

            File saveFile = new File(uploadPath);
            if (!saveFile.exists()) {
                saveFile.mkdir();
            }
            // path : enotesapiservice/notes/java.pdf
            String storePath = uploadPath.concat(uploadfileName);

            // upload file
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));
            if (upload != 0) {
                FileDetails fileDtls = new FileDetails();
                fileDtls.setOriginalFileName(originalFilename);
                fileDtls.setDisplayFileName(getDisplayName(originalFilename));
                fileDtls.setUploadFileName(uploadfileName);
                fileDtls.setFileSize(file.getSize());
                fileDtls.setPath(storePath);
                return fileRepository.save(fileDtls);
            }
        }
        return null;
    }

    private String getDisplayName(String originalFilename) {
        // java_programming_tutorials.pdf
        // java_prog.pdf
        String extension = FilenameUtils.getExtension(originalFilename);
        String fileName = FilenameUtils.removeExtension(originalFilename);

        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
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

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws ResourceNotFoundException, IOException {

        //get the full file path and
        // Create an input stream to read the file
        InputStream file = new FileInputStream(fileDetails.getPath());

        // Convert the input stream to a byte array and return it
        return StreamUtils.copyToByteArray(file);
    }

    @Override
    public FileDetails getFileDetails(Integer fileId) throws ResourceNotFoundException {
        //find file from db
        return fileRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundException("File not found"));
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {

        Pageable pageable= PageRequest.of(pageNo,pageSize);
        Page<Notes> pageNotes = noteRepository.findByCreatedBy(userId,pageable);

        //convert note entity to dto
        List<NotesDto> notesDto = pageNotes.get().map(n -> mapper.map(n, NotesDto.class)).toList();

        //get all the data of notes
        return NotesResponse.builder()
                .notes(notesDto)
                .pageNo(pageNotes.getNumber())
                .pageSize(pageNotes.getSize())
                .totalElements(pageNotes.getTotalElements())
                .totalPages(pageNotes.getTotalPages())
                .isFirst(pageNotes.isFirst())
                .isLast(pageNotes.isLast())
                .build();
    }

}
