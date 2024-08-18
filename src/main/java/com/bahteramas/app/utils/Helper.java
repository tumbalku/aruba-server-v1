package com.bahteramas.app.utils;

import com.bahteramas.app.entity.*;
import com.bahteramas.app.model.response.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@AllArgsConstructor
public class Helper {

  private final Validator validator;
  public static CivilServantResponse csToResponse(CivilServant cs){
    if(Objects.nonNull(cs)){
      CivilServantResponse response = new CivilServantResponse();
      response.setGroup(cs.getGroup());
      response.setRank(cs.getRank());
      response.setWorkUnit(cs.getWorkUnit());
      response.setPosition(cs.getPosition());
      return response;
    }else{
      return null;
    }
  }
  public void validate(Object request){
    Set<ConstraintViolation<Object>> constraintViolations = validator.validate(request);
    if (constraintViolations.size() != 0){
      throw new ConstraintViolationException(constraintViolations);
    }
  }

  public static SimpleUserResponse simpleUserToResponse(User user){
    SimpleUserResponse response = new SimpleUserResponse();
    response.setId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());
    response.setAvatar(user.getAvatar());

    return response;
  }
  public static UserResponse userToResponse(User user){
    UserResponse response = new UserResponse();
    response.setId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());
    response.setPhone(user.getPhone());
    response.setGender(user.getGender());
    response.setStatus(user.getStatus());
    response.setCreatedAt(user.getCreatedAt());
    response.setUpdatedAt(user.getUpdatedAt());
    response.setAvatar(user.getAvatar());
    if(Objects.nonNull(user.getAddress())){
      response.setAddress(user.getAddress().getName());
    }
    response.setCivilServant(csToResponse(user.getCivilServant()));
    if(Objects.nonNull(user.getRoles())){
      response.setRoles(user.getRoles().stream()
              .map(role -> role.getName())
              .collect(Collectors.toList()));
    }
    return response;
  }

  public static void isAdmin(User user){
    // validate if user contain 'ADMIN' role
    boolean isAdmin = user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMIN"));

    if(!isAdmin){
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This Operation is not support for you role!");
    }
  }
  public static CutiResponse cutiToResponse(Cuti cuti) {
    CutiResponse response = new CutiResponse();
    response.setStatus(cuti.getStatus());
    response.setId(cuti.getId());
    if(Objects.nonNull(cuti.getType())){
      response.setType(cuti.getType().getDescription());
    }
    response.setReason(cuti.getReason());
    response.setIsRead(cuti.getIsRead());
    response.setMessage(cuti.getMessage());
    response.setDateEnd(cuti.getDateEnd());
    response.setDateStart(cuti.getDateStart());
    response.setConfirmDate(cuti.getConfirmDate());
    response.setUser(userToResponse(cuti.getUser()));

    return response;
  }

  public static PagingResponse getPagingResponse(Page<?> page){
    int pageNumber = page.getNumber() + 1;
    return PagingResponse.builder()
            .page(pageNumber) // current page
            .totalItems(page.getContent().size()) // get total items
            .pageSize(page.getTotalPages()) // total page keseluruhan
            .size(page.getSize())
            .build();
  }
  public static AddressResponse addressToResponse(Address address){
    AddressResponse response = new AddressResponse();
    response.setId(address.getId());
    response.setName(address.getName());

    return response;
  }

  public static RoleResponse roleToResponse(Role role){
    RoleResponse response = new RoleResponse();
    response.setId(role.getId());
    response.setName(role.getName());

    return response;
  }

  public static DocumentResponse docToResponse(Document document){
    DocumentResponse response = new DocumentResponse();
    response.setId(document.getId());
    response.setDocType(document.getDocType());
    response.setFilename(document.getFilename());
    response.setFileSize(document.getFileSize());
    response.setFileType(document.getFileType());
    response.setUpdatedAt(document.getUpdatedAt());
    response.setUploadedAt(document.getUploadedAt());

    return response;
  }

  public static String capitalizeWords(String input) {
    // Memecah string menjadi kata-kata
    String[] words = input.split(" ");

    // Mengubah huruf pertama dari setiap kata menjadi kapital
    StringJoiner capitalizedWords = new StringJoiner(" ");
    for (String word : words) {
      // Mengambil huruf pertama dan mengubahnya menjadi kapital
      String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
      // Menambahkan kata yang telah dikapitalisasi ke StringJoiner
      capitalizedWords.add(capitalizedWord);
    }

    // Mengembalikan hasil sebagai string
    return capitalizedWords.toString();
  }

  public static byte[] compressImage(byte[] data) {
    Deflater deflater = new Deflater();

    deflater.setLevel(Deflater.BEST_COMPRESSION);
    deflater.setInput(data);
    deflater.finish();

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] temp = new byte[4 * 1024];

    while (!deflater.finished()) {
      int size = deflater.deflate(temp);
      outputStream.write(temp, 0, size);
    }

    try {
      outputStream.close();
    } catch (Exception ignore) {
    }

    return outputStream.toByteArray();

  }


  public static byte[] decompressImage(byte[] data) {
    Inflater inflater = new Inflater();
    inflater.setInput(data);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] temp = new byte[4 * 1024];

    try {
      while (!inflater.finished()) {
        int count = inflater.inflate(temp);
        outputStream.write(temp, 0, count);
      }
      outputStream.close();
    } catch (Exception ignore) {
    }
    return outputStream.toByteArray();
  }

  public static String nameConversion(MultipartFile file){
    LocalDate date = LocalDate.now();
    LocalTime time = LocalTime.now();

    String tanggal = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    String jam = time.format(DateTimeFormatter.ofPattern("HH-mm"));

    return tanggal + "-" + jam + "-" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll("\\s","-");
  }

}
