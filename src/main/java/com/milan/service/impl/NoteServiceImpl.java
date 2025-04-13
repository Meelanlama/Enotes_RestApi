package com.milan.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.milan.dto.CategoryDto;
import com.milan.dto.FavouriteNoteDto;
import com.milan.dto.NotesDto;
import com.milan.dto.NotesResponse;
import com.milan.exception.ResourceNotFoundException;
import com.milan.model.FavouriteNote;
import com.milan.model.FileDetails;
import com.milan.model.Notes;
import com.milan.repository.CategoryRepository;
import com.milan.repository.FavouriteNoteRepository;
import com.milan.repository.FileRepository;
import com.milan.repository.NoteRepository;
import com.milan.service.NoteService;
import com.milan.util.CommonUtil;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
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

    private final FavouriteNoteRepository favouriteNoteRepository;

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public Boolean saveNote(String notes, MultipartFile file) throws Exception {

        //convert string to dto using object mapper
        ObjectMapper objMapper = new ObjectMapper();
        NotesDto notesDto = objMapper.readValue(notes, NotesDto.class);

        // For new notes, explicitly set deletion flag
        notesDto.setIsDeleted(false);
        notesDto.setDeletedOn(null);

        // If ID is provided, validate the note is NOT deleted
        if(!ObjectUtils.isEmpty(notesDto.getId())){
            updateNotes(notesDto,file);
        }

        //check category exists or not
        validation.notesValidation(notesDto); // Throws ValidationException if invalid

        //category validation check if category exists or not before saving
        checkCategoryExist(notesDto.getCategory());

        //convert dto to entity
        Notes saveNote = mapper.map(notesDto, Notes.class);

        //Call method to save file details
        FileDetails fileDetails = saveFileDetails(file);

        if(!ObjectUtils.isEmpty(fileDetails)) {
            saveNote.setFileDetails(fileDetails);
        }else {
            //set file to null only if id is null. for new notes
            if(ObjectUtils.isEmpty(notesDto.getId())){
                saveNote.setFileDetails(null);
            }
        }

        Notes save = noteRepository.save(saveNote);

        //returns true if it's not empty otherwise false
        return !ObjectUtils.isEmpty(save);
    }

    //for updating notes
    private void updateNotes(NotesDto notesDto, MultipartFile file) throws ResourceNotFoundException {

        // Fetch existing note and check deletion status
        Notes existsNotes = noteRepository.findById(notesDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid note id"));

        // Check: Block updates to deleted notes
        if (existsNotes.getIsDeleted()) { // Ensure `isDeleted` is a field in the Notes entity
            throw new IllegalStateException("Cannot update note because it's in recycle bin");
        }

        // Handle file updates only if the note is active
        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
            //convert to dto
            notesDto.setFileDetails(mapper.map(existsNotes.getFileDetails(), NotesDto.FilesDto.class));
        }
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
        return noteRepository.findByIsDeletedFalse().stream()
                .map(note -> mapper.map(note, NotesDto.class))
                .collect(Collectors.toList());
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
    public NotesResponse getAllNotesByUser(Integer pageNo, Integer pageSize) {

        //get user id
        Integer userId = CommonUtil.getLoggedInUser().getId();

        Pageable pageable= PageRequest.of(pageNo,pageSize);
        //GET ONLY ACTIVE NOTES
        Page<Notes> pageNotes = noteRepository.findByCreatedByAndIsDeletedFalse(userId,pageable);

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

    @Override
    public NotesResponse getNotesByUserSearch(Integer pageNo, Integer pageSize, String keyword) {
        //get user id
        Integer userId = CommonUtil.getLoggedInUser().getId();

        Pageable pageable= PageRequest.of(pageNo,pageSize);
        //GET ONLY ACTIVE NOTES
        Page<Notes> pageNotes = noteRepository.searchNotes(keyword,userId,pageable);

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

    @Override
    public void softDeleteNotes(Integer id) throws ResourceNotFoundException {
        Notes notes = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not Found"));

        //set soft delete to true
        notes.setIsDeleted(true);
        notes.setDeletedOn(LocalDateTime.now());
        noteRepository.save(notes);
    }

    @Override
    public void restoreNotes(Integer id) throws ResourceNotFoundException {
        Notes notes = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not Found"));

        //set soft delete to true
        notes.setIsDeleted(false);
        notes.setDeletedOn(null);
        noteRepository.save(notes);
    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes() {
        //get user logged in id
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<Notes> recycleNotes = noteRepository.findByCreatedByAndIsDeletedTrue(userId);

        //convert list entity to dto entity
        return recycleNotes.stream().map(note -> mapper.map(note, NotesDto.class)).toList();
    }

    @Override
    public void hardDeleteNotes(Integer id) throws ResourceNotFoundException {
        Notes notes = noteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Note not found"));

        //delete only notes if is deleted is true(already in recycle bin)
        if (notes.getIsDeleted()) {
            noteRepository.delete(notes);
        } else {
            throw new IllegalArgumentException("Sorry! You cant delete Directly. It should be in Recycle bin");
        }
    }

    @Override
    public void emptyRecycleBin() {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<Notes> recycleNotes = noteRepository.findByCreatedByAndIsDeletedTrue(userId);
        if (!CollectionUtils.isEmpty(recycleNotes)) {
            noteRepository.deleteAll(recycleNotes);
        }else {
            throw new IllegalArgumentException("Recycle bin is empty");
        }
    }

    @Override
    public void favoriteNotes(Integer noteId) throws Exception {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        Notes notes = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Notes Not found or Id is invalid"));

        FavouriteNote favouriteNote = FavouriteNote.builder().note(notes).userId(userId).build();

        favouriteNoteRepository.save(favouriteNote);
    }

    @Override
    public void unFavoriteNotes(Integer favNoteId) throws Exception {
        FavouriteNote favNote = favouriteNoteRepository.findById(favNoteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favourite Note Not found or Id is invalid"));
        favouriteNoteRepository.delete(favNote);
    }

    @Override
    public List<FavouriteNoteDto> getUserFavoriteNotes() throws Exception {
        Integer userId = CommonUtil.getLoggedInUser().getId();
        List<FavouriteNote> favouriteNotes = favouriteNoteRepository.findByUserId(userId);
        return favouriteNotes.stream().map(fn -> mapper.map(fn, FavouriteNoteDto.class)).toList();
    }

    @Override
    public Boolean copyNotes(Integer id) throws Exception {
        Notes notes = noteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notes id invalid ! Not Found"));

//		Notes copyNote=new Notes();
//		copyNote.setTitle(notes.getTitle());

        Notes copyNote = Notes.builder().title(notes.getTitle()).description(notes.getDescription())
                .category(notes.getCategory()).isDeleted(false).fileDetails(null).build();

        // TODO : Need to check User Validation
        Notes saveCopyNote = noteRepository.save(copyNote);
        if (!ObjectUtils.isEmpty(saveCopyNote)) {
            return true;
        }
        return false;
    }

}
