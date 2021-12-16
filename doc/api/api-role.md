# 역할

## 목차

---

1. [데이터 목록 조회](#데이터-목록-조회)
2. [데이터 상세 조회](#데이터-상세-조회)
3. [데이터 등록](#데이터-등록)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)
6. [데이터 다운로드](#데이터-다운로드)


## 데이터 목록 조회

### URL

```
GET /rest/roles
```

### Response Sample

```json
{
  "data": [
    {
      "roleId": "faq.all",
      "roleName": "FAQ관리자",
      "roleDesc": null
    },
    ...
  ],
  "paging": {
    "totalCount": 24,
    "totalCountWithoutCondition": 24,
    "currentPageNum": 0,
    "orderType": null
  }
}
```

## 데이터 상세 조회

---

### URL

```
GET /rest/roles/{roleId}
```

### Parameter Sample

```
{
  "document.manager"
}
```

### Response Sample

```json
{
  "data": {
    "roleId": "document.manager",
    "roleName": "문서처리 관리자",
    "roleDesc": null,
    "createUserName": "ADMIN",
    "createDt": "2021-12-09T15:51:18.606923",
    "updateUserName": null,
    "updateDt": null,
    "arrAuthList": [
      {
        "authId": "action.cancel",
        "cuthName": "문서 취소",
        "authDesc": null
      },
      {
        "authId": "action.terminate",
        "cuthName": "문서 종결",
        "authDesc": null
      }
    ],
    "userRoleMapCount": 0
  }
}
```

## 데이터 등록

---

### URL

```
POST /rest/roles
```

### Parameter Sample

```json
{
  "roleId": "role.test",
  "roleName": "역할 등록,수정,삭제 테스트",
  "roleDesc": "테스트용 ",
  "createUserName": null,
  "createDt": null,
  "updateUserName": null,
  "updateDt": null,
  "arrAuthId": {
    0: "document.read.admin",
    1: "role.create",
    2: "document.read.admin"
  },
  "arrAuthList": null,
  "userRoleMapCount": 0
}
```

### Response Sample

```
{
  "role.test"
}
```

## 데이터 수정

---

### URL

```
POST /rest/roles/{roleId}
```

### Parameter Sample

```json
{
  "roleId": "role.test",
  "roleName": "역할 등록,수정,삭제 테스트",
  "roleDesc": "수정",
  "createUserName": null,
  "createDt": null,
  "updateUserName": null,
  "updateDt": null,
  "arrAuthId": {
    0: "cmdb.attribute.create",
    1: "cmdb.ci.update",
    2: "cmdb.attribute.read"
  },
  "arrAuthList": null,
  "userRoleMapCount": 0
}
```

### Response Sample

```
{
  "role.test"
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/roles/{roleId}
```

### Parameter Sample

```
{
  role.test"
}
```

### Response Sample

```
{
  "true"
}
```

## 데이터 다운로드

---

### URL

```
GET /rest/roles/excel
```

### Response Sample

```json
{
  "status": "200 OK",
  "header": {
    "Content-Type": "application/vnd.ms-excel",
    "Content-Disposition": "attachment; filename=6203f83ee98d47c8a967926ed312086d"
  },
  "body": "[80, 75, 3, 4, 20, 0, 8, 8, 8, 0, -38, -71, -113, 83, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 19, 0, 0, 0, 91, 67, 111, 110, 116, 101, 110, 116, 95, 84, 121, 112, 101, 115, 93, 46, 120, 109, 108, -75, 83, -53, 110, -62, 48, 16, -4, -107, -56, -41, 42, 54, -12, 80, 85, 21, -127, 67, 31, -57, 22, -87, -12, 3, 92, 123, -109, 88, -8, 37, -81, -95, -16, -9, 93, 7, 56, -108, 82, -119, 10, 113, -14, 99, 102, 103, 102, 87, -10, 100, +4,270 more]"
}
```