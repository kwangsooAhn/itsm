# 사용자 정의 차트

사용자 정의 차트는 4가지 종류의 차트가 제공된다.
1. Basic Line Chart
2. Pie Chart
3. Stacked Column Chart 
4. Stacked Bar Chart

연산 방법으로는 카운트, 평균, 퍼센트 3가지 방법이 제공되며

'조건식' 항목에는 간단한 조건문을 추가할 수 있다.

'대상 문서 상태' 옵션에 따라 차트의 문서 범위를 '완료된 문서' 혹은 '진행중인 문서' 까지 대상으로 지정할 수 있다.

해당 API는 차트를 추가 / 수정 / 삭제 / 미리보기 / 미리보기 데이터 확인을 하는 API이다.

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
POST rest/statistics/customChart
```

### Parameter Sample

```json
{
  "chartId": "",
  "chartType": "chart.basicLine",
  "chartName": "박주현_차트TEST",
  "chartDesc": "박주현_차트TEST",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "M",
    "range": {
      "type": "chart.range.between",
      "from": "2021-11-30T15:00:00.000Z",
      "to": "2022-01-20T14:59:00.000Z"
    },
    "documentStatus": "even.running.document",
    "condition": ""
  },
  "tags": [
    {
      "value": "단순문의"
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
PUT /rest/statistics/customChart/{chartId}
```

### Parameter Sample

```json
{
  "chartId": "40288a8c7e75c923017e75f047be0000",
  "chartType": "chart.basicLine",
  "chartName": "박주현_차트_수정TEST",
  "chartDesc": "박주현_차트_수정TEST",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "M",
    "range": {
      "type": "chart.range.last.month",
      "from": "2021-11-30T15:00:00.000Z",
      "to": "2021-12-31T14:59:00.000Z"
    },
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "tags": [
    {
      "value": "단순문의"
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
DELETE /rest/statistics/customChart/{chartId}
```

### Parameter Sample

```json
{
  "chartId": "40288a8c7e75c923017e75f047be0000"
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
POST /rest/statistics/customChart/{chartId}/preview
```

### Parameter Sample

```json
{
  "chartId": "40288a8c7e75c923017e75f9f6fc0003",
  "chartType": "chart.basicLine",
  "chartName": "박주현_차트_미리보기데이터TEST",
  "chartDesc": "박주현_차트_미리보기데이터TEST",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "Y",
    "range": {
      "type": "chart.range.between",
      "from": "2021-11-30T15:00:00.000Z",
      "to": "2022-01-20T14:59:00.000Z"
    },
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "tags": [
    {
      "value": "장애신고"
    }
  ]
}
```

### Response Sample

```json
{
  "chartId": "40288a8c7e75c923017e75f9f6fc0003",
  "chartName": "박주현_차트_미리보기데이터TEST",
  "chartType": "chart.basicLine",
  "chartDesc": "박주현_차트_미리보기데이터TEST",
  "tags": [
    {
      "tagId": null,
      "value": "장애신고"
    }
  ],
  "chartConfig": {
    "range": {
      "type": "chart.range.between",
      "from": "2021-11-30T15:00:00",
      "to": "2022-01-20T14:59:00"
    },
    "operation": "count",
    "periodUnit": "Y",
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "chartData": [
    {
      "id": "null",
      "category": "2021-11-30 15:00:00",
      "value": "0",
      "series": "장애신고"
    }
  ]
}
```

## 차트_미리보기

---

### URL
```
GET /rest/statistics/customChart/{chartId}
```

### Parameter Sample

```json
{
  "chartId": "40288a8c7e75c923017e75f9f6fc0003"
}
```

### Response Sample

```json
{
  "chartId": "40288a8c7e75c923017e75f9f6fc0003",
  "chartName": "박주현_차트_미리보기데이터TEST",
  "chartType": "chart.basicLine",
  "chartDesc": "박주현_차트_미리보기데이터TEST",
  "tags": [
    {
      "tagId": "40288a8c7e75c923017e75f9f7080004",
      "value": "장애신고"
    }
  ],
  "chartConfig": {
    "range": {
      "type": "chart.range.between",
      "from": "2021-11-30T15:00:00",
      "to": "2022-01-20T14:59:00"
    },
    "operation": "count",
    "periodUnit": "Y",
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "chartData": [
    {
      "id": "40288a8c7e75c923017e75f9f7080004",
      "category": "2021-11-30 15:00:00",
      "value": "0",
      "series": "장애신고"
    }
  ]
}
```
