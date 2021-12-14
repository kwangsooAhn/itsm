# 연관문서

(#11831 - [Response 공통화] 현재 상태 확인 및 MD 파일 작성_2) 을 위한 새파일 작성

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 삭제](#데이터-식제)

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
  "data": {
    "folderId": "af327115d88f42368e497a77fe01b8f9",
    "instanceId": "da043e4a2c6d44318eba059d6e6bb61c",
    "tokenId": "40288a9d7d9df3b9017d9df77a9d0001",
    "relatedType": "reference",
    "documentNo": "CSR-20211209-001",
    "documentName": "단순문의",
    "documentColor": "#64BBF6",
    "instanceCreateUserKey": "0509e09412534a6e98f04ca79abb6424",
    "instanceCreateUserName": "ADMIN",
    "instanceStaretDt": "2021-12-09T06:53:01.339948",
    "instanceEndDt": null,
    "instanceStatus": "running",
    "topic": null,
    "avatarPath": "/assets/media/images/avatar/img_avatar_01.png"
  }
}
```

## 데이터 추가

---

### URL

```
POST /rest/folders
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
}
```

### Parameter Sample

```json
{
  "folderId": "2913e8560f2b427ca911b10e0f8fd3e4",
  "instanceId": "da043e4a2c6d44318eba059d6e6bb61c"
}
```

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

### Parameter Sample

```json
{
  "folderId": "2913e8560f2b427ca911b10e0f8fd3e4",
  "instanceId": "da043e4a2c6d44318eba059d6e6bb61c"
}
```