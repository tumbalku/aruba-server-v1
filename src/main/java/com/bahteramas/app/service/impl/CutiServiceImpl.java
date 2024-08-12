package com.bahteramas.app.service.impl;

import com.bahteramas.app.model.request.CutiDecisionRequest;
import com.bahteramas.app.model.request.CutiDetailRequest;
import com.bahteramas.app.model.request.EmailRequest;
import com.bahteramas.app.repository.UserRepository;
import com.bahteramas.app.service.text.CutiPdfService;
import com.bahteramas.app.utils.Helper;
import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.entity.CutiStatus;
import com.bahteramas.app.entity.CutiType;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.CutiRequest;
import com.bahteramas.app.model.response.CutiResponse;
import com.bahteramas.app.repository.CutiRepository;
import com.bahteramas.app.service.CutiService;
import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class CutiServiceImpl implements CutiService {

  private final CutiRepository cutiRepository;
  private final EmailServiceImpl emailService;
  private final CutiPdfService pdfService;
  private final UserRepository userRepository;

  private static final String DIRECTORY = "cuti-pdfs";

  @Override
  public boolean deleteFile(String id) {
    Path filePath = Paths.get(DIRECTORY, id.concat(".pdf"));
    try {
      return Files.deleteIfExists(filePath);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Resource download(User user, String id) throws MalformedURLException, FileNotFoundException {

    Path filePath = Path.of(String.format("cuti-pdfs/%s.pdf", id));
    if(!Files.exists(filePath)) {
      throw new FileNotFoundException("file was not found on the server");
    }

    return new UrlResource(filePath.toUri());
  }

  @Override
  @Transactional
  public CutiResponse decision(User user, CutiDecisionRequest request) {
    Cuti cuti = getCuti(request.getId());
    cuti.setMessage(request.getMessage());
    cuti.setStatus(CutiStatus.valueOf(request.getStatus()));
    cuti.setConfirmDate(LocalDate.now());
    cuti.setUpdatedAt(LocalDateTime.now());
    cuti.setIsRead(true);
    cutiRepository.save(cuti);
    return Helper.cutiToResponse(cuti);
  }

  @Override
  @Transactional(readOnly = true)
  public Cuti getCuti(String id) {
    return cutiRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuti not found!"));
  }

  @Override
  @Transactional
  public CutiResponse create(User user, CutiRequest request) throws MalformedURLException, FileNotFoundException {
    Cuti cuti = new Cuti();

    cuti.setId(UUID.randomUUID().toString());
    cuti.setType(CutiType.valueOf(request.getType()));
    cuti.setDateStart(request.getDateStart());
    cuti.setCreatedAt(LocalDateTime.now());
    cuti.setUpdatedAt(LocalDateTime.now());
    cuti.setDateEnd(request.getDateEnd());
    cuti.setReason(request.getReason());
    cuti.setStatus(CutiStatus.PENDING);
    cuti.setIsRead(false);
    cuti.setUser(user);

    cutiRepository.save(cuti);

    pdfService.makeAnCutiReport(cuti.getId(), request);

    EmailRequest emailRequest = new EmailRequest();
    emailRequest.setNip(user.getId());
    emailRequest.setName(user.getName());
    emailRequest.setToken(cuti.getId());
    emailRequest.setType(cuti.getType());
    emailRequest.setTo(request.getEmail());
    emailRequest.setReason(request.getReason());

    emailService.sendEmailHTMLFormat(emailRequest);

    return Helper.cutiToResponse(cuti);
  }

  @Override
  public CutiResponse makeCuti(CutiRequest request) throws MalformedURLException, FileNotFoundException {
    User user = userRepository.findById(request.getUserNIP())
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));

    Cuti cuti = new Cuti();

    cuti.setId(UUID.randomUUID().toString());
    cuti.setType(CutiType.valueOf(request.getType()));
    cuti.setDateStart(request.getDateStart());
    cuti.setCreatedAt(LocalDateTime.now());
    cuti.setUpdatedAt(LocalDateTime.now());
    cuti.setDateEnd(request.getDateEnd());
    cuti.setReason(request.getReason());
    cuti.setStatus(CutiStatus.APPROVED);
    cuti.setConfirmDate(LocalDate.now());
    cuti.setIsRead(true);
    cuti.setUser(user);

    cutiRepository.save(cuti);

    pdfService.makeAnCutiReport(cuti.getId(), request);

    return Helper.cutiToResponse(cuti);
  }

  @Override
  @Transactional
  public CutiResponse update(User user, CutiRequest request) {
    Cuti cuti = getCuti(request.getId());

    if(Objects.nonNull(request.getType())){
      cuti.setType(CutiType.valueOf(request.getType()));
    }

    if(Objects.nonNull(request.getDateStart())){
      cuti.setDateStart(request.getDateStart());
    }

    if(Objects.nonNull(request.getDateEnd())){
      cuti.setDateEnd(request.getDateEnd());
    }

    if(Objects.nonNull(request.getReason())){
      cuti.setReason(request.getReason());
    }

    cuti.setUpdatedAt(LocalDateTime.now());
    cutiRepository.save(cuti);

    return Helper.cutiToResponse(cuti);
  }

  @Override
  @Transactional
  public CutiResponse delete(User user, String id) {
    Cuti cuti = getCuti(id);
    cutiRepository.delete(cuti);
    return Helper.cutiToResponse(cuti);
  }

  @Override
  @Scheduled(cron = "0 0 2 * * ?")
  public void deleteExpiredLeaveRecords() {
    LocalDate thresholdDate = LocalDate.now().minusDays(30);
    List<Cuti> expiredLeaves = cutiRepository.findByDateEndBefore(thresholdDate);
    cutiRepository.deleteAll(expiredLeaves);
    System.out.println("Expired leave records deleted: " + expiredLeaves.size());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CutiResponse> findAllCurrentCuties(User user) {
    return cutiRepository.findByUser(user).stream().map(Helper::cutiToResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<CutiResponse> findAll() {
    return cutiRepository.findAll().stream().map(Helper::cutiToResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public CutiResponse find(String id) {
    return Helper.cutiToResponse(getCuti(id));
  }
}
