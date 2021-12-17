# Processes


## 목차

---

1. [신규 프로세스 추가 or 다른 이름 저장](#신규-프로세스-추가-or-다른-이름-저장)
2. [프로세스_수정](#프로세스-수정)
3. [프로세스_조회](#프로세스-조회)
4. [프로세스_목록 조회](#프로세스-목록-조회)

## 신규 프로세스 추가 or 다른 이름 저장

---

### URL
```
POST /rest/processes
```

### Parameter Sample_1

* saveType = "saveas"

```json
{
  "saveType": "saveas",
  "process": {
    "id": "4028adf67dc7212d017dc72608460004",
    "name": "다른 이름으로 저장",
    "description": "",
    "status": "process.status.edit",
    "createDt": "2021-12-17T06:48:18.223869",
    "createUserKey": "0509e09412534a6e98f04ca79abb6424",
    "createUserName": "ADMIN",
    "updateDt": "2021-12-17T06:48:52.979371",
    "updateUserKey": "0509e09412534a6e98f04ca79abb6424",
    "updateUserName": "ADMIN",
    "enabled": null
  },
  "elements": [
    {
      "id": "55ecccb4fe7f42a1a5cf595b9bbe043f",
      "name": "Start",
      "type": "commonStart",
      "notification": "false",
      "description": "",
      "display": {
        "width": 40,
        "height": 40,
        "position-x": 120,
        "position-y": 200
      },
      "data": {},
      "required": [
        "id"
      ]
    }
  ]
}
```

### Response Sample_1

```json
{
  "processId": "4028adf67dc7212d017dc727bafb0005",
  "result": 1
}
```

### Parameter Sample_2

* saveType = ""

```json
{
  "saveType": "",
  "processName": "신규 프로세스 등록",
  "processDesc": ""
}
```

### Response Sample_2

```json
{
  "processId": "4028adf67dc7212d017dc723461e0001",
  "result": 1
}
```

## 프로세스_수정

---

### URL
```
POST /rest/processes/{processId}
```

### Parameter Sample

```json
{
  "processName": "박주현_TEST3",
  "processDesc": "TEST",
  "processId": "4028adf67dc20def017dc20fdbb90000",
  "processStatus": "process.status.edit"
}
```

### Response Sample

```
1
```

## 프로세스_조회

---

### URL
```
GET /rest/processes/{processId}/data
```

### Parameter Sample

```json
{
  "processId": "4028adf67dc20def017dc20fdbb90000"
}
```

### Response Sample

```json
{
  "id": "4028adf67dc20def017dc20fdbb90000",
  "name": "박주현_TEST3",
  "description": "TEST",
  "status": "process.status.edit",
  "createDt": "2021-12-16T07:05:58.913354",
  "createUserKey": "40288a8c7be6bdd0017be73ace110004",
  "createUserName": "박주현",
  "updateDt": "2021-12-16T07:20:31.682662",
  "updateUserKey": "40288a8c7be6bdd0017be73ace110004",
  "updateUserName": "박주현",
  "enabled": true
}
```

## 프로세스_목록 조회

---

### URL
```
GET /rest/processes/all
```

### Response Sample

```json
[
  {
    "id": "4028adf67dc20def017dc20fdbb90000",
    "name": "박주현_TEST3",
    "description": "TEST",
    "status": "process.status.edit",
    "createDt": "2021-12-16T07:05:58.913354",
    "createUserKey": "40288a8c7be6bdd0017be73ace110004",
    "createUserName": "박주현",
    "updateDt": "2021-12-16T07:20:31.682662",
    "updateUserKey": "40288a8c7be6bdd0017be73ace110004",
    "updateUserName": "박주현",
    "enabled": true
  },
  ...
]
```
