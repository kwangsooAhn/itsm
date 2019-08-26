# Database
---

## 1. 개요

BWF(Brainz Web Framework)는 기본적으로 PostgreSQL 11.4 버전을 DB로 사용한다.  
기본적인 DB접속과 관련된 설정, JPA를 활용한 샘플을 확인하고, 심도 깊은 학습을 통해서 활용할 수 있도록 한다.
PostgreSQL, JPA와 관련된 내용은 다루지 않는다.

## 2. 관련 설정

기본적인 DB접속 정보와 JPA와 관련된 설정은 /src/main/resource/application.properties 파일에서 확인할 수 있고, 실제 프로젝트에서 적절히 수정해서 사용한다.


```
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.platform=postgres
spring.datasource.url=jdbc:postgresql://35.200.42.144:5432/alice
spring.datasource.username=itsm
spring.datasource.password=itsm123

spring.jpa.database=POSTGRESQL
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation=true
```

특히 JPA설정과 관련하여 **generate-ddl, hibernate.ddl-auto 설정등은 샘플 확인을 위해서 User라는 테이블을 임의로 생성하고 삭제하기 때문에 실제 어플리케이션 개발에서는 꼭 삭제** 해야 하며 전반적으로 검토 후에 사용하도록 한다.

## 3. JPA

JPA는 여기서 간단하게 소개만 하고 아래 샘플과 참고 사이트를 통해서 익히도록 한다.

JPA는 Java Persistence API의 약자로 ORM(Object Relational Mapping)을 위한 자바의 표준 기술이며,  
아주 예전에 EJB에서 ORM, 그것과 개념적으로 동일하게 생각하면 이해가 빠를 것 같다.   
BWF에서는 JPA를 손쉽게 쓸 수 있도록 하는 Spring Data JPA를 사용한다.

아주 극단적으로 간단히 말해서 DB 테이블에 따라 객체를 만들고 컬럼들을 매핑시켜서 쿼리를 대신하는 몇가지 메소드를 사용하게 하는 기술이다.  
RDB 벤더의 변경에 구애받지 않고 개발이 가능해서 이후 DB 변경에 유연하게 대처할 수 있지 않을까 기대(?)를 하지만 어플리케이션에 사용되는 복잡한 모든 쿼리까지 적용할 수는 없을거란 예상도 되고  
몇몇은 Dynamic SQL을 사용할 수도 있다.

좀 많은 학습을 요구하는 측면이 있을 수 있고 오히려 성능에 독이 되는 경우가 있을 수 있지만 비즈니스 로직에 집중하는 장점을 기대해본다. 

[JPA 강좌 시리즈](https://engkimbs.tistory.com/811?category=772527)  
[JPA 간단요약](https://doublesprogramming.tistory.com/260)

## 4. 샘플 설명

DB와 JPA와 관련된 샘플은 /com/brainz/framework/sample/db 패키지를 참고하면 된다.
설정에서 보듯이 프로그램이 시작되면 테이블이 생성되고 종료되면서 drop된다는 점을 감안하자.

기본적인 동작은 Restful 방식으로 URL에 따라 아래와 같다.
- /sample/db/init : 테이블에 일괄 데이터 입력. 테스트용으로 총 9개의 데이터가 입력된다. <a href="../../../sample/db/init" target="_blank">데이터일괄생성</a>
- /sample/db/insert : 1개의 데이터를 입력. POST 방식으로 JSON형태로 전달되어야 한다. 테스트를 위해서는 [포트스맨](https://www.getpostman.com/)같은 툴이 필요하다.
- /sample/db/findall : 전체 데이터를 반환한다. <a href="../../../sample/db/findall" target="_blank">전체데이터조회</a>
- /sample/db/search/{id} : 순번을 넣어서 조회한다. 기본 데이터에는 1~9가 들어가 있다. <a href="../../../sample/db/search/5" target="_blank">5번 데이터조회</a>
- /sample/db/searchByUserId/{userId} : 사용자 아이디로 조회한다. <a href="../../../sample/db/searchByUserId/hcjung" target="_blank">hcjung 아이디로조회하기</a>

### 소스 확인

#### 1) Repository

User에 대한 Repository를 spring의 CurdRepository 인터페이스를 받아서 만든다.

```java
package com.brainz.framework.sample.db.repository;

import java.util.List;

import com.brainz.framework.sample.db.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUserId(String userId);
    List<User> findAll();
}
```

#### 2) Model

DB의 객체(테이블)에 맞는 Model을 구성한다.  
소스에는 테이블 이름이 userInfo로 Camel표기법으로 되어 있고 이는 자동으로 Snake표기법으로 변경되어 적용된다. 결론적으로 실제 테이블은 user_info로 생성되는데 이런 내용은 컬럼에도 적용된다.  
어플리케이션 개발 당시 해당 프로젝트의 표기법이 상이하다면 표기법 자동변환에 대한 전반적인 내용은 네이밍 전략에 대한 스프링 설정에 대한 추가 학습이 필요하다.  

표기법 변환을 피하려면 다 소문자로 적으면 되긴 한다.

```java
package com.brainz.framework.sample.db.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userInfo")
public class User implements Serializable {
 
    private static final long serialVersionUID = -2343243243242432341L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
 
    @Column(name = "userId")
    private String userId;
 
    @Column(name = "userName")
    private String userName;
 
    protected User() {
    }
 
    public User(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }
 
    @Override
    public String toString() {
        return String.format("User Info [id=%d, userId='%s', userName='%s'] \n", id, userId, userName);
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
```

#### 3) Controller

위에서 준비된 내용을 URL에 따라 처리하는 샘플이다.

```java
package com.brainz.framework.sample.db.controller;

import java.util.Arrays;
import java.util.List;

import com.brainz.framework.sample.db.model.User;
import com.brainz.framework.sample.db.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
 
@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;
    
    @GetMapping("/sample/db/init")
    public String init() {      
        // User 여러개 넣기
        userRepository.saveAll(Arrays.asList(new User("hcjung", "정희찬")
                       , new User("jje", "정지은")
                       , new User("smkim", "김성민")
                       , new User("hcpark", "박현철")
                       , new User("kbh", "김범호")
                       , new User("wdj", "우다정")
                       , new User("jylim", "임지영")
                       , new User("shlee", "이소현")
                       , new User("hc.jung", "정현규")));
        
        return "데이터가 생성되었습니다.";
    }
    
    @PostMapping("/sample/db/insert")
    public String create(@RequestBody User user) {
        // User 1개 넣기
        userRepository.save(new User(user.getUserId(), user.getUserName()));

        return "데이터가 생성되었습니다.";
    }
    
    @GetMapping("/sample/db/findall")
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        return users;
    }
    
    @RequestMapping("/sample/db/search/{id}")
    public String search(@PathVariable long id) {
        String user = "";
        user = userRepository.findById(id).toString();
        return user;
    }
    
    @RequestMapping("/sample/db/searchByUserId/{userId}")
    public List<User> fetchDataByUserId(@PathVariable String userId) {  
        List<User> users = userRepository.findByUserId(userId);
        return users;
    }
}
```


## 5. 참고 사이트

[스프링부트와 PostgreSQL연동-영문](https://dzone.com/articles/spring-boot-and-postgresql)  
[스프링부트와 PostgreSQL연동 간략소개](https://engkimbs.tistory.com/789)

## 6. TO DO

- JPA를 사용하지 않는 경우에 대한 내용을 보강할 필요가 있다.
- JPA나 스프링부트에 대한 학습이 부족한 상태라서 앞으로 많은 업데이트를 해야 할 것 같다.