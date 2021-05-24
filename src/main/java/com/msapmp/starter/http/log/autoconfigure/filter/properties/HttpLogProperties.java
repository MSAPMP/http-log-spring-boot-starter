package com.msapmp.starter.http.log.autoconfigure.filter.properties;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "msapmp.http.log")
public class HttpLogProperties {
  private Set<String> registryUris = Collections.singleton("/*");
  private Set<String> filterUris = Collections.singleton("/**");
  private Request request = new Request();
  private Response response = new Response();

  @Data
  public static class BaseHttp {
    private Body body = new Body();
  }

  @Data
  public static class Body {
    /**
     * Comma-separated list of uris for which request or response body's log should not be ignored
     */
    private Set<String> ignoreUris = new HashSet<>();
  }

  public static class Request extends BaseHttp {
  }

  public static class Response extends BaseHttp {
  }
}
