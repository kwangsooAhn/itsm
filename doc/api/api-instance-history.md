# 문서 이력

문서의 이력(Token 이력)을 조회하는 API 이다.

## 목차

---

1. [데이터 조회](#데이터-조회)

## 데이터 조회

---

### URL
```
GET /rest/instances/{instanceId}/history
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
  "data": [
    {
      "tokenId": "71b48ef3ccca47a09c5a9c038d8b7a2a",
      "tokenStartDt": "2021-08-11T23:16:34.277465",
      "elementName": "신청서 작성",
      "assigneeName": "정희찬",
      "tokenStatus": "token.status.finish",
      "tokenAction": "token.action.finish"
    },
    {
      "tokenId": "a1b48ef3ccca47a09c5a9c038d8b7a2b",
      "tokenStartDt": "2021-08-12T23:16:34.277465",
      "elementName": "신청서 검토",
      "assigneeName": "정희찬",
      "tokenStatus": "token.status.running",
      "tokenAction": ""
    }
  ]
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|
