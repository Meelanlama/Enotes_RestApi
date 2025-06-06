package com.milan.repository;

import com.milan.model.FileDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.File;

@Repository
public interface FileRepository extends JpaRepository<FileDetails, Integer> {
}
