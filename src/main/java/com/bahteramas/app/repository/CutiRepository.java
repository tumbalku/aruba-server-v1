package com.bahteramas.app.repository;

import com.bahteramas.app.entity.Cuti;
import com.bahteramas.app.entity.CutiStatus;
import com.bahteramas.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CutiRepository extends JpaRepository<Cuti, String>, JpaSpecificationExecutor<Cuti>  {

  List<Cuti> findByDateEndBefore(LocalDate date);
  List<Cuti> findByStatus(CutiStatus status);

  List<Cuti> findByUser(User user);
}
