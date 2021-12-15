# 코드


## 목차

---

1. [코드_데이터 전체 목록 조회](#코드-데이터-전체-목록-조회)
2. [코드_데이터 상세 조회](#코드-데이터-상세-조회)
3. [연관 코드_상세 조회](#연관-코드-상세-조회)
4. [코드_데이터 추가](#코드-데이터-추가)
5. [코드_데이터 수정](#코드-데이터-수정)
6. [코드_데이터 삭제](#코드-데이터-삭제)
7. [코드_엑셀 다운로드](#코드-엑셀-다운로드)

## 코드_데이터 전체 목록 조회

---

### URL
```
GET /rest/codes
```

### Response Sample

```json
{
    "data": [
        {
            "code": "root",
            "codeValue": null,
            "codeName": "ROOT",
            "codeDesc": null,
            "editable": false,
            "createDt": "2021-11-15T02:27:37.387122",
            "level": 0,
            "seqNum": 0,
            "codeLangName": null,
            "lang": null,
            "pcode": null
        }, 
        ...
    ],
    "totalCount": 206
}
```

## 코드_데이터 상세 조회

---

### URL
```
GET /rest/codes/{code}
```

### Parameter Sample

```json
{
  "code": "cmdb.relation.type.default"
}
```

### Response Sample

```json
{
	"code": "cmdb.relation.type.default",
	"codeName": "default",
	"codeValue": "default",
	"codeDesc": "기본 연관",
	"editable": true,
	"useYn": true,
	"level": 3,
	"seqNum": 1,
	"codeLang": "",
	"pcode": "cmdb.relation.type"
}
```

## 연관 코드_상세 조회

---

### URL
```
GET /rest/codes/related/{code}
```

### Parameter Sample

```json
{
  "code": "cmdb.relation.type"
}
```

### Response Sample

```json
{
    "code": "cmdb.relation.type.default",
    "codeValue": "default",
    "codeName": "default",
    "codeDesc": "기본 연관",
    "editable": true,
    "createDt": "2021-08-24T15:15:09.257758",
    "level": 3,
    "seqNum": 1,
    "codeLangName": null,
    "lang": null,
    "pcode": "cmdb.relation.type"
}
```

## 코드_데이터 추가

---

### URL
```
POST /rest/codes
```

### Parameter Sample

```json
{
  "code": "test",
  "pCode": "root",
  "codeName": "박주현_TEST",
  "codeValue": "test",
  "codeDesc": "",
  "seqNum": "",
  "useYn": true
}
```

### Response Sample

```
0
```

## 코드_데이터 수정

---

### URL
```
PUT /rest/codes/{code}
```

### Parameter Sample

```json
{
  "code": "cmdb.relation.type.default",
  "pCode": "cmdb.relation.type",
  "codeName": "default_수정TEST",
  "codeValue": "default",
  "codeDesc": "기본 연관",
  "seqNum": "1",
  "useYn": true
}
```

### Response Sample

```
1
```

## 코드_데이터 삭제

---

### URL
```
DELETE /rest/codes/{code}
```

### Parameter Sample

```json
{
  "code": "cmdb.relation.type.default"
}
```

### Response Sample

```
0
```

## 코드_엑셀 다운로드

---

### URL
```
GET /rest/codes/excel
```

### Parameter Sample

```json
{
  "search": "",
  "pCode": ""
}
```

### Response Sample

```
excel download
```