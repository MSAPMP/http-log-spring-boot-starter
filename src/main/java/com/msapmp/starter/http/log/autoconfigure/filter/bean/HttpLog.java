package com.msapmp.starter.http.log.autoconfigure.filter.bean;

import java.util.Map;
import lombok.Data;

@Data
public class HttpLog {
  private String id;
  private String startMillisTime;
  private String serviceName;
  private String uri;
  private String clientIp;
  private String server;
  private Integer serverPort;
  private String requestMethod;
  private Map<String, Object> headers;
  private Map<String, String[]> requestParameters;
  private String requestBody;
  private String method;
  private int responseStatus;
  private String responseBody;
  private Map<String, String> exception;
  private Long execTime;
}
