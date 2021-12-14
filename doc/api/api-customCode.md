# 커스텀 코드


##목차

---

1. [데이터 목록 조회](#데이터-목록-조회)
2. [데이터 조회](#데이터-조회)
3. [데이터 추가](#데이터-추가)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)

## 데이터 목록조회

---

### URL

```
GET /rest/custom-codes
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
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
    {
      "customCodeId": "40288a19736b46fb01736b89e46c0009",
      "type": "code",
      "customCodeName": "사용자 부서 검색",
      "sessionKey": "departmentName",
      "totalCount": 0,
      "createDt": "2021-12-09T15:51:18.05219",
      "createUserName": "ADMIN"
    },
    {
      "customCodeId": "40288a19736b46fb01736b89e46c0008",
      "type": "table",
      "customCodeName": "사용자 이름 검색",
      "sessionKey": "userName",
      "totalCount": 0,
      "createDt": "2021-12-09T15:51:18.050315",
      "createUserName": "ADMIN"
    },
    {
      "customCodeId": "40288a19736b46fb01736b89e46c0010",
      "type": "code",
      "customCodeName": "서비스데스크 - 단순문의 : 서비스 항목",
      "sessionKey": null,
      "totalCount": 0,
      "createDt": "2021-12-09T15:51:18.056246",
      "createUserName": "ADMIN"
    },
    {
      "customCodeId": "40288a19736b46fb01736b89e46c0012",
      "type": "code",
      "customCodeName": "서비스데스크 - 서비스요청 : 요청구분",
      "sessionKey": null,
      "totalCount": 0,
      "createDt": "2021-12-09T15:51:18.059027",
      "createUserName": "ADMIN"
    },
    {
      "customCodeId": "40288a19736b46fb01736b89e46c0011",
      "type": "code",
      "customCodeName": "서비스데스크 - 장애신고 : 장애유형",
      "sessionKey": null,
      "totalCount": 0,
      "createDt": "2021-12-09T15:51:18.057632",
      "createUserName": "ADMIN"
    },
    {
      "customCodeId": "4028b22f7c96584f017c970ef75e0035",
      "type": "code",
      "customCodeName": "어플리케이션 변경관리 -관련 서비스",
      "totalCount": 0,
      "sessionKey": null,
      "createDt": "2021-12-09T15:51:18.060541",
      "createUserName": "ADMIN"
    }
  ]
}
```

## 데이터 조회

---

```
GET /rest/custom-codes/{customCodeId}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
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
    {
      "code": "department.group.design",
      "codeValue": "DESIGN",
      "codeName": "DESIGN",
      "codeDesc": null,
      "editable": false,
      "createDt": null,
      "level": 3,
      "seqNum": 1,
      "pCode": "department.group"
    },
    {
      "code": "department.group.itsm",
      "codeValue": "ITSM",
      "codeName": "ITSM",
      "codeDesc": null,
      "editable": false,
      "createDt": null,
      "level": 3,
      "seqNum": 2,
      "pCode": "department.group"
    },
    {
      "code": "department.group.tc",
      "codeValue": "TC",
      "codeName": "TC",
      "codeDesc": null,
      "editable": false,
      "createDt": null,
      "level": 3,
      "seqNum": 3,
      "pCode": "department.group"
    }
  ]
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
  "tagetTable": "awf_user",
  "tagetTableName": null,
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

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
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
  "tagetTable": "awf_user",
  "tagetTableName": null,
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

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/custom-codes/{customCodeId}
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
  "customCodeId": "40288a9d7db25707017db2632c700000"
}
```
