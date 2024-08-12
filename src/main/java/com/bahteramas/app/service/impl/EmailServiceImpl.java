package com.bahteramas.app.service.impl;

import com.bahteramas.app.model.request.EmailRequest;
import com.bahteramas.app.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

//  private static final String host = "https://my-lppaik.netlify.app";
  private static final String host = "http://localhost:5173";
  public static final String UTF_8_ENCODING = "UTF-8";
  private final TemplateEngine templateEngine;
  private final JavaMailSender sender;

  public static String getVerification(String host, String token) {
    return host + "/letters/cuti/decision/" + token;
  }

  @Override
  @Async
  public void sendEmailHTMLFormat(EmailRequest request) {
    try {
      Context context = new Context();
      context.setVariable("name", request.getName());
      context.setVariable("nip", request.getNip());
      context.setVariable("reason", request.getReason());
      context.setVariable("url", getVerification(host, request.getToken()));
      context.setVariable("type", request.getType().getDescription());

      String text = templateEngine.process("email", context);

      MimeMessage message = sender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true, UTF_8_ENCODING);

      helper.setPriority(1);
      helper.setSubject(request.getName() + " Mengajukan Cuti: " + request.getType().getDescription());
      helper.setTo(request.getTo());
      helper.setText(text, true);

      sender.send(message);
    } catch (Exception e) {
      System.err.println(e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }
}
