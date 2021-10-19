# 관련문서

모든 업무흐름상의 문서들은 서로 연관관계를 맺을 수 있으며 특정 문서에 관련된 문서로 등록할 수 있다.

이런 연관관계를 하나의 폴더에 묶어놓는 개념으로 생각하여 `folder`라는 표현을 사용한다.

폴더는 문서가 하나 생성되면 무조건 같이 생성되며 문서와 라이프사이클을 같이 한다.

즉, 폴더 자체를 삭제하는 `URL`은 제공되지 않고 조회 및 폴더에 관련문서를 추가/삭제만 할 수 있다.

문서를 처리하거나 조회할때 참고가 되는 `관련문서`를 추가/조회/삭제하는 API 이다.

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 삭제](#데이터-삭제)

## 데이터 조회

---

### URL
```
GET /rest/folders/{folderId}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
  "data": [
    {
      "folderId": "299b4b55c11c4f809531a95eb3d8f670",
      "instanceId": "71b48ef3ccca47a09c5a9c038d8b7a2a",
      "tokenId": "2c9180ab7b369096017b3780d1f80001",
      "relatedType": "reference",
      "documentNo": "Test-20210811-001",
      "documentName": "김성민_테스트_001",
      "documentColor": "#4C64D4",
      "instanceCreateUserKey": "2c9180ab7b2a039b017b2a15b1f40001",
      "instanceCreateUserName": "정희찬",
      "instanceStartDt": "2021-08-11T23:16:34.277465",
      "instanceEndDt": "",
      "status": "running",
      "topics": ["topic1", "topic2"],
      "avatarPath": "/assets/media/images/avatar/img_avatar_01.png"    
    },
    {
      "folderId": "299b4b55c11c4f809531a95eb3d8f670",
      "instanceId": "fe7e4cd9e6b849f09782f2b357769ff3",
      "tokenId": "2c9180ab7b3bb6ec017b3e59b1220003",
      "relatedType": "reference",
      "documentNo": "Test-20210811-001",
      "documentName": "김성민_테스트_001",
      "documentColor": "#4C64D4",
      "instanceCreateUserKey": "2c9180ab7b2a039b017b2a15b1f40001",
      "instanceCreateUserName": "정희찬",
      "instanceStartDt": "2021-08-11T23:16:34.277465",
      "instanceEndDt": "2021-08-23T23:16:34.277465",
      "status": "finish",
      "topics": ["topic3", "topic4"],
      "avatarPath": "/assets/media/images/avatar/img_avatar_01.png"
    }
  ]
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|

## 데이터 추가

---

### URL
```
POST /rest/folders/
```

### Parameter Sample

```json
{
  "folderId": "299b4b55c11c4f809531a95eb3d8f670",
  "instanceId": "71b48ef3ccca47a09c5a9c038d8b7a2a"
}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|

## 데이터 삭제

---

### URL
```
DELETE /rest/folders/{folderId}/instances/{instanceId}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|
