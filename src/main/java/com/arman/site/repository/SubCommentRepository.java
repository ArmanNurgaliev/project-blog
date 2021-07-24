package com.arman.site.repository;

import com.arman.site.models.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    List<SubComment> findAllByParent_id(Long id);
}
