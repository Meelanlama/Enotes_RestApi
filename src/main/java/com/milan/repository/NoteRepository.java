package com.milan.repository;

import com.milan.model.Notes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Notes, Integer> {

    Page<Notes> findByCreatedBy(Integer createdBy, Pageable pageable);

    List<Notes> findByCreatedByAndIsDeletedTrue(Integer userId);

    Page<Notes> findByCreatedByAndIsDeletedFalse(Integer userId, Pageable pageable);

    List<Notes> findByIsDeletedFalse();

    List<Notes> findAllByIsDeletedAndDeletedOnBefore(boolean b, LocalDateTime deleteNoteDay);

    //sql query for search, search only notes created by that user and is not deleted
    @Query("select n from Notes n where (lower(n.title) like lower(concat('%', :keywordParam, '%')) "
            + "or lower(n.description) like lower(concat('%', :keywordParam, '%')) "
            + "or lower(n.category.name) like lower(concat('%', :keywordParam, '%'))) "
            + "and n.isDeleted = false and n.createdBy = :userIdParam")
    Page<Notes> searchNotes(@Param("keywordParam") String keyword, @Param("userIdParam") Integer userId, Pageable pageable);

}
