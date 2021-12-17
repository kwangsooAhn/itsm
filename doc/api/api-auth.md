# 권한

## 목차

---

1. [데이터 목록 조회](#데이터-목록-조회)
2. [데이터 상세 조회](#데이터-상세-조회)
3. [데이터 등록](#데이터-등록)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)

## 데이터 목록 조회

---

### URL

```
GET /rest/auths
```

### Response Sample

```json
{
  "data": [
    {
      "authId": "cmdb.attribute.update",
      "authName": "CMDB Attribute 변경",
      "authDesc": "CMDB Attribute 변경 권한"
    },
    ...
  ],
  "paging": {
    "totalCount": 104,
    "totalCountWithoutCondition": 104,
    "currentPageNum": 0,
    "totalPageNum": 0,
    "orderType": null
  }
}

```

## 데이터 상세 조회

---

### URL

```
GET /rest/auths/{authId}
```

### Parameter Sample

```json
{
  "authId": "cmdb.attribute.update"
}
```

### Response Sample

```json
{
  "authId": "cmdb.attribute.update",
  "authName": "CMDB Attribute 변경",
  "authDesc": "CMDB Attribute 변경 권한",
  "arrMenuId": null,
  "arrMenuList": [
    {
      "authId": "cmdb.attribute.update",
      "menuId": "cmdb"
    },
    {
      "authId": "cmdb.attribute.update",
      "menuId": "cmdb.attribute"
    }
  ],
  "arrUrl": null,
  "arrUrlList": [
    {
      "url": "cmdb/attribute/{id}/edit",
      "method": "get",
      "authId": "cmdb.attribute.update"
    },
    ...
  ],
  "roleAuthMapCount": 1
}
```

## 데이터 등록

### URL

```
POST /rest/auths
```

### Parameter Sample

```json
{
  "authId": "test.auths",
  "authName": "테스트 새 권한",
  "authDesc": "등록 및 수정, 삭제 테스트 데이터",
  "arrMenuId": {
    0: "board",
    1: "cmdb"
  },
  "arrMenuList": null,
  "arrUrl": {
    0: "/rest/boards/articles/comments/{id}",
    1: "/rest/boards/articles/{id}",
    2: "/rest/boards/{id}"
  },
  "arrUrlList": null,
  "roleAuthMapCount": 0
}
```

### Response Sample

```
{
  "test.auths"
}
```

## 데이터 수정

---

### URL

```
PUT /rest/auths/{authId}
```

### Parameter Sample

```json
{
  "authId": "test.auths",
  "authName": "테스트 권한 제목 수정",
  "authDesc": "등록 및 수정, 삭제 테스트 데이터",
  "arrMenuId": {
    0: "board",
    1: "cmdb",
    2: "cmdb.attribute",
    3: "dashboard"
  },
  "arrMenuList": null,
  "arrUrl": {
    0: "/rest/boards/articles/comments/{id}",
    1: "/rest/boards/articles/{id}",
    2: "/dashboard/view"
  },
  "arrUrlList": null,
  "roleAuthMapCount": 0
}
```

### Response Sample

```
{
  "test.auths"
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/auths/{authId}
```

### Parameter Sample

```json
{
  "authId": "test.auths"
}
```

### Response Sample
```
{
  "true"
}
```