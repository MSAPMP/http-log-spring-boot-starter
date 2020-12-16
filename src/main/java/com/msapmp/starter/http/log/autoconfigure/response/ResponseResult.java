package com.msapmp.starter.http.log.autoconfigure.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseResult<T> implements Serializable {
  private String logId;
  private String code;
  private String msg;
  private T data;
}
