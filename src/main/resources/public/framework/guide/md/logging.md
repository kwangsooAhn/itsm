# 로깅 (Logging)
---

## 1. 적용된 라이브러리

[slf4j 1.7.26](https://www.slf4j.org)  
[log4j 2.11.2](https://logging.apache.org/log4j/2.x)

#### slf4j 소개

slf4j(Simple Logging Facade for Java)는 로깅 프레임워크가 아닌 로깅에 대한 Facade 패턴이다.  
다양한 로깅 프레임워크에 대해서 같은 API를 제공함으로서 일관된 로깅 코드를 작성하여 최소한의 수정으로 실제 로깅 프레임워크를 교체할 수 있도록 한다.

#### log4j2 소개
소개된 순서로 보면 log4j, logback에 이어 가장 최근에 나온 로깅 프레임워크로 성능이 가장 빠르다.  
logback과 slf4j사이의 연동 문제를 해결하였으며 비동기 로깅(asynchronous logging)을 제공하여, 특히 멀티 쓰레드 환경에서 높은 성능을 제공한다.

<img src ="./media/async-throughput-comparison.png" />

## 2. 설정파일

```
<?xml version="1.0" encoding="UTF-8"?>
<Configuration status="info" strict="true" name="webframework">
	<Properties>
    	<Property name="LOG_FILE_PATH">logs</Property>
    	<Property name="LOG_FILE_BACKUP_PATH">logs</Property>
  	</Properties>
  
  	<Appenders>
        <!-- Console Appender -->
    	<Console name="STDOUT">
      		<PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n"/>
    	</Console>
  		<!-- Rolling File Appender -->
    	<RollingFile name="File">
    		<FileName>${LOG_FILE_PATH}/webframework.log</FileName>
    		<FilePattern>${LOG_FILE_BACKUP_PATH}/webframework.%d{yyyyMMdd}.log.gz</FilePattern>
        	<Policies>
        		<TimeBasedTriggeringPolicy interval="1" modulate="true" />
        	</Policies>
        	<PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss.SSS} %-5level %logger{36} - %msg%n"/>
    	</RollingFile>
  	</Appenders>
  
  	<Loggers>
    	<Logger name="co.brainz" level="debug" additivity="false">
      		<AppenderRef ref="STDOUT"/>
      		<AppenderRef ref="File"/>      
    	</Logger>
    	
    	<Logger name="jdbc.sqlonly" level="debug" additivity="false">
    	    <AppenderRef ref="STDOUT"/>
      		<AppenderRef ref="File"/>   
      	</Logger>
  		
  		<Logger name="jdbc.sqltiming" level="debug" additivity="false">
  		    <AppenderRef ref="STDOUT"/>
      		<AppenderRef ref="File"/>
  		</Logger>
  		
  		<Logger name="jdbc.audit" level="OFF" />
  		<Logger name="jdbc.resultset" level="OFF" />
  		<Logger name="jdbc.resultsettable" level="OFF" />
  		
    	<Root level="info">
      		<AppenderRef ref="STDOUT"/>
      		<AppenderRef ref="File"/>
    	</Root>
  	</Loggers>
</Configuration>
```
위와 같은 log 관련 설정은 /src/main/resources/log4j2.xml 파일을 편집하여 사용한다.  
위의 기본 설정은 콘솔과 파일로그를 남기도록 되어 있고 매일 날짜 패턴으로 로그를 남기도록 설정되어 있다.  
더불어, DB 쿼리 관련해서 기본 쿼리와 실행시간 출력 정도만 설정되어 있다.   
기본 설정은 최소한 Appender와 Logger만으로 구성되어 있으니 프로젝트에 따라 정의된 형태로 설정을 수정하여 사용한다.  
설정 내용에 대한 설명은 공식 사이트(https://logging.apache.org/log4j/2.x/manual/configuration.html) 를 참고한다.  

Spring Boot에서는 application.properties에 간단한 설정만으로 꽤 많은 로그 설정을 커버할 수 있으나
원칙적으로는 log4j2를 이용해서 설정하는 것으로 한다.
        
## 3. 샘플 예제

간단한 샘플을 위해 co.brainz.framework.sample.logging.WebframeworkLogging.java가 아래와 같이 작성되어 있다.

```java
package co.brainz.framework.sample.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WebframeworkLogging {
    private static Logger logger = LoggerFactory.getLogger(WebframeworkLogging.class);

    @RequestMapping("/framework/sameple/logging")
    String LoggingSample() {
        
        String tempString = "logging Sample.";
        
        logger.error("ERROR {}", tempString);
        logger.warn("WARN {}", tempString);
        logger.info("INFO {}", tempString);
        logger.debug("DEBUG {}", tempString);
        logger.trace("TRACE {}", tempString);
        
        return "Hello!";
    }
}
```

테스트를 위해 <a href="../../../sample/logging" target="_blank">여기</a>를 클릭하면 해당 파일의 LoggingSample 메소드가 호출되면서 로그파일 남긴다. 실행하고 콘솔 로그를 확인 해본다.


소스를 간단히 살펴보면 

- 아래와 같이 2개의 패키지를 import한다. 

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```
- 아래와 같이 해당 클래스명을 이용하여 logger를 정의한다.

```java
private static Logger logger = LoggerFactory.getLogger(WebframeworkLogging.class);
```

- 필요한 경우에 따라 적절한 레벨과 형태의 로그를 아래와 같은 형태로 기록한다.

```java
String tempString = "logging Sample.";

logger.error("ERROR {}", tempString);
logger.warn("WARN {}", tempString);
logger.info("INFO {}", tempString);
logger.debug("DEBUG {}", tempString);
logger.trace("TRACE {}", tempString);
```

- 실행 시 아래와 같은 로그를 확인한다.

```console
15:33:12.636 ERROR co.brainz.framework.sample.logging.WebframeworkLogging - ERROR logging Sample.
15:33:12.636 WARN  co.brainz.framework.sample.logging.WebframeworkLogging - WARN logging Sample.
15:33:12.637 INFO  co.brainz.framework.sample.logging.WebframeworkLogging - INFO logging Sample.
15:33:12.637 DEBUG co.brainz.framework.sample.logging.WebframeworkLogging - DEBUG logging Sample.
```

## 4. 로그 레벨 가이드

로그에 대한 작성 지침은 해당 프로젝트 기준에 따르지만 아래와 같이 일반적인 케이스를 참고하여 추후 진행되는 프로젝트에 따라 적절히 수정하여 공통 가이드로 사용한다.  
 
- ERROR : 사용자의 요청을 처리하는 중에 의도되지 않은 결과가 발생하여 요청을 처리할 수 없는 문제가 발생하는 경우.
- WARN : 사용자의 요청을 처리할 수는 있지만 에러의 원인이 될 가능성이 있는 경우.
- INFO : 사용자의 요청을 처리하면서 흐름, 상태, 데이터등의 변경이 정보가 되는 경우 기록.
- DEBUG : 개발시 디버그 용도.
- TRACE : 디버그보다 더 낮은 레벨의 상세 정보. 너무 많은 로그가 찍혀서 샘플코드와 설정에는 포함하지 않았음.

## 5. 참고 사이트

[전자정부프레임 로그 환경 설정 가이드](http://www.egovframe.go.kr/wiki/doku.php?id=egovframework:rte3:fdl:%EC%84%A4%EC%A0%95_%ED%8C%8C%EC%9D%BC%EC%9D%84_%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94_%EB%B0%A9%EB%B2%95)  
[slf4j사용설명](https://gmlwjd9405.github.io/2019/01/04/logging-with-slf4j.html)  
[로깅프레임워크 관련 블로그](https://bcho.tistory.com/1312)

## 6. TO DO

- 추후 필요에 따라 아래와 같은 내용에 대한 추가 작업이 필요할 수 있다.
- Json로그 : json 형태로 로그를 작성하여 데이터 처리 및 분석에 효율적으로 활용할 수 있을까?
- 로그 외부전송 : 작성된 로그를 별도의 외부 시스템에 전송하거나 외부 서버에 로그를 남기는 방법을 추가한다.