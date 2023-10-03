package com.kpmg.cacm.api.util;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.kpmg.cacm.api.util.email.template.EmailTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;

@Component
@Slf4j
public class Email {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${ktms.mail.from.email}")
    private String fromEmail;

    @Value("${ktms.mail.from.name}")
    private String fromName;

    public final boolean send(final String subject, final String[] to, final EmailTemplate emailTemplate){
        try {
            final MimeMessage mail = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(new InternetAddress(this.fromEmail, this.fromName));
            helper.setTo(to);
            helper.setSubject(subject);
            final String emailBody = this.templateEngine.process(
                    emailTemplate.getTemplateName(),
                    emailTemplate.getContext());
            helper.setText(emailBody, true);
            this.javaMailSender.send(mail);
            return true;
        } catch (final Exception exception) {
            Email.log.error(exception.getLocalizedMessage(), exception);
            return false;
        }
    }

}
