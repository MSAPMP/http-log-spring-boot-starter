package com.msapmp.starter.http.log.autoconfigure.response;

public interface ResponseConstant {
  ResponseResult<Object> OK = new ResponseResult<>(null, "200", "success", null);
  ResponseResult<Object> FAIL = new ResponseResult<>(null, "500", "fail", null);

  String[] CONTENT_TYPES_HTTP_LOG =
      new String[] {"text/plain", "application/json", "multipart/form-data"};
}
