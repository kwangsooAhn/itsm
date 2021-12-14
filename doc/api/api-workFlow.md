# 업무흐름

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 등록](#데이터-등록)
3. [데이터 수정](#데이터-수정)
4. [데이터 삭제](#데이터-삭제)
5. [편집데이터 저장](#편집데이터-저장)

## 데이터 조회

---

### URL

```
GET /rest/workflows/{documentId}
```

### response sample

```JSON
{
  "responseCode": 200,
  "errorMessage": "OK",
  "data": [
    {
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
  ]
}
  ```

## 데이터 등록

---

### URL

```
POST /rest/workflows
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

## 데이터 수정

---

### URL

```
POST /rest/workflows/{documentId}
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
  "documentId": "40288a9d7db295d3017db2aeb7380000",
  "documentDto": {
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
    "documentIcon": ""
  },
  "isDeleteData": false
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/workflows/{documentId}
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
  "documentId": "40288a9d7db295d3017db2aeb7380000"
}
```

## 편집데이터 저장

---

### URL

```
PUT /rest/workflows/{documentId}/display
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
  "documentId": "40288a9d7db295d3017db2aeb7380000",
  "documentDisplay": {
    "documentId": "40288a9d7db295d3017db2aeb7380000",
    "display": [
      {
        "formGroupId": "4028b8817cc50161017cc531b8e50661",
        "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b8e50661",
        "elementId": "4be94f828adb4b5a938b82a25feca589",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b8e50661",
        "elementId": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b8e50661",
        "elementId": "337ab138ae9e4250b41be736e0a09c5b",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b913066b",
        "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b913066b",
        "elementId": "4be94f828adb4b5a938b82a25feca589",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b913066b",
        "elementId": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b913066b",
        "elementId": "337ab138ae9e4250b41be736e0a09c5b",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b9bb068a",
        "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b9bb068a",
        "elementId": "4be94f828adb4b5a938b82a25feca589",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b9bb068a",
        "elementId": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531b9bb068a",
        "elementId": "337ab138ae9e4250b41be736e0a09c5b",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba050699",
        "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba050699",
        "elementId": "4be94f828adb4b5a938b82a25feca589",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba050699",
        "elementId": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba050699",
        "elementId": "337ab138ae9e4250b41be736e0a09c5b",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba5406a9",
        "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba5406a9",
        "elementId": "4be94f828adb4b5a938b82a25feca589",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba5406a9",
        "elementId": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "4028b8817cc50161017cc531ba5406a9",
        "elementId": "337ab138ae9e4250b41be736e0a09c5b",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "40288ada7d0d3c49017d0d57be030026",
        "elementId": "9c7c235aa4eb43d8a912b2e524264c79",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "40288ada7d0d3c49017d0d57be030026",
        "elementId": "4be94f828adb4b5a938b82a25feca589",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "40288ada7d0d3c49017d0d57be030026",
        "elementId": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "display": "document.displayType.editable"
      },
      {
        "formGroupId": "40288ada7d0d3c49017d0d57be030026",
        "elementId": "337ab138ae9e4250b41be736e0a09c5b",
        "display": "document.displayType.editable"
      }
    ]
  }
}
```