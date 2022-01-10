# 부서

사용자가 소속 될 수 있는 `부서의 정보`를 담고 있다.

상위 부서에서 하위 부서로 연결되어 있는 트리 형태로 구성되어 있다.  
(ex. 연구개발본부 > 개발2그룹 > ITSM)

부서 마다 각각의 기본 역할 설정이 가능하며, 이 역할 설정은 각 조직의 상세 정보 조회 화면에서 등록 및 삭제가 가능하다.

해당 API는 부서의 정보를 추가 / 조회 / 수정 / 삭제 하는 API 이다.

## 목차

---

1. [전체 데이터 조회](#전체-데이터-조회)
2. [상세 데이터 조회](#상세-데이터-조회)
3. [데이터 등록](#데이터-등록)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)

## 전체 데이터 조회

---

### URL

```
GET /rest/organizations
```

### Response Sample

```json
{
  "data": [
    {
      "organizationId": "4028b2d57d37168e017d3716cgf00000",
      "organizationName": "조직구성",
      "organizationDesc": null,
      "useYn": true,
      "level": 0,
      "seqNum": 0,
      "editable": true,
      "createUserKey": "0509e09412534a6e98f04ca79abb6424",
      "createDt": "2022-01-04T09:46:32.615042",
      "updateUserKey": null,
      "updateDt": null,
      "porganizationId": null
    },
    ...
  ],
  "totalCount": 11
}
```

## 상세 데이터 조회

---

### URL

```
GET /rest/organizations/{organizationId}
```

### Parameter Sample

```json
{
  "organizationId": "4028b2d57d37168e017d371a5f3d0006"
}
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": {
    "organizationId": "4028b2d57d37168e017d371a5f3d0006",
    "organizationName": "팀 1-1-2",
    "organizationDesc": "ffffdddd",
    "useYn": true,
    "level": 3,
    "seqNum": 3,
    "editable": true,
    "roles": [
      {
        "roleId": "faq.all",
        "roleName": "FAQ관리자",
        "roleDesc": "12341"
      },
      ...
    ],
    "porganizationId": "4028b2d57d37168e017d37197bb40001",
    "porganizationName": "그룹 1-1"
  }
}
```

## 데이터 등록

---

### URL

```
POST /rest/organizations
```

### Parameter Sample

```json
{
  "organizationId": "",
  "pOrganizationId": "40289eb27e28b4ea017e28bdd3b70002",
  "organizationName": "MD등록 예시",
  "organizationDesc": "Md확인용",
  "useYn": true,
  "seqNum": 4,
  "roleIds": [
    "faq.all", "~!@#$^&*()", "ㅅㄷㄴ"
  ]
}
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": null
}
```

## 데이터 수정

---

### URL

```
PUT /rest/organizations/{organizationId}
```

### Parameter Sample

```json
{
  "organizationId": "40288a9d7e2e0e28017e2e3233f40000",
  "pOrganizationId": "40289eb27e28b4ea017e28bdd3b70002",
  "organizationName": "md수정 예시",
  "organizationDesc": "Md확인용",
  "useYn": true,
  "seqNum": 4,
  "roleIds": [
    "users.general", "serviceDesk.manager", "serviceDesk.assignee", "service.manager"
  ]
}
```

### Response Sample

```json
{
  "status":200,
  "message":"OK",
  "data":null
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/organizations/{organizationId}
```

### Parameter Sample

```json
{
  "organizationId": "40288a9d7e2e0e28017e2e3233f40000"
}
```

### Response Sample

```json
{
  "status":200,
  "message":"OK",
  "data":null
}
```