# Portal


## 목차

---

1. [포탈_검색](#포탈-검색)
2. [포탈_상단 게시글](#포탈-상단-게시글)
3. [포탈_파일 허용 확장자 목록 조회](#포탈-파일-허용-확장자-목록-조회)
4. [포탈_파일 목록 조회](#포탈-파일-목록-조회)
5. [포탈_파일 다운로드](#포탈-파일-다운로드)

## 포탈_검색

---

### URL
```
GET /rest/portals
```

### Parameter Sample

```json
{
  "searchValue": "test",
  "offset": 0
}
```

### Response Sample

```json
[
  {
    "portalId": "2c9180867da08bd2017da22443f90000",
    "portalTitle": "test",
    "portalContent": "test",
    "createDt": "2021-12-10T02:20:25.463033",
    "updateDt": "2021-12-10T02:20:36.027464",
    "tableName": "notice"
  },
  {
    "portalId": "4028b21e7cc9d192017cc9d64da60000",
    "portalTitle": "TEST",
    "portalContent": "download.category.companyPolicy",
    "createDt": "2021-10-29T02:17:17.47786",
    "updateDt": null,
    "tableName": "download"
  }
]
```

## 포탈_상단 게시글

---

### URL
```
GET /rest/portals/top
```

### Parameter Sample

```json
{
  "limit" : 1
}
```

### Response Sample

```json
{
  "notice": [
    {
      "id": "2c9180867da08bd2017da22443f90000",
      "title": "test",
      "content": "test",
      "createDt": "2021-12-10T02:20:25.463033"
    }
  ],
  "faq": [
    {
      "id": "4028b21e7cc96011017cc96340b30000",
      "title": "테스트",
      "content": "테스트",
      "createDt": "2021-10-29T00:11:37.521546"
    }
  ],
  "download": [
    {
      "id": "2c9180867d0b3336017d0dea5bcb0002",
      "title": "가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나",
      "content": "download.category.companyPolicy",
      "createDt": "2021-11-11T07:33:22.506995"
    }
  ]
}
```

## 포탈_파일 허용 확장자 목록 조회

---

### URL
```
GET /rest/portals/filenameextensions
```

### Response Sample

```json
[
  {
    "fileNameExtension": "TXT"
  },
  ...
]
```

## 포탈_파일 목록 조회

---

### URL
```
GET /rest/portals/filelist
```

### Parameter Sample

```json
{
  "ownId": "2c9180837c909d4e017c91c0e4490001",
  "fileDataId": ""
}
```

### Response Sample

```json
[
  {
    "ownId": "2c9180867da08bd2017da22443f90000",
    "fileLocDto": {
      "fileSeq": 264,
      "fileOwner": "40288a8c7be6bdd0017be73ace110004",
      "uploaded": true,
      "uploadedLocation": "D:\\files\\uploadRoot\\20211216",
      "randomName": "LUCDYJVMKLST",
      "originName": "image.PNG",
      "fileSize": 557,
      "sort": 0
    }
  }
]
```

## 포탈_파일 다운로드

---

### URL
```
GET /rest/portals/filedownload
```

### Parameter Sample

```json
{
  "seq" : 264
}
```

### Response Sample

```
ResponseEntity<InputStreamResource>
```
