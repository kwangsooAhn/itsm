# FAQ

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 수정](#데이터-수정)
4. [데이터 삭제](#데이터-삭제)

## 데이터 조회

---

### URL

```
GET /rest/faqs/{faqId}
```

### Parameter Sample

```json
{
  "faqId": "40288a9d7db60f6e017db630574b0000",
  "request": {
    "rolePrefix": "ROLE_"
  }
}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
  "data": {
    "faqId": "40288a9d7db60f6e017db630574b0000",
    "faqGroup": "faq.category.setting",
    "faqGroupName": "설정",
    "faqTitle": "faq 신규등록",
    "faqContent": "안녕하세요",
    "createDt": "2021-12-13T23:46:01.162433",
    "createUserName": "ADMIN"
  }
}
```

## 데이터 추가

---

### URL

```
POST /rest/faqs
```

### Parameter Sample

```json
{
  "faqId": "",
  "faqGroup": "faq.category.setting",
  "faqTitle": "faq 신규등록",
  "faqContent": "안녕하세요",
  "createDt": "2021-12-14T00:12:04.106839300",
  "createUserKey": null,
  "updateDt": "2021-12-14T00:12:04.106839300",
  "updateUserKey": null
}
```

### Response Sample

```json
{
  "return": true
}
```

## 데이터 수정

---

### URL

```
PUT /rest/faqs/{faqId}
```

### Parameter Sample

```json
{
  "faqId": "40288a9d7db60f6e017db64bad810001",
  "faqGroup": "faq.category.techSupport",
  "faqTitle": "faq 신규등록",
  "faqContent": "분류 및 내용 수정합니다",
  "createDt": "2021-12-14T00:31:27.435771400",
  "createUserKey": null,
  "updateDt": "2021-12-14T00:31:27.435771400",
  "updateUserKey": null
}
```

### Response Sample

```json
{
  "return": true
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/faqs/{faqId}
```

### Parameter Sample

```json
{
  "faqId": "40288a9d7db60f6e017db64bad810001"
}
```

### Response Sample

```json
{
  "return": true
}
```
