package com.bahteramas.app.service;

import com.bahteramas.app.entity.Address;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.AddressRequest;
import com.bahteramas.app.model.response.AddressResponse;


public interface AddressService extends GenericService<Long, AddressRequest, AddressResponse> {
  Address getById(Long name);

  Address makeAddress(String name);

  void isAddressExist(String name);
}
