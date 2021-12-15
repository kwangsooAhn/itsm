# CMDB_CI Attribute


## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 수정](#데이터-수정)
4. [데이터 삭제](#데이터-삭제)

## 데이터 조회

---

### URL
```
GET /rest/cmdb/attributes
```

### Response Sample

```json
{
    "data": [
        {
            "attributeId": "2c9180837c99e748017c9b099d500000",
            "attributeName": "AAAtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttestt",
            "attributeText": "",
            "attributeType": "inputbox",
            "attributeDesc": ""
        },
        ...
    ],
    "paging": {
        "totalCount": 189,
        "totalCountWithoutCondition": 189,
        "currentPageNum": 0,
        "totalPageNum": 13,
        "orderType": "common.label.nameAsc"
    }
}
```

## 데이터 추가

---

### URL
```
POST /rest/cmdb/attributes
```

### Parameter Sample

```json
{
  "attributeId": "",
  "attributeName": "박주현_TEST",
  "attributeType": "inputbox",
  "attributeText": "TEST",
  "mappingId": "",
  "attributeDesc": "TEST",
  "attributeValue": {
    "validate": "",
    "required": "true",
    "maxLength": "1000",
    "minLength": "0"
  }
}
```

### Response Sample

```
0
```

## 데이터 수정

---

### URL
```
PUT /rest/cmdb/attributes/{attributeId}
```

### Parameter Sample

```json
{
  "attributeId": "4028adf67dbcfb92017dbd1a75df0000",
  "attributeName": "박주현_수정_TEST",
  "attributeType": "inputbox",
  "attributeText": "TEST",
  "mappingId": "",
  "attributeDesc": "TEST",
  "attributeValue": {
    "validate": "",
    "required": "true",
    "maxLength": "1000",
    "minLength": "0"
  }
}
```

### Response Sample

```
0
```

## 데이터 삭제

---

### URL
```
DELETE /rest/cmdb/attributes/{attributeId}
```

### Parameter Sample

```json
{
  "attributeId": "4028adf67dbcfb92017dbd1a75df0000"
}
```

### Response Sample

```
0
```
