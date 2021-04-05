package com.rest.api.service;

import com.rest.api.domain.Files;
import com.rest.api.repository.FileRepository;
import com.rest.api.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public void fileUpload(MultipartFile file) throws Exception {
        fileRepository.save(FileUtil.fileUpload(file));
    }

    public Optional<Files> getFile(Long id){
        return fileRepository.findById(id);
    }

    public void deleteFile(Files f){
        fileRepository.delete(f);
    }

}
