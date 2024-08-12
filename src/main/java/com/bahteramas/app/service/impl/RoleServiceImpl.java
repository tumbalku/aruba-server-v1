package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.Role;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.RoleRequest;
import com.bahteramas.app.model.response.RoleResponse;
import com.bahteramas.app.repository.RoleRepository;
import com.bahteramas.app.service.RoleService;
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
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  @Override
  public RoleResponse create(User user, RoleRequest request) {

    // check if admin
    Helper.isAdmin(user);

    Role role = new Role();
    role.setName(request.getName());
    roleRepository.save(role);
    return Helper.roleToResponse(role);
  }

  @Override
  public RoleResponse update(User user, RoleRequest request) {

    Helper.isAdmin(user);

    Role role = getRole(request.getId());
    if(Objects.nonNull(request.getName())){
      role.setName(request.getName());
    }
    roleRepository.save(role);
    return Helper.roleToResponse(role);
  }

  @Override
  public RoleResponse delete(User user, Integer id) {

    Helper.isAdmin(user);

    Role role = getRole(id);
    roleRepository.delete(role);
    return Helper.roleToResponse(role);
  }

  @Override
  public List<RoleResponse> findAll() {
    return roleRepository.findAll().stream()
            .map(Helper::roleToResponse).collect(Collectors.toList());
  }

  @Override
  public RoleResponse find(Integer id) {
    return Helper.roleToResponse(getRole(id));
  }

  @Override
  public Role getRole(Integer id) {
    return roleRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found!"));
  }
}
