# SSO 연동

ITSM에서 Ksign `SSO 연동`에 대한 정보를 확인하는 문서이다.

## 목차

---

1. [SSO 동작 방식](#SSO-동작-방식)
2. [SSO 사용 여부 확인 URL](#SSO-사용-여부-확인-URL)
3. [SSO 토큰 확인 화면 URL](#SSO-토큰-확인-화면-URL)
4. [SSO 로그인 처리 URL](#SSO-로그인-처리-URL)
5. [SSO 토큰 정보](#SSO-토큰-정보)
6. [라이브러리](#라이브러리)

## SSO 동작 방식

---
ITSM에서 SSO연동은 다른 화면에서 SSO인증이 끝난 후 연결되어 토큰(rspData) 유효성만 체크하는 구조이다.

1. 고객사 포탈에서 ITSM으로 이동한다. (/itsm 호출)
2. 발급된 SSO 토큰 정보 중 "UID"가 확인된다면 UID를 RSA 암호화하여 서버로 전달한다.
3. 서버에서는 UID를 복호화하고 권한, 역할 등 정보를 포함하여 세션에 담는다.
4. ITSM로그인 처리가 되어 메인페이지로 이동된다.
5. "UID"가 확인되지 않을 경우 기존 로그인페이지로 이동된다.

## SSO 사용 여부 확인 URL

---

### URL

```
GET /itsm
```

### Parameter Sample

```
{
  "response": HttpServletResponse
}
```

### Response Sample
```
sso 설정 여부가 true일 경우 /itsm/sso redirect
ssp 설정 여부가 false일 경우 /portals/main redirect
```

## SSO 토큰 확인 화면 URL

---

### URL

```
GET /itsm/sso
```

### Parameter Sample

```
{
  "request": HttpServletRequest
}
```

### Response Sample
```
login.jsp 화면 호출 
```

## SSO 로그인 처리 URL

---

### URL

```
POST /itsm/ssoLogin
```

### Parameter Sample

```
{
  "request": HttpServletRequest,
  "response": HttpServletResponse
}
```

### Response Sample
```
/login redirect
```

## SSO 토큰 정보

---

SSO 토큰 발급이 완료된 후 확인할 수 있는 토큰의 정보는 아래와 같다.

```
1. UID
- 확인 방법 : rspData.getAttribute("UID");

2. 인증 토큰 획득 성공 여부
- 확인 방법 : rspData.getResultCode();
- Result Code : 성공 0 / 실패 -1

3. SSO토큰이 발급되지 않는 로컬 환경에서는 login.jsp 에서 "UID"를 임의로 지정하고 테스트해야 한다.
```

## 라이브러리

---

Ksign에서 제공한 라이브러리는 아래 경로에 위치해있다.

```
/libs/ksign
```

라이브러리 목록

```
1. kcasecrypto.jar
2. KSignAccessAgent-4.0.2.jar
3. KSignAccess-common-4.0.2.jar
4. KSignAccessLib2.0.jar
5. KSignAccessSSOAgent-w-4.1.2.jar
6. KSignCrypto_for_Java_v1.0.1.0.jar
7. KSignLicenseGenerator-2.7.8.jar
8. KSignLicenseVerify-2.5.0.jar
9. log4j-1.2.17.jar
- ksign에서 제공된 log4j의 버전은 1.2.8이지만 서버 실행이 안되는 이슈가 있어 1.2.17로 변경 
10. SSOUtil-2.5.jar
```
