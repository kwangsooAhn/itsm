# 업무흐름

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 수정](#데이터-수정)
4. [데이터 삭제](#데이터-삭제)
5. [편집데이터 저장](#편집데이터-저장)

## 데이터 조회

---

### URL

```
GET /rest/workflows/{documentId}
```

### Parameter Sample

```json
{
  "documentId": "4028b21f7c90d996017c91ae7987004f"
}
```

### response Sample

```JSON
{
  "data": {
    "documentId": "4028b21f7c90d996017c91ae7987004f",
    "documentType": "application-form",
    "documentName": "단순문의",
    "documentDesc": null,
    "documentStatus": "document.status.use",
    "processId": "4028b21f7c9698f4017c96a70ded0000",
    "formId": "4028b21f7c9698f4017c973010230003",
    "documentNumberingRuleId": "40125c91714df6c325714e053c890125",
    "documentColor": "#64BBF6",
    "documentGroup": "document.group.inquiry",
    "apiEnable": false,
    "createUserKey": "0509e09412534a6e98f04ca79abb6424",
    "createDt": "2021-11-09T13:00:41.226803",
    "updateUserKey": null,
    "updateDt": null,
    "documentIcon": "img_document_09.png"
  }
}
  ```

## 데이터 추가

---

### URL

```
POST /rest/workflows
```

### Parameter Sample

```json
{
  "documentId": "",
  "documentType": "application-form",
  "documentName": "추가 신청서",
  "documentDesc": "",
  "documentStatus": "document.status.temporary",
  "processId": "4028b21f7c81a928017c81aa9dc60000",
  "formId": "4028b8817cc50161017cc5082b460002",
  "documentNumberingRuleId": "60211d93621zd1f126241s053c890122",
  "documentColor": "#E1E9FE",
  "documentGroup": "document.group.incident",
  "apiEnable": false,
  "createUserKey": null,
  "createDt": null,
  "updateUserKey": null,
  "updateDt": null,
  "documentIcon": ""
}
```

### Response Sample

```json
{
  "return": "40288a9d7db295d3017db2aeb7380000"
}
```

## 데이터 수정

---

### URL

```
POST /rest/workflows/{documentId}
```

### Parameter Sample

```json
{
  "documentId": "40288a9d7db295d3017db2aeb7380000",
  "documentType": "application-form",
  "documentName": "추가 신청서",
  "documentDesc": "",
  "documentStatus": "document.status.temporary",
  "processId": "4028b21f7c81a928017c81aa9dc60000",
  "formId": "4028b8817cc50161017cc5082b460002",
  "documentNumberingRuleId": "60211d93621zd1f126241s053c890122",
  "documentColor": "#E1E9FE",
  "documentGroup": "",
  "apiEnable": false,
  "createUserKey": null,
  "createDt": null,
  "updateUserKey": null,
  "updateDt": null,
  "documentIcon": "",
  "isDeleteData": false
}
```

### Response Sample

```json
{
  "return": "40288a9d7db295d3017db2aeb7380000"
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/workflows/{documentId}
```

### Parameter Sample

```json
{
  "documentId": "40288a9d7db295d3017db2aeb7380000"
}
```

### Response Sample

```
{
  true
}
```

## 편집데이터 저장

---

### URL

```
PUT /rest/workflows/{documentId}/display
```

### Parameter Sample

```json
{
  "documentId": "40288a9d7db295d3017db2aeb7380000",
  "display": [
    {
      "formGroupId": "4028b8817cc50161017cc531b8e50661",
      "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
      "display": "document.displayType.editable"
    },
    ...
  ]
}
```

### Response Sample

```
{
  true
}
```
