package com.rest.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 *
 * Description : 메일 클래스
 *
 * Modification Information
 * 수정일			 수정자						수정내용
 * -----------	-----------------------------  -------
 * 2021. 3.  22.    MICHAEL						최초작성
 *
 */
public class MailUtil {

    // https://hhseong.tistory.com/167

    public static void signCertificationMail(String fromAddr, String fromNm, String toAddr, String toNm, Long no , JavaMailSender javaMailSender) throws MessagingException, UnsupportedEncodingException {

        String subject = "MICHAEL SITE SIGN UP MAIL";

        StringBuilder body = new StringBuilder();
        body.append("<html><body><h3>Michael Site Membership Verification Email</h3>\n" +
                "\t<h4><a href=\"http://localhost:9090/completed/"+toAddr+"/"+no+"\" target=\"_blank\">링크</a>를 클릭하여 회원인증을 완료 해주세요.</h4>");
        body.append("<br> 이 메일에 유효 시간은 10분 입니다.");
        body.append("</body></html>");

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, true, "UTF-8");

        mimeMessageHelper.setFrom(new InternetAddress(fromAddr, fromNm));
        mimeMessageHelper.setTo(new InternetAddress(toAddr, toNm));
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(body.toString(), true);
/*
        FileSystemResource fileSystemResource = new FileSystemResource(new File("C:\\Users\\sangs\\Desktop\\JJAL\\도커.txt"));
        mimeMessageHelper.addAttachment("도커.txt", fileSystemResource);

        FileSystemResource file = new FileSystemResource(new File("C:\\Users\\sangs\\Desktop\\JJAL\\joker.jpg"));
        mimeMessageHelper.addInline("joker.jpg", file);*/

        javaMailSender.send(message);

    }
}
