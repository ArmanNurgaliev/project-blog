package com.arman.site.service;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import com.arman.site.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class FileStorageService {
    @Value("${upload.path}")
    private String uploadPath;
    private FileRepository fileRepository;

    @Autowired
    public FileStorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public void store(MultipartFile[] files, Post post) throws IOException {
        for (MultipartFile file: files) {
            if (files != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists())
                    uploadDir.mkdir();
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();
                String path = uploadPath + "/" + resultFilename;
                file.transferTo(new File(path));
                FileDB fileDB = new FileDB(path, resultFilename, post);

                fileRepository.save(fileDB);
            }
        }
    }

    public FileDB getFile(Long id) {
        return fileRepository.findById(id).get();
    }

    public Stream<FileDB> getAllFiles() {
        return fileRepository.findAll().stream();
    }
}
