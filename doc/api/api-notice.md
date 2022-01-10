# 공지사항


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

### Parameter Sample

```json
{
  "noticeTitle": "박주현_TEST",
  "noticeContents": "TEST",
  "popYn": false,
  "popStrtDt": "",
  "popEndDt": "",
  "popWidth": "",
  "popHeight": "",
  "topNoticeYn": true,
  "topNoticeStrtDt": "2021-12-14T05:01:00.000Z",
  "topNoticeEndDt": "2022-01-13T05:01:00.000Z",
  "fileSeq": [
    "2"
  ]
}
```

### Response Sample

```
null
```

## 데이터 수정

---

### URL

```
PUT /rest/notices/{noticeId}
```

### Parameter Sample

```json
{
  "noticeId": "40288a8c7db763b7017db77859300000",
  "noticeTitle": "박주현_수정_TEST",
  "noticeContents": "TEST",
  "popYn": false,
  "popStrtDt": "",
  "popEndDt": "",
  "popWidth": "",
  "popHeight": "",
  "topNoticeYn": false,
  "topNoticeStrtDt": "",
  "topNoticeEndDt": "",
  "fileSeq": [],
  "noticeNo": "40288a8c7db763b7017db77859300000",
  "delFileSeq": [
    "4"
  ]
}
```

### Response Sample

```
null
```

## 데이터 삭제

---

### URL

```
DELETE /rest/notices/{noticeId}
```

### Parameter Sample

```json
{
  "noticeId": "40288a8c7db763b7017db77859300000"
}
```

### Response Sample

```
null
```

