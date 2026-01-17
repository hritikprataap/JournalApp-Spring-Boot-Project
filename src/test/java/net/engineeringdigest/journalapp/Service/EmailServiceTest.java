package net.engineeringdigest.journalapp.Service;

import net.engineeringdigest.journalapp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void testSendMAIL(){
        emailService.sendEmail("23bei022@ietdavv.edu.in","testing javaMailSender","hey how are u doing");
    }
}
