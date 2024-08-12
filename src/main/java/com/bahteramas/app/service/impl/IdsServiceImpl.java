package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.Address;
import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.entity.Role;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.repository.AddressRepository;
import com.bahteramas.app.repository.CutiRepository;
import com.bahteramas.app.repository.RoleRepository;
import com.bahteramas.app.repository.UserRepository;

import com.bahteramas.app.service.IdsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class IdsServiceImpl implements IdsService {

  private final UserRepository userRepository;
  private final CutiRepository cutiRepository;
  private final AddressRepository addressRepository;
  private final RoleRepository roleRepository;

  @Override
  public User getUser(String id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
  }

  @Override
  public Cuti getCuti(String id) {
    return cutiRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cuti not found!"));
  }

  @Override
  public Address getAddress(Long id) {
    return addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found!"));
  }

  @Override
  public Role getRole(Integer id) {
    return roleRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));
  }
}
