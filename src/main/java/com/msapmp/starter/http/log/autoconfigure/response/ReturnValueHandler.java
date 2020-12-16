package com.msapmp.starter.http.log.autoconfigure.response;

import com.msapmp.starter.http.log.autoconfigure.thread.HttpLogHolder;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ReturnValueHandler implements HandlerMethodReturnValueHandler {
  private final HandlerMethodReturnValueHandler handler;

  ReturnValueHandler(
      HandlerMethodReturnValueHandler handler) {
    this.handler = handler;
  }

  @Override
  public boolean supportsReturnType(MethodParameter methodParameter) {
    return handler.supportsReturnType(methodParameter) ||
        methodParameter.hasMethodAnnotation(RequestMapping.class);
  }

  @Override
  public void handleReturnValue(
      Object returnValue,
      MethodParameter returnType,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest)
      throws Exception {
    ResponseResult<Object> result = ResponseConstant.OK;

    result.setLogId(HttpLogHolder.getId());
    result.setData(returnValue);

    handler.handleReturnValue(result, returnType, mavContainer, webRequest);
  }
}
