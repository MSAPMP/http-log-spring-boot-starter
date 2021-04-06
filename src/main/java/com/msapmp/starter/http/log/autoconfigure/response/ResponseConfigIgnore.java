package com.msapmp.starter.http.log.autoconfigure.response;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseConfigIgnore {
}
