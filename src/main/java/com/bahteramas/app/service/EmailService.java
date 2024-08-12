package com.bahteramas.app.service;

import com.bahteramas.app.model.request.EmailRequest;

public interface EmailService {
  void sendEmailHTMLFormat(EmailRequest request);
}
