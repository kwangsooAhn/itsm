# 차트


## 목차

---

1. [데이터 추가](#데이터-추가)
2. [데이터 수정](#데이터-수정)
3. [데이터 삭제](#데이터-삭제)
4. [차트_미리보기 데이터](#차트-미리보기-데이터)
5. [차트_미리보기](#차트-미리보기)

## 데이터 추가

---

### URL
```
POST /rest/charts
```

### Parameter Sample

```json
{
  "chartId": "",
  "chartType": "chart.basicLine",
  "chartName": "박주현_TEST",
  "chartDesc": "TEST",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "Y",
    "range": {
      "type": "chart.range.between",
      "from": "2021-10-31T15:00:00.000Z",
      "to": "2021-12-15T02:17:00.000Z"
    }
  },
  "tags": [
    {
      "value": "test"
    }
  ]
}

```

### Response Sample

```
"0"
```

## 데이터 수정

---

### URL
```
PUT /rest/charts/{chartId}
```

### Parameter Sample

```json
{
  "chartId": "4028adf67dbb3a90017dbbe20cb5000a",
  "chartType": "chart.basicLine",
  "chartName": "박주현_수정",
  "chartDesc": "TEST",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "Y",
    "range": {
      "type": "chart.range.between",
      "from": "2021-10-31T15:00:00.000Z",
      "to": "2021-12-15T02:17:00.000Z"
    }
  },
  "tags": [
    {
      "value": "test"
    }
  ]
}
```

### Response Sample

```
"0"
```

## 데이터 삭제

---

### URL
```
DELETE /rest/charts/{chartId}
```

### Parameter Sample

```json
{
  "chartId": "4028adf67dbb3a90017dbbe20cb5000a"
}
```

### Response Sample

```
"0"
```

## 차트_미리보기 데이터

---

### URL
```
POST /rest/charts/{chartId}/preview
```

### Parameter Sample

```json
{
  "chartId": "4028adf67dbb3a90017dbbec7dc2000d",
  "chartType": "chart.basicLine",
  "chartName": "차트_미리보기 데이터",
  "chartDesc": "",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "Y",
    "range": {
      "type": "chart.range.between",
      "from": "2021-10-31T15:00:00.000Z",
      "to": "2021-12-15T02:28:00.000Z"
    }
  },
  "tags": [
    {
      "value": "test"
    }
  ]
}
```

### Response Sample

```json
{
  "chartId": "4028adf67dbb3a90017dbbec7dc2000d",
  "chartName": "차트_미리보기 데이터",
  "chartType": "chart.basicLine",
  "chartDesc": "",
  "tags": [
    {
      "tagId": null,
      "value": "test"
    }
  ],
  "chartConfig": {
    "range": {
      "type": "chart.range.between",
      "from": "2021-10-31T15:00:00",
      "to": "2021-12-15T02:28:00"
    },
    "operation": "count",
    "periodUnit": "Y"
  },
  "chartData": [
    {
      "id": "null",
      "category": "2021-01-01 00:00:00",
      "value": "0",
      "series": "test"
    }
  ]
}
```

## 차트_미리보기

---

### URL
```
GET /rest/charts/{chartId}
```

### Parameter Sample

```json
{
  "chartId": "4028adf67dbb3a90017dbbec7dc2000d"
}
```

### Response Sample

```json
{
  "chartId": "4028adf67dbb3a90017dbbec7dc2000d",
  "chartName": "차트_미리보기 데이터",
  "chartType": "chart.basicLine",
  "chartDesc": "",
  "tags": [
    {
      "tagId": "4028adf67dbb3a90017dbbec7dd9000e",
      "value": "test"
    }
  ],
  "chartConfig": {
    "range": {
      "type": "chart.range.between",
      "from": "2021-10-31T15:00:00",
      "to": "2021-12-15T02:28:00"
    },
    "operation": "count",
    "periodUnit": "Y"
  },
  "chartData": [
    {
      "id": "4028adf67dbb3a90017dbbec7dd9000e",
      "category": "2021-01-01 00:00:00",
      "value": "0",
      "series": "test"
    }
  ]
}
```
