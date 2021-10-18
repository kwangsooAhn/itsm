# 문서 댓글

문서에 댓글을 조회, 추가, 삭제하는 API 이다.

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 삭제](#데이터-삭제)

## 데이터 조회

---

### URL
```
GET /rest/instances/{instanceId}/comments
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
  "data": {
    "instanceId": "71b48ef3ccca47a09c5a9c038d8b7a2a",
    "comments": [
      {
        "commentId": "2c9180ab7b369096017b3780d1f80001",
        "content": "잘 작성하셨습니다.",
        "createUserName": "정희찬",
        "createUserKey": "2c9180ab7b2a039b017b2a15b1f40001",
        "createDt": "2021-08-11T23:16:34.277465",
        "avatarPath": "/assets/media/images/avatar/img_avatar_01.png"    
      },
      {
        "commentId": "2c9180ab7b369096017b3780d1f80001",
        "content": "정리가 잘 되었네요.",
        "createUserName": "정희찬",
        "createUserKey": "2c9180ab7b2a039b017b2a15b1f40001",
        "createDt": "2021-08-11T23:16:34.277465",
        "avatarPath": "/assets/media/images/avatar/img_avatar_01.png"
      }
    ]
  }
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
POST /rest/instances/{instanceId}/comments
```

### Parameter Sample

```json
{
  "data": {
    "instanceId": "71b48ef3ccca47a09c5a9c038d8b7a2a",
    "content": "오타 수정해주세요."
  }
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
DELETE /rest/instances/{instanceId}/comments/{commentId}
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
