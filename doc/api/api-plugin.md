# 플러그인 관리

외부 연동을 위한 플러그인 관련 API 이다.

## 목차

---

1. [전체 데이터 조회](#전체-데이터-조회)
2. [플러그인 실행 요청](#플러그인-실행-요청)

## 전체 데이터 조회

---

### URL

```
GET /rest/plugins
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": [
    {
      "pluginId": "4028b21e7d1ab894017d1ac0bf50000d",
      "pluginName": "방화벽연동-FOCS"
    },
    {
      "pluginId": "4028b21e7d1ab894017d1ac0bf50001d",
      "pluginName": "Test-Woo"
    }]
}
```

## 플러그인 실행 요청

---

### URL

```
POST /rest/plugins/{pluginId}
```

### Parameter Sample

```json
{
  "tokenId": "",
  "pluginId": "",
  "data": [
    ["출발지","목적지","서비스","시작일", "만료일"],
    ["1.1.1.1","2.2.2.2-2.2.2.22","TCP/80","2021-12-31T15:00:00.000Z","2022-02-27T15:00:00.000Z"],
    ["1.1.1.1","2.2.2.2-2.2.2.22","UDP/80","",""],
    ["1.1.1.1","2.2.2.2-2.2.2.22","TCP/8080-8088","",""],
    ["1.1.1.1","3.3.3.0/24","TCP/80","",""],
    ["1.1.1.1","3.3.3.0/24","UDP/80","",""],
    ["1.1.1.1","3.3.3.0/24","TCP/8080-8088","",""]
  ]
}
```

### Response Sample

#### 성공시
```
{
  "status": 200,
  "message": "Success",
  "data": {
     "result": true
   }
}
```

#### 실패시
```
{
  "status": 200,
  "message": "Fail",
  "data": {
     "result": false
   }
}
```
