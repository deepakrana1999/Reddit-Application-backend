package com.programming.ranatech.springredditclone.service;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.programming.ranatech.springredditclone.exceptions.SpringRedditException;
import com.programming.ranatech.springredditclone.model.NotificationEmail;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {
	
	private final JavaMailSender mailSender;
	private final MailContentBuilder mailContentBuilder;
	
	@Async
	void sendMail(NotificationEmail notificationEmail) {
		MimeMessagePreparator messagePreparator = mimeMessage ->{
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom("ranadipu1999@gmail.com");
			messageHelper.setTo(notificationEmail.getRecipient());
			messageHelper.setSubject(notificationEmail.getSubject());
			
		
			messageHelper.setText(notificationEmail.getBody(), true);
		};
		
        try {
            mailSender.send(messagePreparator);
            log.info("Activation email sent successfully to {}", notificationEmail.getRecipient());
        } catch (Exception e) {
            log.error("Failed to send email", e);
            throw new SpringRedditException(
                    "Failed to send email to " + notificationEmail.getRecipient(), e);
        }
	}

}
