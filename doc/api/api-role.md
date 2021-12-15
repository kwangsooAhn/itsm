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
  "responseCode": 200,
  "errorMessage": "OK",
  "data": [
    {
      "roleId": "faq.all",
      "roleName": "FAQ관리자",
      "roleDesc": null
    },
    {
      "roleId": "board.admin.manager",
      "roleName": "게시판 관리자",
      "roleDesc": "게시판 관리자"
    },
    {
      "roleId": "notice.all",
      "roleName": "공지사항 관리자",
      "roleDesc": null
    },
    {
      "roleId": "notice.view",
      "roleName": "공지사항 사용자",
      "roleDesc": null
    },
    {
      "roleId": "admin",
      "roleName": "관리자",
      "roleDesc": "전체관리자"
    },
    {
      "roleId": "configuration.change.manager",
      "roleName": "구성관리 관리자",
      "roleDesc": null
    },
    {
      "roleId": "configuration.change.assignee",
      "roleName": "구성관리 담당자",
      "roleDesc": null
    },
    {
      "roleId": "auth.all",
      "roleName": "권한 관리자",
      "roleDesc": null
    },
    {
      "roleId": "document",
      "roleName": "문서처리",
      "roleDesc": null
    },
    {
      "roleId": "document.manager",
      "roleName": "문서처리 관리자",
      "roleDesc": null
    },
    {
      "roleId": "problem.manager",
      "roleName": "문제관리 관리자",
      "roleDesc": null
    },
    {
      "roleId": "problem.assignee",
      "roleName": "문제관리 담당자",
      "roleDesc": null
    },
    {
      "roleId": "users.manager",
      "roleName": "사용자관리자",
      "roleDesc": null
    },
    {
      "roleId": "users.general",
      "roleName": "사용자일반",
      "roleDesc": null
    },
    {
      "roleId": "serviceDesk.manager",
      "roleName": "서비스데스크 관리자",
      "roleDesc": null
    },
    {
      "roleId": "serviceDesk.assignee",
      "roleName": "서비스데스크 담당자",
      "roleDesc": null
    },
    {
      "roleId": "application.change.manager",
      "roleName": "어플리케이션 변경 관리자",
      "roleDesc": null
    },
    {
      "roleId": "application.change.assignee",
      "roleName": "어플리케이션 변경 담당자",
      "roleDesc": null
    },
    {
      "roleId": "role.all",
      "roleName": "역할 관리자",
      "roleDesc": null
    },
    {
      "roleId": "role.view",
      "roleName": "역할 사용자",
      "roleDesc": null
    },
    {
      "roleId": "infra.change.manager",
      "roleName": "인프라 변경 관리자",
      "roleDesc": null
    },
    {
      "roleId": "infra.change.assignee",
      "roleName": "인프라 변경 담당자",
      "roleDesc": null
    },
    {
      "roleId": "incident.manager",
      "roleName": "장애 관리자",
      "roleDesc": null
    },
    {
      "roleId": "incident.assignee",
      "roleName": "장애 담당자",
      "roleDesc": null
    }
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

```json
{
  "roleId": "document.manager"
}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
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
    "0": "document.read.admin",
    "1": "role.create",
    "2": "document.read.admin"
  },
  "arrAuthList": null,
  "userRoleMapCount": 0
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
    "0": "cmdb.attribute.create",
    "1": "cmdb.ci.update",
    "2": "cmdb.attribute.read"
  },
  "arrAuthList": null,
  "userRoleMapCount": 0
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
DELETE /rest/roles/{roleId}
```

### Parameter Sample

```json
{
  "roleId": "role.test"
}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
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
  "responseCode": 200,
  "errorMessage": "OK"
}
```