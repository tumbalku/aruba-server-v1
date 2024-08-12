package com.bahteramas.app.service;

import com.bahteramas.app.entity.Document;
import com.bahteramas.app.model.request.DocumentRequest;
import com.bahteramas.app.model.request.SearchDocRequest;
import com.bahteramas.app.model.request.UserSearchRequest;
import com.bahteramas.app.model.response.DocumentResponse;
import com.bahteramas.app.model.response.UserResponse;
import com.bahteramas.app.utils.Helper;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface DocumentService {

  List<DocumentResponse> getAll();

  Page<DocumentResponse> searchDoc(SearchDocRequest request);
  List<DocumentResponse> getAllByDocType(String type);
  DocumentResponse saveDoc(MultipartFile file, DocumentRequest request) throws IOException;
  Document getFile(Long id) throws IOException;
  Document getDoc(Long id);
  DocumentResponse updateDoc(DocumentRequest request);
  DocumentResponse getDocById(Long id);

  DocumentResponse delete(Long id);
}
