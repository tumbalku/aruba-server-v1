package com.bahteramas.app.service;

import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.CutiDecisionRequest;
import com.bahteramas.app.model.request.CutiDetailRequest;
import com.bahteramas.app.model.request.CutiRequest;
import com.bahteramas.app.model.response.CutiResponse;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

public interface CutiService extends GenericService<String, CutiRequest, CutiResponse>{

  Cuti getCuti(String id);
  List<CutiResponse> findAllCurrentCuties(User user);
  CutiResponse decision(User user, CutiDecisionRequest request);
  void deleteExpiredLeaveRecords();

  Resource download(User user, String id) throws MalformedURLException, FileNotFoundException;

  boolean deleteFile(String fileName);

  CutiResponse makeCuti(CutiRequest request) throws MalformedURLException, FileNotFoundException;
}
