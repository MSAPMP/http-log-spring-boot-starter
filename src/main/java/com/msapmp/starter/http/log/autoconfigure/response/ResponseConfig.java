package com.msapmp.starter.http.log.autoconfigure.response;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResponseConfig {
  @Bean
  public ReturnValueFactory handlerMethodReturnValueFactory() {
    return new ReturnValueFactory();
  }
}
