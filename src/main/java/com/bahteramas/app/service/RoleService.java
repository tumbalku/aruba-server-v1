package com.bahteramas.app.service;

import com.bahteramas.app.entity.Role;
import com.bahteramas.app.model.request.RoleRequest;
import com.bahteramas.app.model.response.RoleResponse;

public interface RoleService extends GenericService<Integer, RoleRequest, RoleResponse>{

  Role getRole(Integer id);
}
