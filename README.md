# Alice - Web Framework

비즈니스 도메인과 무관하게 웹에서 제공해야 할 기능들을 제공한다. 로그인, 보안, 파일처리등을 포함한다.

쉽게 생각하면 SpringMVC, SpringBoot같은 안정화된 기존 프레임워크를 이용하여 우리가 필요로 하는 기능들을 잘 붙여서 사용하기 편리하게 wrapper를 씌우는 일이다.
전자정부프레임워크를 그대로 사용하는 것은 어떨까 고민을 살짝 했는데.... 너무 범용적인 목표를 가진 프레임워크라 거추장스러운게 많고 "정부"라는 글자도 마음에 안든다.

###### 1) 주요 기능

* DB Connect
* G11n
* Security
* File Control
* Log
* Mail, SMS
* Document Control (Excel, PDF, Print)
* Auth

###### 2) 개발 환경 및 사용 기술 (검토 필요)

* RestFul
* SpringBoot 2.1.3
* PostgreSQL 11.2
* Open JDK 11
* JPA - ORM 사용에 대한 재검토 필요.
* Vue, sitemesh, tiles, thymeleaf