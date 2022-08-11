# 리소스 관리

공유 폴더/파일을 추가하거나 삭제, 조회하는 등 관리가 가능하다.

공유 폴더는 application.yml 파일의 file.upload.dir 에서 설정이 가능하다.

리소스 관리에 등록된 파일은 폼 디자이너, 프로세스 디자이너 등에서 사용된다.

이미지뿐 아니라 다양한 종류의 문서가 파일로 등록될 수 있다.

해당 API는 폴더 및 파일을 조회 / 등록 / 수정 / 삭제 / 다운로드 하는 API이다.

## 목차

---

1. [Drag 사용 여부](#dragEnabled)
2. [기본 경로 조회](#basePath)
3. [폴더 등록](#폴더-등록)
4. [폴더 이름 변경](#폴더-이름-변경)
5. [폴더 삭제](#폴더-삭제)
6. [파일 목록 조회](#파일-목록-조회)
7. [파일 조회](#파일-조회)
8. [파일 등록](#파일-등록)
9. [파일 이름 변경](#파일-이름-수정)
10. [파일 삭제](#파일-삭제)
11. [파일 다운로드](#파일-다운로드)
12. [파일 허용 확장자 목록 조회](허용-확장자-목록-조회)

## Drag 사용 여부 

application.yml 파일의 file.drag.enabled 에서 설정이 가능하다.

---

### URL

```
GET /rest/resources/enabledFileDrag
```

### Response Sample

```
true or false
```

## 기본 경로 조회

---

### URL

```
GET /rest/resources/basePath
```

### Response Sample

```
C:/files/...
```

## 폴더 등록

---

### URL

```
POST /rest/resources/folder
```

### Parameter Sample

```json
{
  "originPath": "C:/files/폴더명",
  "modifyPath": ""
}
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 폴더 이름 변경

---

### URL

```
PUT /rest/resources/folder
```

### Parameter Sample

```json
{
  "originPath": "C:/files/수정전폴더명",
  "modifyPath": "C:/files/수정후폴더명"
}
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 폴더 삭제

---

### URL

```
DELETE /rest/resources/folder?path=C:/files/폴더명
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 파일 목록 조회

---

### URL

```
GET /rest/resources/files?ownId=4028adf67dc0690b017dc11989590005&fileDataId=1
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": [
    {
      "ownId": "",
      "fileLocDto": {
        "fileSeq": 1,
        "fileOwner": "",
        "uploaded": true,
        "uploadedLocation": "",
        "randomName": "",
        "originName": "",
        "fileSize": 1500,
        "sort": 1
      }
    },
    ...
  ]
}
```

## 파일 조회

---

### URL

```
GET /rest/resources/file
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": {
    "name": "itsm.png",
    "fullPath": "D:\\files\\shared\\itsm.png",
    "extension": "png",
    "size": "1.9 KB",
    "data": "iVBORw0KGgoAAAANSUhEUgAAAL8AAAA0CAYAAADbnekdAAAAAXNSR0IArs4c6QAAAARnQU1BAA...",
    "width": 191,
    "height": 52,
    "directoryYn": false,
    "imageFileYn": true,
    "count": 0,
    "editable": true,
    "updateDt": "2022-02-08T02:08:55.907"
  }
}
```

## 파일 등록

---

### URL

```
POST /rest/resources/file/upload?type=file&path=C:/files/폴더명
```

### Parameter Sample

```json
{
  "files" : "ITSM.PNG"
}
```

### Response Sample

```
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 파일 이름 변경

---

### URL

```
PUT /rest/resources/file
```

### Parameter Sample

```json
{
  "originPath": "C:/files/ITSM.PNG",
  "modifyPath": "C:/files/ITSM_수정.PNG"
}
```

### Response Sample

```
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 파일 삭제

---

### URL

```
DELETE /rest/resources/file?type=file&path=c:/files/server.png
```

### Response Sample

```
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 파일 다운로드

---

### URL

```
GET /rest/resources/file/download?type=file&path=C:/files/server.png
```

### Response Sample

```
ResponseEntity<InputStreamResource>
```

## 파일 허용 확장자 목록 조회

---

### URL

```
GET /rest/resources/file/extensions
```

### Response Sample

```
["PNG", "GIF", "JPG", "JPEG"]
```