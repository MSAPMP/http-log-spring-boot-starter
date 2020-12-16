package com.msapmp.starter.http.log.autoconfigure.filter;

import com.alibaba.fastjson.JSONObject;
import com.msapmp.starter.http.log.autoconfigure.filter.bean.HttpLog;
import com.msapmp.starter.http.log.autoconfigure.filter.properties.HttpLogProperties;
import com.msapmp.starter.http.log.autoconfigure.thread.HttpLogHolder;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

@Slf4j
@NoArgsConstructor
@RequiredArgsConstructor
public class HttpLogFilter extends OncePerRequestFilter implements Ordered {
  private final PathMatcher pathMatcher = new AntPathMatcher();
  @Value("${spring.application.name}")
  private String serviceName;
  @Value("${server.port}")
  private Integer serverPort;
  @NonNull
  private HttpLogProperties httpLogProperties;

  private static boolean isValid(String str) {
    return null == str || "".equals(str.trim());
  }

  @Override
  public int getOrder() {
    return Ordered.LOWEST_PRECEDENCE - 10;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain)
      throws ServletException, IOException {
    if (!isRequestValid(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    String requestURI = request.getRequestURI();
    Set<String> filterUris = httpLogProperties.getFilterUris();

    boolean isFiltered = false;
    for (String filterUri : filterUris) {
      if (pathMatcher.match(filterUri, requestURI)) {
        isFiltered = true;
        break;
      }
    }

    if (!isFiltered) {
      filterChain.doFilter(request, response);
      return;
    }

    String id = UUID.randomUUID().toString();
    HttpLogHolder.setId(id);
    request.setAttribute("logId", id);

    if (!(request instanceof ContentCachingRequestWrapper)) {
      request = new ContentCachingRequestWrapper(request);
    }
    if (!(response instanceof ContentCachingResponseWrapper)) {
      response = new ContentCachingResponseWrapper(response);
    }

    long startTime = System.currentTimeMillis();
    try {
      filterChain.doFilter(request, response);
    } finally {
      HttpLog httpLog = new HttpLog();
      httpLog.setId(id);
      httpLog.setStartMillisTime(String.valueOf(System.currentTimeMillis()));
      httpLog.setServiceName(serviceName);
      httpLog.setUri(requestURI);

      httpLog.setClientIp(getIpAddr(request));
      httpLog.setServer(InetAddress.getLocalHost().getHostAddress());
      httpLog.setServerPort(serverPort);
      httpLog.setRequestMethod(request.getMethod());

      Enumeration<String> headerNames = request.getHeaderNames();
      Map<String, Object> headers = new HashMap<>();
      while (headerNames.hasMoreElements()) {
        String headerName = headerNames.nextElement();
        String headerValue = request.getHeader(headerName);

        headers.put(headerName, headerValue);
      }
      httpLog.setHeaders(headers);

      httpLog.setRequestParameters(request.getParameterMap());

      boolean ignoreRequestBody = false;
      if (null != httpLogProperties
          && !CollectionUtils
          .isEmpty(httpLogProperties.getRequest().getBody().getIgnoreUris())) {
        Set<String> ignoreUris = httpLogProperties.getRequest().getBody().getIgnoreUris();
        for (String ignoreUri : ignoreUris) {
          if (pathMatcher.match(ignoreUri, requestURI)) {
            ignoreRequestBody = true;
            break;
          }
        }
      }
      if (ignoreRequestBody) {
        httpLog.setRequestBody(null);
      } else {
        httpLog.setRequestBody(getRequestBody(request));
      }

      httpLog.setMethod(request.getMethod());
      httpLog.setResponseStatus(response.getStatus());

      boolean ignoreResponseBody = false;
      if (null != httpLogProperties
          && !CollectionUtils
          .isEmpty(httpLogProperties.getResponse().getBody().getIgnoreUris())) {
        Set<String> ignoreUris = httpLogProperties.getResponse().getBody().getIgnoreUris();

        for (String ignoreUri : ignoreUris) {
          if (pathMatcher.match(ignoreUri, requestURI)) {
            ignoreResponseBody = true;
            break;
          }
        }
      }
      if (ignoreResponseBody) {
        httpLog.setResponseBody(null);
      } else {
        httpLog.setResponseBody(getResponseBody(response));
      }

      httpLog.setExecTime(System.currentTimeMillis() - startTime);

      log.info(JSONObject.toJSONString(httpLog));

      updateResponse(response);
    }
  }

  private boolean isRequestValid(HttpServletRequest request) {
    try {
      new URI(request.getRequestURL().toString());
      return true;
    } catch (URISyntaxException ex) {
      return false;
    }
  }

  private String getRequestBody(HttpServletRequest request) {
    String requestBody = null;
    ContentCachingRequestWrapper wrapper =
        WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {
      try {
        requestBody =
            IOUtils
                .toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
      } catch (IOException e) {
        log.error("getRequestBody error", e);
      }
    }
    return requestBody;
  }

  private String getResponseBody(HttpServletResponse response) {
    String responseBody = null;
    ContentCachingResponseWrapper wrapper =
        WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (wrapper != null) {
      try {
        responseBody =
            IOUtils
                .toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
      } catch (IOException e) {
        log.error("getResponseBody error", e);
      }
    }
    return responseBody;
  }

  private void updateResponse(HttpServletResponse response) throws IOException {
    ContentCachingResponseWrapper responseWrapper =
        WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    Objects.requireNonNull(responseWrapper).copyBodyToResponse();
  }

  private String getIpAddr(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");

    if (isValid(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (isValid(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (isValid(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }
}
