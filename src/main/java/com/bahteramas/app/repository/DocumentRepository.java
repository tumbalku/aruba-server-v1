package com.bahteramas.app.repository;

import com.bahteramas.app.entity.DocType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bahteramas.app.entity.Document;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>, JpaSpecificationExecutor<Document> {
  List<Document> findByDocType(DocType docType);
}
