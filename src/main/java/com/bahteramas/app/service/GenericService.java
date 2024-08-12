package com.bahteramas.app.service;

import com.bahteramas.app.entity.User;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

public interface GenericService<ID, REQ, RES> {

  RES create(User user, REQ request) throws MalformedURLException, FileNotFoundException;
  RES update(User user, REQ request);
  RES delete(User user, ID id);
  List<RES> findAll();
  RES find(ID id);

}
