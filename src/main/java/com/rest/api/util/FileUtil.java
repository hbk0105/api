package com.rest.api.util;

import com.rest.api.domain.Files;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.io.*;

/**
 *
 * Description : 파일 관련 클래스
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
public class FileUtil {

    @Value("${file.uploadDir}")
    private static String uploadDir;

    /**
     * 파일 업로드
     * @param file
     * @return Files
     * @throws Exception
     */
    // TODO: 파일 업로드
    public static Files fileUpload(MultipartFile file) throws Exception {
        Files f = new Files();
        try {

            String origFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = FilenameUtils.getExtension(origFilename).toLowerCase();

            // 동일한 파일 업로드 안됨
            //String fileName = new MD5Generator(origFilename).toString()  + "." + fileExtension;
            String fileName =  RandomStringUtils.randomAlphanumeric(32)  + "." + fileExtension;

            if(fileName.contains("..")){
                throw new Exception("invalid path : "+fileName );
            }

            String savePath = uploadDir + "\\files";
            /* 파일이 저장되는 폴더가 없으면 폴더를 생성합니다. */
            if(new File(savePath).exists() == false){ new File(savePath).mkdirs(); }

            /*
            // 이미지 타입 검사
            if(checkImageMimeType((File) file)){
                Tika tika = new Tika();
                f.setMimeType(tika.detect((File) file));
            }
            */

            String filePath = savePath + "\\" + fileName;
            File destinationFile =  new File(filePath);
            file.transferTo(destinationFile);
            f.setOrigFilename(origFilename);
            f.setFileName(fileName);
            f.setSize(file.getSize());
            f.setFilePath(filePath);
            f.setMimeType(new Tika().detect(destinationFile));

        }catch (Exception e){
            e.printStackTrace();
        }
        return f;
    }

    /**
     * 파일 다운로드
     * @param orgName
     * @param f
     * @param req
     * @return ResponseEntity<byte[]>
     * @throws Exception
     */
    // TODO: 파일 다운로드
    public static ResponseEntity<byte[]> fileDownload(String orgName , File f , HttpServletRequest req) throws Exception{

        InputStream inputImage = new FileInputStream(f.getAbsolutePath());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int l = inputImage.read(buffer);
        while (l >= 0) {
            outputStream.write(buffer, 0, l);
            l = inputImage.read(buffer);
        }

        HttpHeaders header = new HttpHeaders();
        String strClient = req.getHeader("User-Agent");

        String dwnFileName = new String(orgName.trim().getBytes("EUC-KR"),"8859_1");   //수정사항

        // https://m.blog.naver.com/PostView.nhn?blogId=kimgungoo&logNo=90045379130&proxyReferer=https:%2F%2Fwww.google.com%2F
        if(strClient.indexOf("MSIE 5.5")>-1) {
            header.add("Content-Type", "doesn/matter;");
            header.add("Content-Disposition", "filename=" + dwnFileName + ";");
        }
        else {
            header.add("Content-Type", "application/octet-stream;");
            header.add("Content-Disposition", "attachment; filename=" + dwnFileName + ";");
        }

        // 파일의 mediaType를 알아내기 위한 api
        //String mediaType = new Tika().detect(new File(f.getAbsolutePath()));
        header.setContentType(MediaType.valueOf(new Tika().detect(new File(f.getAbsolutePath()))));
        header.add("Content-Type","text/html; charset=EUC_KR");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return new ResponseEntity<byte[]>(outputStream.toByteArray(), header, HttpStatus.OK);
    }

    /**
     * 이미지 출력
     * @param f
     * @return ResponseEntity<byte[]>
     * @throws IOException
     */
    // TODO: 이미지 출력
    public static ResponseEntity<byte[]> getImage(File f) throws IOException {
        InputStream inputImage = new FileInputStream(f.getAbsolutePath());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int l = inputImage.read(buffer);
        while(l >= 0) {
            outputStream.write(buffer, 0, l);
            l = inputImage.read(buffer);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", String.valueOf(MediaType.valueOf(new Tika().detect(new File(f.getAbsolutePath())))));
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }




}
