package com.arman.site.repository;

import com.arman.site.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post getByTitle(String title);

    @Query(value = "select p from post p where p.title like %?1%", nativeQuery = true)
    Iterable<Post> findAllByTitle_name(String filter);
}
