package com.msapmp.starter.http.log.autoconfigure.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * JsonUtil
 */
@Slf4j
public class JsonUtil {
  public static boolean isValid(String str) {
    if (StringUtils.isBlank(str)) {
      return false;
    }

    try {
      ObjectMapper objectMapper = new ObjectMapper();
      objectMapper.readTree(str);
    } catch (Exception e) {
      log.info("str [{}] is not valid json format", str);
      return false;
    }

    return true;
  }
}
