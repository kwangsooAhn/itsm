# Process


## 목차

---

1. [프로세스_조회](#데이터-조회)
2. [프로세스_수정](#데이터-수정)
3. [프로세스_삭제](#데이터-삭제)
4. [프로세스_시뮬레이션](#프로세스-시뮬레이션)

## 프로세스_조회

---

### URL
```
GET /rest/process/{processId}/data
```

### Parameter Sample

```json
{
  "processId": "4028b21f7c9698f4017c96a70ded0000"
}
```

### Response Sample

```json
{
  "process": {
    "id": "4028b21f7c9698f4017c96a70ded0000",
    "name": "서비스데스크 - 단순문의",
    "description": "",
    "status": "process.status.use",
    "createDt": "2021-11-15T02:27:39.763675",
    "createUserKey": "0509e09412534a6e98f04ca79abb6424",
    "createUserName": "ADMIN",
    "updateDt": null,
    "updateUserKey": null,
    "updateUserName": null,
    "enabled": null
  },
  "elements": [
    {
      "id": "337ab138ae9e4250b41be736e0a09c5b",
      "name": "승인",
      "type": "userTask",
      "notification": "Y",
      "description": "",
      "display": {
        "width": 160,
        "height": 40,
        "position-x": 744,
        "position-y": 670
      },
      "data": {
        "assignee-type": "assignee.type.assignee",
        "assignee": "z-approver",
        "reject-id": "53be2caebd5e40e0b6e9ebecce3f16bd",
        "withdraw": "N"
      },
      "required": null
    },
    ...
  ]
}
```

## 프로세스_수정

---

### URL
```
PUT /rest/process/{processId}/data
```

### Parameter Sample

```json
{
  "process": {
    "id": "4028adf67dc1e2a9017dc1e337860000",
    "name": "박주현_TEST",
    "description": "",
    "status": "process.status.edit",
    "createDt": "2021-12-16T06:17:13.280415",
    "createUserKey": "40288a8c7be6bdd0017be73ace110004",
    "createUserName": "박주현",
    "updateDt": "2021-12-16T06:17:30.664724",
    "updateUserKey": "40288a8c7be6bdd0017be73ace110004",
    "updateUserName": "박주현",
    "enabled": null
  },
  "elements": [
    {
      "id": "a40b2b73160627493cb9bdd69b17de90",
      "name": "",
      "type": "arrowConnector",
      "notification": "false",
      "description": "",
      "display": {},
      "data": {
        "action-name": "",
        "action-value": "",
        "is-default": "N",
        "condition-value": "",
        "start-id": "aa986e23a9f0e6d11aa1446299852411",
        "start-name": "Start",
        "end-id": "a61d4f170d20f3a951457015a0ebe844",
        "end-name": "New Task"
      },
      "required": [
        "id",
        "start-id",
        "end-id"
      ],
      "start-id": "aa986e23a9f0e6d11aa1446299852411",
      "end-id": "a61d4f170d20f3a951457015a0ebe844"
    },
    ...
  ]
}
```

### Response Sample

```
"1"
```

## 프로세스_삭제

---

### URL
```
DELETE /rest/process/{processId}
```

### Parameter Sample

```json
{
  "processId": "4028adf67dc1e2a9017dc1e337860000"
}
```

### Response Sample

```
true
```

## 프로세스_시뮬레이션

---

### URL
```
PUT /rest/process/{processId}/simulation
```

### Parameter Sample

```json
{
  "process": {
    "id": "4028b2177c2fe099017c309c78400000",
    "name": "우다정_구성관리_복사",
    "description": "",
    "status": "process.status.edit",
    "createDt": "2021-09-29T08:12:13.1964",
    "createUserKey": "0509e09412534a6e98f04ca79abb6424",
    "createUserName": "ADMIN",
    "updateDt": "2021-10-29T01:19:37.1859",
    "updateUserKey": "0509e09412534a6e98f04ca79abb6424",
    "updateUserName": "ADMIN",
    "enabled": null
  },
  "elements": [
    {
      "id": "4432915c44784a419c58a75a4fcb9862",
      "name": "신청서 작성",
      "type": "userTask",
      "notification": "false",
      "description": "",
      "display": {
        "width": 160,
        "height": 40,
        "position-x": 320,
        "position-y": 200
      },
      "data": {
        "assignee-type": "assignee.type.candidate.groups",
        "assignee": [
          "users.general"
        ],
        "reject-id": "",
        "withdraw": "N"
      },
      "required": [
        "id",
        "name",
        "assignee-type",
        "assignee"
      ]
    },
    ...
  ]
}
```

### Response Sample

```json
{
  "success": true,
  "simulationReport": [
    {
      "success": true,
      "failedMessage": "",
      "elementId": "cf6eaaf30c704b22a0889438a13c8036",
      "elementType": "commonStart",
      "elementName": "Start"
    },
    ...
  ]
}
```
