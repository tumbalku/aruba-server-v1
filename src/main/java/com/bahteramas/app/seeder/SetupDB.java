package com.bahteramas.app.seeder;

import com.bahteramas.app.entity.*;
import com.bahteramas.app.repository.AddressRepository;
import com.bahteramas.app.repository.RoleRepository;
import com.bahteramas.app.repository.UserRepository;
import com.bahteramas.app.security.BCrypt;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Component
@AllArgsConstructor
public class SetupDB implements CommandLineRunner {

  UserRepository userRepository;
  AddressRepository addressRepository;
  RoleRepository roleRepository;

  @Override
  public void run(String... args) throws Exception {

    if (roleRepository.findAll().size() == 0){
      Address address = new Address();
      address.setName("Kendari");
      addressRepository.save(address);


      Role EMPLOYEE = new Role();
      EMPLOYEE.setName("EMPLOYEE");

      Role MANAGER = new Role();
      MANAGER.setName("MANAGER");

      Role ADMIN = new Role();
      ADMIN.setName("ADMIN");
      roleRepository.saveAll(List.of(MANAGER, EMPLOYEE, ADMIN));
//      roleRepository.saveAll(List.of(MANAGER, EMPLOYEE));

//      saveUser("admin", "admin@gmail.com",
//              "085336421912", "rahasia",
//              "admin", address,
//              EMPLOYEE, UserStatus.ACTIVE);
      saveUser("admin", "admin@gmail.com",
              "085336421912", "rahasia",
              "admin123", address,
              ADMIN, UserStatus.ACTIVE);
    }


  }

  public void saveUser(String name,
                       String email,
                       String phone,
                       String password,
                       String username,
                       Address address,
                       Role role,
                       UserStatus status){

    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setName(name);
    user.setGender(Gender.MALE);
    user.setEmail(email);
    user.setPhone(phone);
    user.setCreatedAt(LocalDateTime.now());
    user.setUpdatedAt(LocalDateTime.now());
    user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
    user.setUsername(username);
    user.setAddress(address);
    user.setRoles(Set.of(role));
    user.setStatus(status);

    userRepository.save(user);
  }

}
