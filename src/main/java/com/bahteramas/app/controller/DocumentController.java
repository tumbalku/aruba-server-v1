package com.bahteramas.app.controller;

import com.bahteramas.app.entity.Document;
import com.bahteramas.app.entity.User;
import com.bahteramas.app.model.request.DocumentRequest;
import com.bahteramas.app.model.request.SearchDocRequest;
import com.bahteramas.app.model.response.DocumentResponse;
import com.bahteramas.app.model.response.WebResponse;
import com.bahteramas.app.model.response.WebResponseWithPaging;
import com.bahteramas.app.service.impl.DocumentServiceImpl;
import com.bahteramas.app.utils.Helper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/documents")
@AllArgsConstructor
public class DocumentController {

  private final DocumentServiceImpl documentService;

  @GetMapping("/{id}")
  public WebResponse<DocumentResponse> getById(@PathVariable("id") Long id){
    DocumentResponse response = documentService.getDocById(id);
    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Success get document")
            .build();
  }
  @GetMapping
  public WebResponseWithPaging<List<DocumentResponse>> search(
                                                       @RequestParam(name = "filename", required = false) String filename,
                                                       @RequestParam(name = "fileType", required = false) String fileType,
                                                       @RequestParam(name = "docType", required = false) String docType,
                                                       @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                                       @RequestParam(name = "size", required = false, defaultValue = "10") Integer size){

    SearchDocRequest request = new SearchDocRequest();
    request.setSize(size);
    request.setPage(page);
    request.setFileType(fileType);
    request.setDocType(docType);
    request.setFilename(filename);

    Page<DocumentResponse> responses = documentService.searchDoc(request);

    return WebResponseWithPaging.<List<DocumentResponse>>builder()
            .data(responses.getContent())
            .message("Search Success")
            .pagination(Helper.getPagingResponse(responses))
            .build();
  }

  @GetMapping("/download/{id}")
  public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) throws IOException {
    Document fileEntity = documentService.getFile(id);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileEntity.getFilename() + "\"")
            .contentType(MediaType.parseMediaType(fileEntity.getFileType()))
            .body(fileEntity.getData());
  }

  @PostMapping("/upload")
  public WebResponse<DocumentResponse> save(@RequestParam(name = "file") MultipartFile file,
                                            @RequestParam(name = "name", required = false) String name,
                                            @RequestParam(name = "docType", required = false) String docType) throws IOException {

    DocumentRequest request = new DocumentRequest();
    request.setFilename(name);
    request.setDocType(docType);

    DocumentResponse response = documentService.saveDoc(file, request);

    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Success add new file")
            .build();
  }

  @PatchMapping("/{id}")
  public WebResponse<DocumentResponse> update(@PathVariable("id") Long id, @RequestBody DocumentRequest request){

    request.setId(id);
    DocumentResponse response = documentService.updateDoc(request);

    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Success update")
            .build();
  }

  @DeleteMapping("/{id}")
  public WebResponse<DocumentResponse> delete(@PathVariable("id") Long id){
    DocumentResponse response = documentService.delete(id);
    return WebResponse.<DocumentResponse>builder()
            .data(response)
            .message("Remove has been success")
            .build();

  }

}
