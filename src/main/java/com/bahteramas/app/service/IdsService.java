package com.bahteramas.app.service;

import com.bahteramas.app.entity.Address;
import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.entity.Role;
import com.bahteramas.app.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface IdsService {

  public User getUser(String id);

  public Cuti getCuti(String id);

  public Address getAddress(Long id);

  public Role getRole(Integer id);
}
