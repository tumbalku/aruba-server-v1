package com.bahteramas.app.model.response;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WebResponseWithPaging<T> {

  private T data;

  private String message;

  private PagingResponse pagination;
}
