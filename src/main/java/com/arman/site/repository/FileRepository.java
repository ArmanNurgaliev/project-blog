package com.arman.site.repository;

import com.arman.site.models.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileDB, Long> {
    FileDB getByPost_id(Long id);

    List<FileDB> findAllByPost_id(Long id);
}
