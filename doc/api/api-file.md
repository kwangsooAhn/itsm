# 파일 관리

문서에 첨부파일을 자동으로 넣는 기능은 스크립트 태스크를 이용해서 가능하다.

이때 첨부파일은 파일 관리를 통해서 등록되어 있는 파일에 한해서 설정이 가능하다.

이미지뿐 아니라 다양한 종류의 문서가 첨부파일로 등록될 수 있다.

해당 API는 파일을 조회 / 등록 / 수정 / 삭제 / 다운로드 하는 API이다.

## 목차

---

1. [전체 데이터 조회](#전체-데이터-조회)
2. [상세 데이터 조회](#상세-데이터-조회)
3. [데이터 등록](#데이터-등록)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)
5. [데이터 다운로드](#데이터-다운로드)

## 전체 데이터 조회

---

### URL

```
GET /rest/files
```

### Response Sample

```json
{
  "type": "file",
  "searchValue": "",
  "offset": 0,
  "data": [
    {
      "name": "excel.xlsx",
      "fullpath": "D:\\files\\shared\\excel.xlsx",
      "extension": "xlsx",
      "size": "37.2 KB",
      "data": null,
      "width": null,
      "height": null,
      "updateDt": "2022-02-08T01:44:32.933"
    },
    ...
  ],
  "totalCount": 2
}
```

## 상세 데이터 조회

---

### URL

```
GET /rest/files/{name}
```

### Parameter Sample

```json
{
  "name": "itsm.png"
}
```

### Response Sample

```json
{
  "name": "itsm.png",
  "fullpath": "D:\\files\\shared\\itsm.png",
  "extension": "png",
  "size": "1.9 KB",
  "data": "iVBORw0KGgoAAAANSUhEUgAAAL8AAAA0CAYAAADbnekdAAAAAXNSR0IArs4c6QAAAARnQU1BAA...",
  "width": 191,
  "height": 52,
  "updateDt": "2022-02-08T02:08:55.907"
}
```

## 데이터 등록

---

### URL

```
POST /rest/files
```

### Parameter Sample

```json
{
  "multipartFiles" : "ITSM.PNG"
}
```

### Response Sample

```
true
```

## 데이터 수정

---

### URL

```
PUT /rest/files
```

### Parameter Sample

```json
{
  "originName": "ITSM.PNG",
  "modifyName": "ITSM_수정.PNG"
}
```

### Response Sample

```
true
```

## 데이터 삭제

---

### URL

```
DELETE /rest/files/{name}
```

### Parameter Sample

```json
{
  "name": "itsm.png"
}
```

### Response Sample

```
true
```

## 데이터 다운로드

---

### URL

```
GET /rest/files/download
```

### Parameter Sample

```json
{
  "fileName": "download.pdf"
}
```

### Response Sample

```
ResponseEntity<InputStreamResource>
```
