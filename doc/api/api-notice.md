#공지사항

## 목차

---

1. [데이터 추가](#데이터-추가)
2. [데이터 수정](#데이터-수정)
3. [데이터 삭제](#데이터-삭제)

## 데이터 추가

---

### URL

```
POST /rest/notices
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
  "noticeNo": "",
  "noticeTitle": "새 공지사항입니다.",
  "noticeContents": "내용입니다.",
  "popYn": false,
  "popStrDt": null,
  "popEndDt": null,
  "popWidth": null,
  "popHeight": null,
  "topNoticeYn": false,
  "topNoticeStrtDt": null,
  "topNoticeEndDt": null,
  "createDt": "2021-12-14T04:49:52.002485",
  "createUserKey": null,
  "createUserName": null,
  "updateDt": null,
  "updateUserKey": null,
  "fileSeq": 0,
  "delFilSeq": null
}
```

## 데이터 수정

---

### URL

```
PUT /rest/notices/{noticeId}
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
  "noticeId": "40288a9d7db60f6e017db74d89ea0003",
  "noticeDto": {
    "noticeNo": "40288a9d7db60f6e017db74d89ea0003",
    "noticeTitle": "새 공지사항입니다.",
    "noticeContents": "내용 수정합니다.",
    "popYn": true,
    "popStrDt": "2021-12-14T05:04",
    "popEndDt": "2022-01-13T05:04",
    "popWidth": 500,
    "popHeight": 500,
    "topNoticeYn": false,
    "topNoticeStrtDt": null,
    "topNoticeEndDt": null,
    "createDt": "2021-12-14T04:49:52.002485",
    "createUserKey": null,
    "createUserName": null,
    "updateDt": null,
    "updateUserKey": null,
    "fileSeq": 0,
    "delFilSeq": null
  }
}
```

## 데이터 삭제

---

### URL

```
PUT /rest/notices/{noticeId}
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
  "noticeId": "40288a9d7db60f6e017db74d89ea0003"
}
```