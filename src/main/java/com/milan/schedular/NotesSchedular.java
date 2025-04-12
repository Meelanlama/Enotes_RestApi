package com.milan.schedular;

import com.milan.dto.NotesDto;
import com.milan.model.Notes;
import com.milan.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotesSchedular {

    private final NoteRepository noteRepository;

    //check every day at 12 A.M
    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteNotesSchedule() {

        //delete in 7 days
        LocalDateTime deleteNoteDay = LocalDateTime.now().minusDays(7);
        //get notes that is soft deleted from db
        //find all notes that is deleted and deleted on previous days
       List<Notes> deletedNotes = noteRepository.findAllByIsDeletedAndDeletedOnBefore(true,deleteNoteDay);

       noteRepository.deleteAll(deletedNotes);
    }
}
