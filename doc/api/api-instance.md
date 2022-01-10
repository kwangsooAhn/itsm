# Instance


## 목차

---

1. [문서 정보 조회](#문서-정보-조회)
2. [댓글 조회](#댓글-조회)
3. [댓글 추가](#댓글-추가)
4. [댓글 삭제](#댓글-삭제)

## 문서 정보 조회

---

### URL
```
GET /rest/instances/{instanceId}/history
```

### Parameter Sample

```json
{
  "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742"
}
```

### Response Sample

```json
[
  {
    "tokenStartDt": "2021-12-17T00:56:55.575328",
    "tokenEndDt": null,
    "elementName": "업무흐름검토",
    "elementType": "userTask",
    "tokenStatus": "token.status.running",
    "tokenAction": null,
    "assigneeId": "40288a8c7be6bdd0017be73ace110004",
    "assigneeName": "박주현"
  }
]
```

## 댓글 조회

---

### URL
```
GET /rest/instances/{instanceId}/comments
```

### Parameter Sample

```json
{
  "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742"
}
```

### Response Sample

```json
[
  {
    "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742",
    "commentId": "4028adf67dc5de46017dc5f4b2ff0004",
    "content": "test",
    "createUserKey": "40288a8c7be6bdd0017be73ace110004",
    "createUserName": "박주현",
    "createDt": "2021-12-17T01:14:47.864804",
    "avatarPath": "/assets/media/images/avatar/img_avatar_01.png"
  }
]
```

## 댓글 추가

---

### URL
```
POST /rest/instances/{instanceId}/comments
```

### Parameter Sample

```json
{
  "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742",
  "contents": "박주현_댓글_TEST"
}
```

### Response Sample

```
true
```

## 댓글 삭제

---

### URL
```
DELETE /rest/instances/{instanceId}/comments/{commentId}
```

### Parameter Sample

```json
{
  "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742",
  "commentId": "4028adf67dc5de46017dc5fbb0220005"
}
```

### Response Sample

```
true
```