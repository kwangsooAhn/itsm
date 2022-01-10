# 스케줄러


## 목차

---

1. [데이터 등록](#데이터-등록)
2. [데이터 수정](#데이터-수정)
3. [데이터 삭제](#데이터-삭제)
4. [데이터 즉시 실행](#데이터-즉시-실행)

## 데이터 등록

---

### URL

```
POST /rest/schedulers
```

### Parameter Sample

```json
{
  "taskId": null,
  "taskName": "테스트 스케줄러",
  "taskDesc": "등록 및 수정, 삭제 테스트 데이터",
  "taskType": "query",
  "useYn": true,
  "executeClass": null,
  "executeQuery": "select * from awf_user",
  "executeCommand": null,
  "executeCycleType": "fixedDelay",
  "executeCyclePeriod": 1000000000,
  "cronExpression": null,
  "editable": null,
  "args": null,
  "src": null
}
```

### Response Sample

```
{
  "0"
}
```

## 데이터 수정

### URL

```
PUT /rest/schedulers/{taskId}
```

### Parameter Sample

```json
{
  "taskId": "40288a9d7dc0f444017dc10fe6270000",
  "taskName": "테스트 스케줄러 수정",
  "taskDesc": "등록 및 수정, 삭제 테스트 데이터",
  "taskType": "query",
  "useYn": false,
  "executeClass": null,
  "executeQuery": "select * from awf_user",
  "executeCommand": null,
  "executeCycleType": "fixedDelay",
  "executeCyclePeriod": 1000000000,
  "cronExpression": null,
  "editable": null,
  "args": null,
  "src": null
}
```

### Response Sample

```
{
 "0" 
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/schdulers/{taskId}
```

### Parameter Sample

```json
{
  "taskId": "40288a9d7dc0f444017dc18fccc80002"
}
```

### Response Sample

```
{
  "0"
}
```

## 데이터 즉시 실행

---

### URL

```
POST /rest/schdulers/{taskId}/execute
```

### Parameter Sample

```json
{
  "taskId": "40288a9d7dc0f444017dc10fe6270000",
  "taskName": "테스트 스케줄러",
  "taskDesc": "등록 및 수정, 삭제 테스트 데이터",
  "taskType": "query",
  "useYn": false,
  "executeClass": null,
  "executeQuery": "select * from awf_user",
  "executeCommand": null,
  "executeCycleType": "fixedDelay",
  "executeCyclePeriod": 1000000000,
  "cronExpression": null,
  "editable": null,
  "args": null,
  "src": null
}
```

### Response Sample

```
{
  "0"
}
```