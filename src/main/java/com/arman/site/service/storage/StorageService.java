package com.arman.site.service.storage;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {

    void store(MultipartFile[] files, String title) throws IOException;

    Stream<FileDB> loadAll();

    List<FileDB> load(Long id);


}
