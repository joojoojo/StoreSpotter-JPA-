package com.sojoo.StoreSpotter.service.mail;

import com.sojoo.StoreSpotter.common.error.ErrorCode;
import com.sojoo.StoreSpotter.common.exception.SmtpSendFailedException;
import com.sojoo.StoreSpotter.service.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final RedisService redisService;

    public MailService(JavaMailSender javaMailSender, RedisService redisService) {
        this.javaMailSender = javaMailSender;
        this.redisService = redisService;
    }


    // --------------------- 메일 인증코드 ---------------------
    // 메일 메시지 작성
    private MimeMessage createMailMessage(String email, String code) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);         // 보내는 대상
        System.out.println("createMailMessage email 대상 확인 : " + email);

        message.setSubject("StoreSpotter 인증메일 발송");
        String msg = "";
        msg += "<h1>StoreSpotter 이메일 인증번호입니다.</h1>";
        msg += "<div style='font-size:130%'>";
        msg += "인증 코드 : <strong>";
        msg += code + "</strong><div><br/>";
        msg += "</div>";
        message.setText(msg, "utf-8", "html");

        message.setFrom(new InternetAddress("techsupp@naver.com"));

        return message;
    }

    // 회원가입 인증 코드 메일
    public void sendMail(String email, String code) throws MessagingException, UnsupportedEncodingException {
        try {
            MimeMessage mailMsg = createMailMessage(email, code);
            javaMailSender.send(mailMsg);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new SmtpSendFailedException(ErrorCode.SMTP_SEND_FAILED);
        }
    }

    // 회원가입 인증 코드 메일 전송, 인증 코드 redis 저장
    public void sendCertificationMail(String email) {
        try {
            // 랜덤 인증 코드 생성
            String code = createCode();
            // email, code 순서로
            sendMail(email, code);

            // redis에 인증 코드 저장
            redisService.setValues(email, code, 3, TimeUnit.MINUTES);

        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            throw new SmtpSendFailedException(ErrorCode.SMTP_SEND_FAILED);
        }
    }

    // --------------------- 비밀번호 재발급 ---------------------
    // 비밀번호 재발급 메일 작성
    private MimeMessage createPwMessage(String email, String code) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, email);         // 보내는 대상
        System.out.println("createPwMessage email : " + email);
        System.out.println("createPwMessage code : " + code);

        message.setSubject("TECHSUPP 비밀번호 재발급");                     // 제목

        // 비밀번호 재발급
        String pwmsg = "";
        pwmsg += "<h1>StoreSpotter 임시비밀번호 발급.</h1>";
        pwmsg += "<div style='font-size:130%'>";
        pwmsg += "임시비밀번호 : <strong>";
        pwmsg += code + "</strong><div><br/>";
        pwmsg += "</div>";
        message.setText(pwmsg, "utf-8", "html");

        message.setFrom(new InternetAddress("techsupp@naver.com"));

        return message;
    }

    // 비밀번호 재발급 메일 발송
    public String sendPwMail(String email) throws Exception {
        String code = createCode();
        MimeMessage message = createPwMessage(email, code);

        try {
            javaMailSender.send(message);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalStateException();
        }
        return code;
    }


    //-------------------- 랜덤 인증 코드 생성 --------------------
    private String createCode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {           // 인증코드 8자리
            int index = rnd.nextInt(4); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
                case 3:
                    key.append((char) ((int) (rnd.nextInt(15)) + 33));
                    // 특수문자
                    break;
            }
        }
        return key.toString();
    }
}
