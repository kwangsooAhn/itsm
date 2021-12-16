# CMDB_CI Class


## 목차

---

1. [데이터 단일 조회](#데이터-단일-조회)
2. [데이터 목록 조회](#데이터-목록-조회)
3. [데이터 추가](#데이터-추가)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)
6. [데이터 세부 속성 조회](#데이터-세부-속성-조회)

## 데이터 단일 조회

---

### URL
```
GET /rest/cmdb/classes/{classId}
```

### Parameter Sample

```json
{
  "classId": "4028b88179210e1b017921201e8f0007"
}
```

### Response Sample

```json
{
  "classId": "4028b88179210e1b017921201e8f0007",
  "className": "WinNT ",
  "classDesc": "WinNT Class입니다.",
  "classSeq": 30,
  "editable": true,
  "attributes": [
    {
      "classId": "4028b88179210e1b017921201e8f0007",
      "attributeId": "df0e88d216ace73e0164f3dbf7ade131",
      "attributeName": "버전",
      "classLevel": 6,
      "order": 1
    }
  ],
  "extendsAttributes": [
    {
      "classId": "4028b881792074460179210677bb0016",
      "attributeId": "27caaeba596663101d55a09ec873a375",
      "attributeName": "상태",
      "classLevel": 1,
      "order": 1
    },
    ...
  ],
  "pclassName": "인프라정보 - 서버",
  "pclassId": "4028b88179210e1b0179211d13760005"
}
```

## 데이터 목록 조회

---

### URL
```
GET /rest/cmdb/classes
```

### Response Sample

```json
{
  "data": [
    {
      "classId": "root",
      "className": "root",
      "classDesc": "root",
      "classLevel": 0,
      "totalAttributes": 0,
      "pclassId": null,
      "pclassName": null
    },
    ...
  ],
  "totalCount": 62
}
```

## 데이터 추가

---

### URL
```
POST /rest/cmdb/classes
```

### Parameter Sample

```json
{
  "classId": "",
  "className": "인프라정보 - 서버",
  "classDesc": "TEST",
  "classSeq": "1",
  "attributes": [
    "d47973f063130acab00b2cf203a9788b"
  ],
  "pclassId": "4028b88179210e1b0179211d13760005"
}
```

### Response Sample

```
"0"
```

## 데이터 수정

---

### URL
```
PUT /rest/cmdb/classes/{classId}
```

### Parameter Sample

```json
{
  "classId": "4028adf67dbcfb92017dbd284cc00001",
  "className": "인프라정보 - 서버",
  "classDesc": "수정_TEST",
  "classSeq": "1",
  "attributes": [
    "d47973f063130acab00b2cf203a9788b"
  ],
  "pclassId": "4028b88179210e1b0179211d13760005"
}
```

### Response Sample

```
"1"
```

## 데이터 삭제

---

### URL
```
DELETE /rest/cmdb/classes/{classId}
```

### Parameter Sample

```json
{
  "classId": "4028adf67dbcfb92017dbd284cc00001"
}
```

### Response Sample

```
"0"
```

## 데이터 세부 속성 조회

---

### URL
```
GET /rest/cmdb/classes/{classId}/attributes
```

### Parameter Sample

```json
{
  "classId": "4028b88179210e1b0179211eb65d0006"
}
```

### Response Sample

```json
[
  {
    "className": "일반정보 - 인프라",
    "attributes": [
      {
        "attributeId": "27caaeba596663101d55a09ec873a375",
        "attributeName": "상태",
        "attributeDesc": "일반정보 - 인프라",
        "attributeText": "상태",
        "attributeType": "radio",
        "attributeOrder": 1,
        "attributeValue": "{\"option\":[{\"text\":\"사용\",\"value\":\"use\"},{\"text\":\"미사용\",\"value\":\"unused\"},{\"text\":\"폐기\",\"value\":\"disposal\"},{\"text\":\"할당\",\"value\":\"assignment\"},{\"text\":\"반납\",\"value\":\"return\"},{\"text\":\"AS\",\"value\":\"as\"},{\"text\":\"예비\",\"value\":\"spare\"}]}",
        "value": null,
        "childAttributes": []
      },
      ...
    ]
  }
]
```