package com.rest.api.service;

import com.rest.api.domain.Files;
import com.rest.api.exception.FileStorageException;
import com.rest.api.repository.FileRepository;
import com.rest.api.support.FileStorageProperties;
import com.rest.api.util.FileUtil;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public void fileUpload(MultipartFile file) throws Exception {

        // 파일 업로드(여러개) 처리 부분
        //for (MultipartFile file : files) {
            fileRepository.save(FileUtil.fileUpload(file));
       // }
    }

    public Optional<Files> getFile(Long id){
        return fileRepository.findById(id);
    }


    public static boolean checkImageMimeType(File file) throws IOException {
        Tika tika = new Tika();
        String mimeType = tika.detect(file);
        System.out.println("mimeType :: " + mimeType);
        if(mimeType.startsWith("image")) {
            return true;
        }else {
            return false;
        }
    }

}
