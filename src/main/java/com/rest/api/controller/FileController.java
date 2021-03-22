package com.rest.api.controller;

import com.rest.api.domain.Files;
import com.rest.api.util.FileUtil;
import com.rest.api.service.FileService;
import com.rest.api.util.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import java.io.*;
import java.util.List;
import java.util.Optional;

/**
 *
 * Description : 파일 컨트롤러
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
@RequiredArgsConstructor
@RestController
public class FileController {

    private Logger logger = LoggerFactory.getLogger(BoardController.class);

    @Autowired
    private FileService fileService;

    /**
     * 파일 등록
     * @param files
     * @return ResponseMessage
     * @throws Exception
     */
    // TODO: 파일 등록
    @PostMapping("/file")
    public ResponseMessage fileUpload(@RequestParam("file") List<MultipartFile> files) throws Exception {
        ResponseMessage ms = new ResponseMessage();

        files.forEach(file -> {
            try {
                if(!file.isEmpty()) fileService.fileUpload(file);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        });

        return ms;
    }


    /**
     * 파일 조회
     * @param fileId
     * @param req
     * @return ResponseEntity<byte[]>
     * @throws Exception
     */
    // TODO: 파일 조회
    @GetMapping("/file/{fileId}")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<byte[]> downloadDocument(
            @PathVariable Long fileId, HttpServletRequest req) throws Exception {
        Optional<Files> f = fileService.getFile(fileId);
        return  FileUtil.fileDownload( f.get().getOrigFilename() , new File(f.get().getFilePath()) , req);
    }

    /**
     * 이미지 조회
     * @param fileId
     * @param req
     * @return ResponseEntity<byte[]>
     * @throws Exception
     */
    // TODO: 이미지 조회
    @GetMapping("/image/{fileId}")
    public  ResponseEntity<byte[]> getImage(@PathVariable Long fileId, HttpServletRequest req) throws Exception {
        Optional<Files> f = fileService.getFile(fileId);
        return  FileUtil.getImage(new File(f.get().getFilePath()));
    }

}
