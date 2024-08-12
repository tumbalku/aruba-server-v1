package com.bahteramas.app.service.impl;

import com.bahteramas.app.entity.DocType;
import com.bahteramas.app.entity.Document;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.entity.UserStatus;
import com.bahteramas.app.model.request.DocumentRequest;
import com.bahteramas.app.model.request.SearchDocRequest;
import com.bahteramas.app.model.response.DocumentResponse;
import com.bahteramas.app.repository.DocumentRepository;
import com.bahteramas.app.service.DocumentService;
import com.bahteramas.app.utils.Helper;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DocumentServiceImpl implements DocumentService {

  private final DocumentRepository documentRepository;

  private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
          "application/pdf",
          "application/msword", // .doc
          "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
          "application/vnd.ms-excel", // .xls
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xlsx
          "application/vnd.ms-powerpoint", // .ppt
          "application/vnd.openxmlformats-officedocument.presentationml.presentation" // .pptx
  );

  private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5 MB

  @Override
  @Transactional(readOnly = true)
  public Page<DocumentResponse> searchDoc(SearchDocRequest request){

    int page = request.getPage() - 1;

    Specification<Document> specification = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if(Objects.nonNull(request.getFilename())){
        predicates.add(builder.like(root.get("filename"), "%" + request.getFilename() + "%"));
      }

      if(Objects.nonNull(request.getFileType())){
        predicates.add(builder.equal(root.get("fileType"), request.getFileType()));
      }

      if(Objects.nonNull(request.getDocType())) {
        predicates.add(builder.equal(root.get("docType"), DocType.valueOf(request.getDocType())));
      }

      return query.where(predicates.toArray(new Predicate[]{})).getRestriction();
    };

    Pageable pageable = PageRequest.of(page, request.getSize());
    Page<Document> users = documentRepository.findAll(specification, pageable);
    List<DocumentResponse> userResponse = users.getContent().stream()
            .map(Helper::docToResponse)
            .collect(Collectors.toList());

    return new PageImpl<>(userResponse, pageable, users.getTotalElements());
  }
  @Override
  public List<DocumentResponse> getAll(){
    return documentRepository.findAll().stream().map(Helper::docToResponse)
            .collect(Collectors.toList());
  }

  @Override
  public List<DocumentResponse> getAllByDocType(String docType){

    return documentRepository.findByDocType(DocType.valueOf(docType)).stream()
            .map(Helper::docToResponse)
            .collect(Collectors.toList());
  }

  @Override
  public DocumentResponse saveDoc(MultipartFile file, DocumentRequest request) throws IOException {
    String contentType = file.getContentType();

    if(!ALLOWED_CONTENT_TYPES.contains(contentType)){
      throw new ResponseStatusException(
              HttpStatus.UNSUPPORTED_MEDIA_TYPE,
              "Only allows file with extension (.pdf, .doc, .docx, .xls, .xls, .ppt, .pptx)");
    }

    if(file.getSize() > MAX_FILE_SIZE){
      throw new ResponseStatusException(
              HttpStatus.PAYLOAD_TOO_LARGE,
              "Max file size should be 5MB");
    }

    Document document = new Document();

    if(Objects.nonNull(request.getFilename())){
      document.setFilename(request.getFilename());
    }else{
      document.setFilename(file.getOriginalFilename());
    }

    if(Objects.nonNull(request.getDocType())){
      document.setDocType(DocType.valueOf(request.getDocType()));
    }

    document.setFileType(file.getContentType());
    document.setUploadedAt(LocalDateTime.now());
    document.setUpdatedAt(LocalDateTime.now());


    // Compress file data
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try (GzipCompressorOutputStream gzipOutputStream = new GzipCompressorOutputStream(byteArrayOutputStream)) {
      gzipOutputStream.write(file.getBytes());
    }
    document.setData(byteArrayOutputStream.toByteArray());
    document.setFileSize(byteArrayOutputStream.size());

    documentRepository.save(document);
    return Helper.docToResponse(document);
  }

  @Override
  public DocumentResponse updateDoc(DocumentRequest request){
    Document document = getDoc(request.getId());
    if(Objects.nonNull(request.getFilename())){
      document.setFilename(request.getFilename());
    }
    if(Objects.nonNull(request.getDocType())){
      document.setDocType(DocType.valueOf(request.getDocType()));
    }
    document.setUpdatedAt(LocalDateTime.now());
    documentRepository.save(document);
    return Helper.docToResponse(document);
  }

  @Override
  public Document getDoc(Long id){
    return documentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
  }

  public DocumentResponse getDocById(Long id){
    Document document = getDoc(id);
    return Helper.docToResponse(document);
  }

  @Override
  public Document getFile(Long id) throws IOException {
    Document document = getDoc(id);

    // Decompress file data
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(document.getData());
    try (GzipCompressorInputStream gzipInputStream = new GzipCompressorInputStream(byteArrayInputStream)) {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      int len;
      while ((len = gzipInputStream.read(buffer)) != -1) {
        byteArrayOutputStream.write(buffer, 0, len);
      }
      document.setData(byteArrayOutputStream.toByteArray());
    }

    return document;
  }

  @Override
  public DocumentResponse delete(Long id) {
    Document document = getDoc(id);
    documentRepository.delete(document);
    return Helper.docToResponse(document);
  }
}
