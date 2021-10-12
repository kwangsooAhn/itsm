# API documentation

ITSM에서 Frontend와 Backend 사이에서 사용하는 API를 정리한다.

URL 앞에 `[View]`라고 표기된 내용은 화면을 리턴하고 `[Rest]`라고 표기된 내용은 Json Object를 리턴한다.

## 목차

---

1. [검색 & 리스트 화면](#검색-&-리스트-화면)

## 검색 & 리스트 화면

---

기본적으로 데이터를 조회하고 리스트 형태로 검색 결과를 출력하는 화면이며, 일반적으로 메뉴를 클릭하는 경우 첫번째 화면이다.

### URL
```
[View] GET /{DomainName}/search
```

### Parameter

| 이름 | 설명 | 필수여부 | 기본값 |
|---|:---|:---:|:---:|
|`DomainName`|`notices`(공지사항), `faqs`(FAQ), `users`(사용자), `forms`(문서양식)과 같은 예제가 있으며 실제 제공되는 서비스를 뜻합니다. 모든 서비스도메인이 위와 같은 API를 제공하지는 않으며 화면 구성에 따라 사전에 정의되어야 합니다.| O | X |

### Response
* 리턴되는 내용은 `{DomainName}Search.html` 파일이다.  

### Error

| 에러코드 | 설명 | 
|:---|:---|
|
