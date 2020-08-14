package com.msapmp.starter.http.log.autoconfigure.filter.annotation;

import com.msapmp.starter.http.log.autoconfigure.EnableHttpLogImportSelector;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(EnableHttpLogImportSelector.class)
@Documented
public @interface EnableHttpLog {
  boolean enabled() default true;
}
