# Features
add dependency into pom xml, add auto configure the filter to record http log.
* @EnableHttpLog support
* custom registry and filter uris of filter
* custom uris to ignore request or response body when not neccessary to be recorded
* global ExceptionHandler to process all exceptions and log
* return value or exception throwed is wrapped into ResponseResult, format is in
 below
```
{
    "logId": "log id, based uuid",
    "code": "biz code",
    "msg": "custom msg",
    "data": "data"
}
```
* custom BizException, can be set code and message those will be returned to
 client 

# Usage
## configure in pom.xml
Usage
### 1.Add repository first
```
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
### 2.Add dependency
```
<dependency>
    <groupId>com.github.msapmp</groupId>
    <artifactId>http-log-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Config in application.yml
```
msapmp:
  http:
    log:
      # registry these uris to filter, supported patterns refer [spring](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-requestmapping-uri-templates)
      registry-uris: /*
      # uris to record log, included registry-uris
      filter-uris: /*
      request:
        body:
          # will not record these uris's request body, supporting the Ant-style pattern syntax
          ignore-uris: /ignore-uri-1,/ignore-uri-2
      response:
        body:
          # will not record these uris's response body, supporting the Ant-style pattern syntax
          ignore-uris: /ignore-uri-1,/ignore-uri-2
```

## Config in pom.xml
```
<dependency>
    <groupId>com.msapmp.starter</groupId>
    <artifactId>http-log-spring-boot-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

# Samples
## http log
```
{
  "clientIp": "xxx",
  "execTime": 119,
  "headers": {
    "accept": "text/html,application/xhtml+xml,application/xml",
    "host": "xxx"
  },
  "id": "request-id",
  "method": "GET",
  "requestBody": "",
  "requestMethod": "GET",
  "requestParameters": {},
  "responseBody": "hello",
  "responseStatus": 200,
  "server": "xxx",
  "timestamp": "xxx",
  "uri": "/hello"
}
```

## BizException
```
throw BizException.notExist("not exist");
```

## ResponseResult(return value)
```
{
    "logId": "ffffffff-ffff-ffff-ffff-ffffffffffff",
    "code": "200",
    "msg": "success",
    "data": "hi"
}
```

## sample project
HelloController in [springboot-demo](https://github.com/wellsemon/springboot-demo/tree/master/api-rest)
