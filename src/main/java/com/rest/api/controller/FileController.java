package com.rest.api.controller;

import com.rest.api.domain.Files;
import com.rest.api.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import com.rest.api.service.FileService;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class FileController {

    private Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private FileService fileService;

    @PostMapping("/file")
    public ResponseMessage fileUpload(@RequestParam("file") List<MultipartFile> files) throws Exception {
        ResponseMessage ms = new ResponseMessage();

        files.forEach(file -> {
            try {
                fileService.fileUpload(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return ms;
    }


    @GetMapping("/file/{fileId}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long fileId, HttpServletRequest req) throws Exception {
        Optional<Files> f = fileService.getFile(fileId);
        return  FileUtil.fileDownload( f.get().getOrigFilename() , new File(f.get().getFilePath()) , req);
    }

}
