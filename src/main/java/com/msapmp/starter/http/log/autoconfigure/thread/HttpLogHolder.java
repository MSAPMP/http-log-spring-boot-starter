package com.msapmp.starter.http.log.autoconfigure.thread;

public class HttpLogHolder {
  private static final ThreadLocal<String> idHolder = new ThreadLocal<>();

  public static String getId() {
    return idHolder.get();
  }

  public static void setId(String logId) {
    idHolder.set(logId);
  }
}
