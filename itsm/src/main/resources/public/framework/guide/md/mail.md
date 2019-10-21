# 메일 / 단문자 연동
---

## 1. 개요

스프링 프레임워크에는 메일 발송을 위한 MailSender 인터페이스를 제공하고 있기 떄문에 따로 라이브러리를 추가하지 않아도 된다. 본 문서는 MailSender 를 상속받은 JaveMailSender 사용하여 메일 발송 기능을 적용한다.

우선 메일을 전송하기 위해서는 SMTP(Simple Mail Transfer Protocol) 서버가 필요하며, 본 문서는 무료인 Gmail SMTP 서버를 사용한다.

단문자 연동은 고객사마다 처리 방식 및 연동 대상이 다르기 때문에 본 문서는 데이터를 전달 받아서 데이터베이스에 넣는 방식으로 기능을 적용한다.

## 2. 설정 방법

### 1) Gmail 설정 변경

Gmail을 SMTP 서버로 사용하기 위하여 아래와 같이 발신용 Gmail 설정을 변경합니다.

1) 발신용 Gmail에 로그인한 후 오른쪽 상단에서 설정 > 환경 설정 > 전달 및 POP/IMAP 탭을 클릭합니다.

2) 'IMAP 액세스' 섹션에서 IMAP 사용을 선택합니다.변경사항 저장을 클릭합니다.
[https://support.google.com/mail/troubleshooter/1668960?hl=ko&rd=2#ts=1665018](https://support.google.com/mail/troubleshooter/1668960?hl=ko&rd=2#ts=1665018)


3) 또한, 내 계정 -> 로그인및 보안 -> 연결된 앱 및 사이트 -> 보안 수준이 낮은 앱 허용 : 사용 으로 변경한다.
[https://micropilot.tistory.com/category/Java%20Mail/Gmail%20Account](https://micropilot.tistory.com/category/Java%20Mail/Gmail%20Account)

### 2) build.gradle 파일에 의존성을 추가

```gradle
build.gradle 파일에 의존성을 추가
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-mail")
}
```

### 3) mail.properties 추가
관련된 설정은 co/brainz/framework/sample/alarmy/mail/mail.properties 파일에서 확인할 수 있고, 실제 프로젝트에서 적절히 수정해서 사용한다.

```properties
# Mail 연동 
spring.mail.default-encoding=UTF-8
spring.mail.protocol=smtp
spring.mail.host=smtp.gmail.com
spring.mail.username=[gmail id]]
spring.mail.password=[gmail password]
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.timeout=5000

# TLS , port 587
spring.mail.smtp.port=587
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true

# SSL, post 465
#spring.mail.properties.mail.smtp.socketFactory.port = 465
#spring.mail.properties.mail.smtp.socketFactory.class = javax.net.ssl.SSLSocketFactory
#spring.mail.properties.mail.smtp.socketFactory.fallback = false
```

SSL 및 TLS은 네트워크를 통해 작동하는 서버, 시스템 및 응용프로그램간에 인증 및 데이터 암호화를 제공하는 암호화 프로토콜을 말한다.

SSL(Secure Sockets Layer)은 보안 소켓 계층 이라는 뜻으로 인터넷을 통해 전달되는 정보 보안의 안전한 거래를 허용하기 위해 Netscape 사에서 개발한 인터넷 통신 규약 프로토콜이며, TLS(Transport Layer Security)는 SSL 3.0을 기초로 해서 IETF가 만든 프로토콜로 이는 SSL3.0을 보다 안전하게 하고 프로토콜의 스펙을 더 정확하고 안정성을 높이는 목적으로 나왔다.

SSL로 통신하는 경우에는 처음부터 암호화된 통신을 하게되나, TLS는 처음엔 암호화하지 않은 메시지를 전달하고 응답을 받아 상호간 통신이 가능함이 확인되면 암호화된 통신을 한다는 차이가 있다.

### 4) Message 객체 구현

메일, 단문자, SMS 등에 공통으로 사용되는 메세지 객체는 수신자(to), 발신자(from), 주제(subject), 내용(content) 으로 이루어진다.

#### [ java 소스 ]
```java
package co.brainz.framework.sample.alarmy;

public class Message {
    private String from;
    private String to;
    private String subject;
    private String content;
    
    public Message() {
    }

    public Message(String from, String to, String subject, String content) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", subject='" + subject + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
```

#### [ kotlin 소스 ]
```kotlin

```


### 5) MailConfiguration Class 구현

본 예제는 TLS를 사용하여 메일을 발송한다.

#### [ java 소스 ]
```java
package co.brainz.framework.sample.alarmy.mail;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
@PropertySource({ "classpath:/co/brainz/framework/sample/alarmy/mail/mail.properties" })
public class MailConfig {
    @Value("${spring.mail.default-encoding}")
    private String encoding;
    
    @Value("${spring.mail.protocol}")
    private String protocol;
    
    @Value("${spring.mail.host}")
    private String host;
    
    @Value("${spring.mail.username}")
    private String username;
    
    @Value("${spring.mail.password}")
    private String password;
    
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;
    
    @Value("${spring.mail.properties.mail.smtp.timeout}")
    private String timeout;
    
    @Value("${spring.mail.smtp.port}")
    private int port;
    
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;
    
    @Value("${spring.mail.properties.mail.smtp.starttls.required}")
    private String required;
    
    private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    private static final String MAIL_SMTP_TIMEOUT = "mail.smtp.timeout";
    private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    private static final String MAIL_SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    
    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setProtocol(protocol);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setJavaMailProperties(getMailProperties());
        mailSender.setDefaultEncoding(encoding);
        return mailSender;
    }
    
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty(MAIL_SMTP_AUTH, auth);
        properties.setProperty(MAIL_SMTP_TIMEOUT, timeout);
        properties.setProperty(MAIL_SMTP_STARTTLS_ENABLE, starttls);
        properties.setProperty(MAIL_SMTP_STARTTLS_REQUIRED, required);
        return properties;
    }
}
```

#### [ kotlin 소스 ]
```kotlin

```

### 6) thymeleaf를 이용하여 HTML 메일 템플릿 적용

thymeleaf 이용하여 메일의 템플릿을 만들기 위해 /src/main/resources/public/templates/ 위치에 mail-template.html 템플릿을 작성한다. 본 예제는 샘플을 그대로 적용하였으니 상황에 맞게 HTML 템플릿 파일을 수정하도록 한다.

```html
<!DOCTYPE html> 
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Email with Thymeleaf HTML Template</title>
</head>
<body>
    <span th:text="${message}"></span>
</body> 
</html>
```

아래와 같이 ThymeleafConfig Class를 구현한다. 자세한 사용법은 아래 링크를 참조한다.
[https://www.thymeleaf.org/doc/articles/springmail.html](https://www.thymeleaf.org/doc/articles/springmail.html)

#### [ java 소스 ]
```java
import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
public class ThymeleafConfig {
    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }
    
    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver(){
        SpringResourceTemplateResolver emailTemplateResolver = new SpringResourceTemplateResolver();
        emailTemplateResolver.setPrefix("classpath:/public/templates/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
        emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return emailTemplateResolver;
    }
}
```

#### [ kotlin 소스 ]
```kotlin

```

### 7) 메일을 발송하는 Service Class구현

발송 기능을 담당하는 공통 인터페이스를 구현하고 implements 하여 사용한다.

#### [ java 소스 ]
```java
package co.brainz.framework.sample.alarmy;

public interface Alarmy {
     /**
     * 발송
     */
    void send(Message msg);
}
```

```java
package co.brainz.framework.sample.alarmy.mail;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import co.brainz.framework.sample.alarmy.Alarmy;
import co.brainz.framework.sample.alarmy.Message;

@Service
public class Mail implements Alarmy{
    private JavaMailSender mailSender;
    private SpringTemplateEngine templateEngine;
    
    @Autowired
    public Mail(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }
    
    @Override
    public void send(Message msg) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(msg.getFrom()); //보내는 사람
            helper.setTo(msg.getTo()); //받는 사람 
            helper.setSubject(msg.getSubject()); //제목
            
            Context ctx = new Context();
            ctx.setVariable("${from}", msg.getFrom());
            ctx.setVariable("${to}", msg.getTo());
            ctx.setVariable("${message}", msg.getContent());
            
            String htmlText = templateEngine.process("mail-template", ctx);
            helper.setText(htmlText, true);
            
            mailSender.send(message);
            System.out.println("메일 발송 성공");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("메일 발송 실패");
        }
    }
}
```

#### [ kotlin 소스 ]
```kotlin

```

MIME(Multi-purpose Internet Mail Extensions)는 인터넷 메일 교환을 위한 문서 타입의 정의 할 수 있으며 SMTP 로 전송시 이메일에 텍스트 밖에 포함하지 못하는 단점을 보완하여, 메시지 안에 텍스트 이외의 데이터를 전송할 수 있는 프로토콜이다. 



### 8) 메일을 발송 테스트
#### [ java 소스 ]
```java
package co.brainz.framework.sample.alarmy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.brainz.framework.sample.alarmy.mail.Mail;

@RestController
public class AlarmyTest {   
    @Autowired
    private Mail mail;
    
    @RequestMapping("/sample/sendTest") 
    public void sendTest() {
        mail.send(new Message("tester@gmail.com", "wdj@brainz.co.kr", "Alice Mail Send1", "Mail Test."));
    }
}
```
#### [ kotlin 소스 ]
```kotlin

```

## 4. 참고 사이트

[https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#mail](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#mail)
