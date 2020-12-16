package com.msapmp.starter.http.log.autoconfigure.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

@Component
public class ReturnValueFactory implements InitializingBean {
  @Autowired
  private RequestMappingHandlerAdapter adapter;

  @Override
  public void afterPropertiesSet() {
    List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
    ArrayList<HandlerMethodReturnValueHandler> handlers =
        new ArrayList<>(Objects.requireNonNull(returnValueHandlers));
    decorateHandlers(handlers);
    adapter.setReturnValueHandlers(handlers);
  }

  private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
    for (HandlerMethodReturnValueHandler handler : handlers) {
      if (handler instanceof RequestResponseBodyMethodProcessor) {
        ReturnValueHandler decorator = new ReturnValueHandler(handler);
        int index = handlers.indexOf(handler);
        handlers.set(index, decorator);
        break;
      }
    }
  }
}
