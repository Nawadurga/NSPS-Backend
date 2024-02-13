package com.project.carparking.repository;

import com.project.carparking.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    void deleteByCreatedAtBefore(LocalDateTime oneMonthAgo);

    List<FileMetadata> findAllByOrderByCreatedAtDesc();
}
