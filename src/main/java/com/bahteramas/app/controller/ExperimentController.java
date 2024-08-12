package com.bahteramas.app.controller;

import com.bahteramas.app.model.request.EmailRequest;
import com.bahteramas.app.service.impl.EmailServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/experiment")
@AllArgsConstructor
public class ExperimentController {

  private final EmailServiceImpl emailService;

  @PostMapping
  public String sent(){
    EmailRequest request = new EmailRequest();
//    request.setToken("simple-token");
//    request.setTutorName("simple-tutor-name");
//    request.setUserId("12345-simple-user-id");
//    request.setUserName("simple-user-name");
    emailService.sendEmailHTMLFormat(request);

    return "Email has been sent";
  }
}
