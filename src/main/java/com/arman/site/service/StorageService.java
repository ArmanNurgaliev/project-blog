package com.arman.site.service;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void store(MultipartFile[] files, Post post);

    Stream<FileDB> loadAll();

    List<FileDB> load(Long id);


}
