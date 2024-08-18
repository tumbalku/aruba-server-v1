package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.*;
import com.bahteramas.app.model.response.SimpleUserResponse;
import com.bahteramas.app.security.BCrypt;
import com.bahteramas.app.service.ImageService;
import com.bahteramas.app.utils.Helper;
import com.bahteramas.app.model.request.UserRequest;
import com.bahteramas.app.model.request.UserSearchRequest;
import com.bahteramas.app.model.response.UserResponse;
import com.bahteramas.app.repository.RoleRepository;
import com.bahteramas.app.repository.UserRepository;
import com.bahteramas.app.service.UserService;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final AddressServiceImpl addressService;
  private final ImageService imageService;

  @Override
  public User getUser(String id) {
    return userRepository.findById(id)
            .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!"));
  }

  @Override
  public List<SimpleUserResponse> listSimpleUserResponse() {
    return userRepository.findAll().stream().map(Helper::simpleUserToResponse).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Page<UserResponse> searchUsers(UserSearchRequest request){

    int page = request.getPage() - 1;

    Specification<User> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if(Objects.nonNull(request.getIdentity())){
        predicates.add(builder.or(
                builder.equal(root.get("id"), request.getIdentity()),
                builder.like(root.get("name"), "%" + request.getIdentity() + "%")));
      }

      if(Objects.nonNull(request.getStatus())) {
        predicates.add(builder.equal(root.get("status"), UserStatus.valueOf(request.getStatus())));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(page, request.getSize());
    Page<User> users = userRepository.findAll(specification, pageable);
    List<UserResponse> userResponse = users.getContent().stream()
            .map(Helper::userToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(userResponse, pageable, users.getTotalElements());
  }

  @Override
  @Transactional
  public UserResponse create(User user, UserRequest request) {

    Helper.isAdmin(user);

    // by default user must be an employee
    Role role = roleRepository.findByName("EMPLOYEE")
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Please create the ROLE!"));

    User candidate = new User();
    candidate.setId(request.getId());
    candidate.setName(request.getName());
    candidate.setEmail(request.getEmail());
    candidate.setStatus(UserStatus.ACTIVE);
    candidate.setPhone(request.getPhone());
    candidate.setCreatedAt(LocalDateTime.now());
    candidate.setUpdatedAt(LocalDateTime.now());
    candidate.setUsername(request.getUsername());
    candidate.setRoles(Collections.singleton(role));
    candidate.setGender(Gender.valueOf(request.getGender()));
    candidate.setPassword(BCrypt.hashpw(request.getUsername(), BCrypt.gensalt()));
    if(Objects.nonNull(request.getCs())){
      CivilServant cs = new CivilServant();
      cs.setGroup(request.getCs().getGroup());
      cs.setRank(request.getCs().getRank());
      cs.setWorkUnit(request.getCs().getWorkUnit());
      cs.setPosition(request.getCs().getPosition());
      candidate.setCivilServant(cs);
    }

    if(Objects.nonNull(request.getAddress())){
      Address address = addressService.makeAddress(request.getAddress());
      candidate.setAddress(address);
    }

    userRepository.save(candidate);
    return Helper.userToResponse(candidate);
  }

  @Override
  @Transactional
  public UserResponse update(User user, UserRequest request) {
    Helper.isAdmin(user);

    User candidate = getUser(request.getId());

    if(Objects.nonNull(request.getName())){
      candidate.setName(request.getName());
    }
    if(Objects.nonNull(request.getEmail())){
      candidate.setEmail(request.getEmail());
    }
    if(Objects.nonNull(request.getStatus())){
      candidate.setStatus(UserStatus.valueOf(request.getStatus()));
    }
    if(Objects.nonNull(request.getPhone())){
      candidate.setPhone(request.getPhone());
    }
    if(Objects.nonNull(request.getRoles()) && request.getRoles().size() != 0) {
      candidate.getRoles().clear();
      for (String role : request.getRoles()) {
        Role exsistingRole = roleRepository.findByName(role)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Role input"));
        candidate.getRoles().add(exsistingRole);
      }
    }
    if(Objects.nonNull(request.getGender())){
      if(Gender.isValidGender(request.getGender())) {
        candidate.setGender(Gender.valueOf(request.getGender()));
      }else{
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong Gender input");
      }
    }
    if(Objects.nonNull(request.getCs())){
      CivilServant cs = new CivilServant();
      cs.setGroup(request.getCs().getGroup());
      cs.setRank(request.getCs().getRank());
      cs.setWorkUnit(request.getCs().getWorkUnit());
      cs.setPosition(request.getCs().getPosition());
      candidate.setCivilServant(cs);
    }
    if(Objects.nonNull(request.getAddress())){
      Address address = addressService.makeAddress(request.getAddress());
      candidate.setAddress(address);
    }

    candidate.setUpdatedAt(LocalDateTime.now());
    userRepository.save(candidate);
    return Helper.userToResponse(candidate);
  }

  @Override
  @Transactional
  public void updaterAvatar(User user, MultipartFile file) throws IOException {

    // supaya tidak buang buang memory
    if(Objects.nonNull(user.getAvatar())){
      imageService.removePrevImage(user.getAvatar());
    }
    user.setAvatar(imageService.saveImageToDb(file));
    userRepository.save(user);
  }

  @Override
  @Transactional
  public UserResponse delete(User user, String id) {
    Helper.isAdmin(user);
    User candidate = getUser(id);
    userRepository.delete(candidate);
    return Helper.userToResponse(candidate);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponse> findAll() {
    return userRepository.findAll()
            .stream()
            .filter(user -> user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMIN")))
            .map(Helper::userToResponse).collect(Collectors.toList());
  }

  @Override
  public UserResponse find(String id) {
    return Helper.userToResponse(getUser(id));
  }
}
