# 커스텀 코드

## 목차

---

1. [데이터 목록 조회](#데이터-목록-조회)
2. [데이터 조회](#데이터-조회)
3. [데이터 추가](#데이터-추가)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)

## 데이터 목록 조회

---

### URL

```
GET /rest/custom-codes
```

### Response Sample

```json
{
  "data": [
    {
      "customCodeId": "40288ab777dd21b50177dd52781e0000",
      "type": "code",
      "customCodeName": "데이터베이스",
      "sessionKey": null,
      "totalCount": 0,
      "createDt": "2021-12-09T15:51:18.053853",
      "createUserName": "ADMIN"
    },
    ...
  ],
  "paging": {
    "totalCount": 7,
    "totalCountWithoutCondition": 7,
    "currentPageNum": 0,
    "orderType": "common.label.nameAsc"
  }
}
```

## 데이터 조회

---

```
GET /rest/custom-codes/{customCodeId}
```

### Parameter Sample

```json
{
  "customCodeId": "40288a19736b46fb01736b89e46c0009"
}
```

### Response Sample

```json
{
  "data": [
    {
      "code": "department.group",
      "codeValue": null,
      "codeName": "부서 명",
      "codeDesc": null,
      "editable": false,
      "createDt": null,
      "level": 2,
      "seqNum": 1,
      "pCode": "department"
    },
    ...
  ],
  "totalCount": 4
}
```

## 데이터 추가

---

```
POST /rest/custom-codes
```

### Parameter Sample

```json
{
  "customCodeId": "",
  "type": "table",
  "customCodeName": "커스텀 코드 추가 예시",
  "targetTable": "awf_user",
  "targetTableName": null,
  "searchColumn": "department",
  "searchColumnName": null,
  "valueColumn": "user_key",
  "valueColumnName": null,
  "pCode": null,
  "condition": "[]",
  "sessionKey": "",
  "createDt": null,
  "createUserName": null,
  "enabled": true
}
```

### Response Sample

```
{
  "0"
}
```

## 데이터 수정

---

```
PUT /rest/custom-codes
```

### Parameter Sample

```json
{
  "customCodeId": "40288a9d7db25707017db2632c700000",
  "type": "table",
  "customCodeName": "커스텀 코드 추가 예시",
  "targetTable": "awf_user",
  "targetTableName": null,
  "searchColumn": "department",
  "searchColumnName": null,
  "valueColumn": "user_key",
  "valueColumnName": null,
  "pCode": null,
  "condition": "[]",
  "sessionKey": "userId",
  "createDt": null,
  "createUserName": null,
  "enabled": true
}
```

### Response Sample

```
{
  "0"
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/custom-codes/{customCodeId}
```

### Parameter Sample

```json
{
  "customCodeId": "40288a9d7db25707017db2632c700000"
}
```

### Response Sample

```
{
  "0"
}
```