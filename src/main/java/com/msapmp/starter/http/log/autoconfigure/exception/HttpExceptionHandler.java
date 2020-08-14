package com.msapmp.starter.http.log.autoconfigure.exception;

import com.alibaba.fastjson.JSONObject;
import com.msapmp.starter.http.log.autoconfigure.response.ResponseResult;
import com.msapmp.starter.http.log.autoconfigure.thread.HttpLogHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class HttpExceptionHandler {
  @ExceptionHandler({Exception.class})
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public ResponseResult<Object> process(Exception e) {
    ResponseResult<Object> result = new ResponseResult<>();
    String logId = HttpLogHolder.getId();
    result.setLogId(logId);

    if (e instanceof BizException) {
      BizException bizException = (BizException) e;

      result.setCode(String.valueOf(bizException.getCode()));
      result.setMsg(e.getMessage());
    } else {
      result.setCode(String.valueOf(BizException.ERROR));
      result.setMsg(e.getMessage());
      log.error(JSONObject.toJSONString(result), e);
    }

    return result;
  }
}
