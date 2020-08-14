package com.msapmp.starter.http.log.autoconfigure;

import com.msapmp.starter.http.log.autoconfigure.filter.annotation.EnableHttpLog;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

public class EnableHttpLogImportSelector
    implements ImportSelector {
  public String[] selectImports(AnnotationMetadata metadata) {
    AnnotationAttributes attributes =
        AnnotationAttributes.fromMap(
            metadata.getAnnotationAttributes(EnableHttpLog.class.getName(), false));

    boolean enabled = attributes.getBoolean("enabled");
    if (enabled) {
      return new String[] {
          "com.msapmp.starter.http.log.autoconfigure.HttpLogConfiguration",
          "com.msapmp.starter.http.log.autoconfigure.response.ResponseConfig",
          "com.msapmp.starter.http.log.autoconfigure.exception.HttpExceptionHandler"
      };
    }

    return new String[] {};
  }
}
