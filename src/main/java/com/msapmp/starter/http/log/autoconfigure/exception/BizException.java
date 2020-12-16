package com.msapmp.starter.http.log.autoconfigure.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BizException extends Exception {
  public static final int ERROR = 500;
  private static final int INVALID_PARAM = 400;
  private static final int NOT_EXIST = 404;
  private int code;

  public BizException(int code, String message) {
    super(message);
    this.code = code;
  }

  public static BizException notExist(String message) {
    return new BizException(NOT_EXIST, message);
  }

  public static BizException invalidParam(String message) {
    return new BizException(INVALID_PARAM, message);
  }

  public static BizException error(String message) {
    return new BizException(ERROR, message);
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
