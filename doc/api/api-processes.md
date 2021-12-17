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

### Parameter Sample

```json
{
  "process": {
    "id": "4028adf67dc1ee05017dc2037a5a0000",
    "name": "박주현_TEST2",
    "description": "",
    "status": "process.status.edit",
    "createDt": "2021-12-16T06:52:27.5639",
    "createUserKey": "40288a8c7be6bdd0017be73ace110004",
    "createUserName": "박주현",
    "updateDt": "2021-12-16T06:54:27.569537",
    "updateUserKey": "40288a8c7be6bdd0017be73ace110004",
    "updateUserName": "박주현",
    "enabled": null
  },
  "elements": [{
    "id": "a49788ef762999b81cfb00d469304ef8",
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
    "required": ["id"]
  }]
}
```

### Response Sample

```json
{
  "processId": "4028adf67dc20def017dc20fdbb90000",
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
