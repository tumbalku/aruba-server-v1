package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.Address;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.AddressRequest;
import com.bahteramas.app.model.response.AddressResponse;
import com.bahteramas.app.repository.AddressRepository;
import com.bahteramas.app.repository.UserRepository;
import com.bahteramas.app.service.AddressService;
import com.bahteramas.app.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

  private final AddressRepository addressRepository;
  private final UserRepository userRepository;

  @Override
  public void isAddressExist(String name) {
    boolean exist = addressRepository.existsByNameIgnoreCase(name);
    if(exist){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address already created!");
    }
  }

  @Override
  public Address makeAddress(String name) {
    Address exist = addressRepository.findByNameIgnoreCase(name).orElse(null);
    if(Objects.nonNull(exist)){
      return exist;
    }else{
      Address address = new Address();
      address.setName(name);
      addressRepository.save(address);
      return address;
    }
  }

  @Override
  public Address getById(Long id) {
    return addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found!"));
  }

  @Override
  public AddressResponse create(User user, AddressRequest request) {

    Helper.isAdmin(user);
    isAddressExist(request.getName());

    Address address = new Address();
    address.setName(request.getName());
    addressRepository.save(address);
    return Helper.addressToResponse(address);
  }

  @Override
  public AddressResponse update(User user, AddressRequest request) {

    Helper.isAdmin(user);
    Address address = getById(request.getId());
    if(Objects.nonNull(request.getName())){
      address.setName(request.getName());
    }

    addressRepository.save(address);
    return Helper.addressToResponse(address);
  }

  @Override
  public AddressResponse delete(User user, Long id) {

    Helper.isAdmin(user);
    Address address = getById(id);
    address.getUsers().forEach(candidate -> candidate.setAddress(null));

    userRepository.saveAll(address.getUsers());
    addressRepository.delete(address);
    return Helper.addressToResponse(address);
  }

  @Override
  public List<AddressResponse> findAll() {
    return addressRepository.findAll().stream()
            .map(Helper::addressToResponse).collect(Collectors.toList());
  }

  @Override
  public AddressResponse find(Long id) {
    return Helper.addressToResponse(getById(id));
  }
}
