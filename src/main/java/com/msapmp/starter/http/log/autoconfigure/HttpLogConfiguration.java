package com.msapmp.starter.http.log.autoconfigure;

import com.msapmp.starter.http.log.autoconfigure.filter.HttpLogFilter;
import com.msapmp.starter.http.log.autoconfigure.filter.properties.HttpLogProperties;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({HttpLogProperties.class})
public class HttpLogConfiguration {
  @Autowired
  private HttpLogProperties httpLogProperties;

  @Bean
  public FilterRegistrationBean<HttpLogFilter> httpTraceLogFilter() {
    FilterRegistrationBean registrationBean = new FilterRegistrationBean();
    registrationBean.setFilter(new HttpLogFilter(httpLogProperties));

    Set<String> registryUris = httpLogProperties.getRegistryUris();
    registrationBean.addUrlPatterns(registryUris.toArray(new String[registryUris.size()]));

    registrationBean.setName("httpLogRegistry");
    return registrationBean;
  }
}