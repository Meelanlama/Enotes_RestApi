package com.milan.repository;

import com.milan.model.FavouriteNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteNoteRepository extends JpaRepository<FavouriteNote, Integer> {

    List<FavouriteNote> findByUserId(int userId);

}
