package org.com.mplayer.mail.application.gateway.message;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.com.mplayer.users.SendSimpleMailUserEvent;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class MailMessageController {
    private final JavaMailSender mailSender;

    @ApplicationModuleListener
    public void execute(SendSimpleMailUserEvent event) {
        log.info("MailMessageController - New event received: SendSimpleMailUserEvent");

        sendSimpleMail(event.to(), event.subject(), event.content());
    }

    private void sendSimpleMail(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            mimeMessage.setSubject(subject);

            helper.setTo(to);
            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
