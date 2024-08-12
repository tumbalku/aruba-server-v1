package com.bahteramas.app.controller;

import com.bahteramas.app.entity.User;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

public interface GenericController<ID, REQ, RES> {

  RES create(User user, REQ request) throws MalformedURLException, FileNotFoundException;
  RES update(User user,ID id, REQ request);
  RES delete(User user,ID id);
  RES findAll();
  RES find(ID id);
}
