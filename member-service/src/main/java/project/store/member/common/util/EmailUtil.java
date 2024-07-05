package project.store.member.common.util;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUtil {

  private final JavaMailSender emailSender;

  @Async
  public void sendEmail(String toEmail,
    String title,
    String text) {
    SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
    try {
      emailSender.send(emailForm);
    } catch (RuntimeException e) {
      log.debug("MailService.sendEmail exception occur toEmail: {}, " +
        "title: {}, text: {}", toEmail, title, text);
    }
  }

  // 발신할 이메일 데이터 세팅
  private SimpleMailMessage createEmailForm(String toEmail,
    String title,
    String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(title);
    message.setText(text);

    return message;
  }
}