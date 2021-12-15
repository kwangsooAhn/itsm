# 게시판


## 목차

---

1. [게시판 관리_데이터 조회](#게시판-관리-데이터-조회)
2. [게시판 관리_데이터 추가](#게시판-관리-데이터-추가)
3. [게시판 관리_데이터 수정](#게시판-관리-데이터-수정)
4. [게시판 관리_데이터 삭제](#게시판-관리-데이터-삭제)
5. [게시판_데이터 추가](#게시판-데이터-추가)
6. [게시판_데이터 수정](#게시판-데이터-수정)
7. [게시판_데이터 삭제](#게시판-데이터-삭제)
8. [게시판 댓글_데이터 추가](#게시판-댓글-데이터-추가)
9. [게시판 댓글_데이터 수정](#게시판-댓글-데이터-수정)
10. [게시판 댓글_데이터 삭제](#게시판-댓글-데이터-삭제)
11. [게시판 답글_데이터 추가](#게시판-답글-데이터-추가)

## 게시판 관리_데이터 조회

---

### URL
```
GET /rest/boards/{boardAdminId}/view
```

### Parameter Sample

```json
{
  "boardAdminId": "40288a19736b46fb01736b718cb60001"
}
```

### Response Sample

```json
{
    "boardAdminId": "40288a19736b46fb01736b718cb60001",
    "boardAdminTitle": "자유 게시판",
    "boardAdminDesc": null,
    "boardAdminSort": 1,
    "boardUseYn": true,
    "replyYn": true,
    "commentYn": true,
    "categoryYn": false,
    "attachYn": true,
    "attachFileSize": 1024,
    "boardBoardCount": 0,
    "enabled": null,
    "categoryInfo": [],
    "createDt": "2021-11-15T02:27:39.174855",
    "createUserName": "ADMIN"
}
```

## 게시판 관리_데이터 추가

---

### URL
```
POST /rest/boards
```

### Parameter Sample

```json
{
  "boardAdminTitle": "박주현_TEST",
  "boardAdminDesc": "TEST",
  "boardAdminSort": "",
  "boardUseYn": true,
  "replyYn": true,
  "commentYn": true,
  "categoryYn": false,
  "attachYn": true,
  "attachFileSize": "1024"
}
```

### Response Sample

```json
true
```
## 게시판 관리_데이터 수정

---

### URL
```
PUT /rest/boards
```

### Parameter Sample

```json
{
  "boardAdminTitle": "박주현_수정_TEST",
  "boardAdminDesc": "TEST",
  "boardAdminSort": "",
  "boardUseYn": true,
  "replyYn": true,
  "commentYn": true,
  "categoryYn": false,
  "attachYn": true,
  "attachFileSize": "1000",
  "boardAdminId": "4028adf67dbb3a90017dbb66a0840000"
}
```

### Response Sample

```json
true
```

## 게시판 관리_데이터 삭제

---

### URL
```
DELETE /rest/boards/{boardAdminId}
```

### Parameter Sample

```json
{
  "boardAdminId": "4028adf67dbb3a90017dbb66a0840000"
}
```

### Response Sample

```json
true
```

## 게시판_데이터 추가

---

### URL
```
POST /rest/boards/articles
```

### Parameter Sample

```json
{
  "boardAdminId": "40288a19736b46fb01736b718cb60001",
  "boardTitle": "박주현_TEST",
  "boardContents": "{\"ops\":[{\"insert\":\"test\\n\"}]}",
  "fileSeqList": [],
  "delFileSeqList": []
}
```

### Response Sample

```
null
```

## 게시판_데이터 수정

---

### URL
```
PUT /rest/boards/articles
```

### Parameter Sample

```json
{
  "boardAdminId": "40288a19736b46fb01736b718cb60001",
  "boardTitle": "박주현_TEST",
  "boardContents": "{\"ops\":[{\"insert\":\"test\\n\"}]}",
  "fileSeqList": [],
  "delFileSeqList": [],
  "boardId": "4028adf67dbb3a90017dbb92e5940003"
}
```

### Response Sample

```
null
```

## 게시판_데이터 삭제

---

### URL
```
DELETE /rest/boards/articles/{boardId}
```

### Parameter Sample

```json
{
  "boardId": "4028adf67dbb3a90017dbb92e5940003"
}
```

### Response Sample

```
null
```

## 게시판 댓글_데이터 추가

---

### URL
```
POST /rest/boards/articles/comments
```

### Parameter Sample

```json
{
  "boardId": "4028adf67dbb3a90017dbba4ba2b0005",
  "boardCommentContents": "댓글 테스트"
}
```

### Response Sample

```
null
```

## 게시판 댓글_데이터 수정

---

### URL
```
PUT /rest/boards/articles/comments
```

### Parameter Sample

```json
{
  "boardCommentId": "4028adf67dbb3a90017dbba53ae40007",
  "boardId": "4028adf67dbb3a90017dbba4ba2b0005",
  "boardCommentContents": "댓글 테스트_수정"
}
```

### Response Sample

```
null
```

## 게시판 댓글_데이터 삭제

---

### URL
```
DELETE /rest/boards/articles/comments/{commentId}
```

### Parameter Sample

```json
{
  "commentId": "4028adf67dbb3a90017dbba53ae40007"
}
```

### Response Sample

```
null
```

## 게시판 답글_데이터 추가

---

### URL
```
POST /rest/boards/articles/reply
```

### Parameter Sample

```json
{
  "boardAdminId": "40288a19736b46fb01736b718cb60001",
  "boardTitle": "RE : 박주현_TEST",
  "boardContents": "{\"ops\":[{\"insert\":\"답글 테스트\\n\"}]}",
  "fileSeqList": [],
  "delFileSeqList": [],
  "boardId": "4028adf67dbb3a90017dbba4ba2b0005"
}
```

### Response Sample

```
null
```
