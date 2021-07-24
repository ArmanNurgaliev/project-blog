package com.arman.site.service;

import com.arman.site.models.FileDB;
import com.arman.site.models.Post;
import com.arman.site.repository.FileRepository;
import com.arman.site.repository.PostRepository;
import com.arman.site.service.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileStorageService implements StorageService {
    @Value("${upload.path}")
    private String uploadPath;
    private FileRepository fileRepository;
    private PostRepository postRepository;

    @Autowired
    public FileStorageService(FileRepository fileRepository, PostRepository postRepository) {
        this.fileRepository = fileRepository;
        this.postRepository = postRepository;
    }


    @Override
    public void store(MultipartFile[] files, String title) throws IOException {

          /*  if (files == null || files.length == 0) {
                throw new RuntimeException("Failed to store empty file");
            }*/
            for (MultipartFile file: files) {
                if (file != null && !file.getOriginalFilename().isEmpty()) {
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists())
                        uploadDir.mkdir();
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + "." + file.getOriginalFilename();
                    file.transferTo(new File(uploadPath + "/" + resultFilename));

                    Post postWithId = postRepository.getByTitle(title);
                    FileDB fileDB = new FileDB(resultFilename, postWithId);
                    fileRepository.save(fileDB);
                }
            }
    }

    @Override
    public Stream<FileDB> loadAll() {
        return fileRepository.findAll().stream();
    }

    @Override
    public List<FileDB> load(Long id) {
        return fileRepository.findAllByPost_id(id).stream().collect(Collectors.toList());
    }




}
