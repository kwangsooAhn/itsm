# 보고서


## 목차

---

1. [차트_데이터 조회](#차트-데이터-조회)
2. [보고서 템플릿_데이터 추가](#보고서-템플릿-데이터-추가)
3. [보고서 템플릿_데이터 수정](#보고서-템플릿-데이터-수정)
4. [보고서 템플릿_데이터 삭제](#보고서-템플릿-데이터-삭제)
5. [임시 보고서_데이터 추가](#임시-보고서-데이터-추가)

## 차트_데이터 조회

---

### URL
```
GET /rest/reports/template/charts
```

### Parameter Sample

```json
{
  "chartId": "4028b21e7d985593017d985683d60000"
}
```

### Response Sample

```json
[
  {
    "chartId": "4028b21e7d985593017d985683d60000",
    "chartName": "차트 테스트",
    "chartType": "chart.basicLine",
    "chartDesc": "",
    "tags": [
      {
        "tagId": "4028b21e7d9c95a6017d9c96fed60000",
        "value": "업무흐름1"
      },
     ...
    ],
    "chartConfig": {
      "range": {
        "type": "chart.range.between",
        "from": "2021-11-08T15:00:00",
        "to": "2021-12-09T14:59:59"
      },
      "operation": "count",
      "periodUnit": "Y"
    },
    "chartData": [
      {
        "id": "4028b21e7d9c95a6017d9c96fed60000",
        "category": "2021-01-01 00:00:00",
        "value": "1",
        "series": "업무흐름1"
      },
      ...
    ]
  }
]
```

## 보고서 템플릿_데이터 추가

---

### URL
```
POST /rest/reports/template
```

### Parameter Sample

```json
{
  "templateId": "",
  "templateName": "템플릿_추가_TEST",
  "templateDesc": "",
  "reportName": "",
  "automatic": false,
  "charts": [
    {
      "id": "4028adf67dc0690b017dc0d266560001",
      "order": 1
    }
  ]
}
```

### Response Sample

```json
{
  "code": "0", 
  "status":true
}
```

## 보고서 템플릿_데이터 수정

---

### URL
```
PUT /rest/reports/template/{templateId}
```

### Parameter Sample

```json
{
  "templateId": "4028adf67dc0690b017dc0d4263a0003",
  "templateName": "템플릿_수정_TEST",
  "templateDesc": "",
  "reportName": "",
  "automatic": false,
  "charts": [
    {
      "id": "4028adf67dc0690b017dc0d266560001",
      "order": 1
    }
  ]
}
```

### Response Sample

```json
{
  "code": "0", 
  "status":true
}
```

## 보고서 템플릿_데이터 삭제

---

### URL
```
DELETE /rest/reports/template/{templateId}
```

### Parameter Sample

```json
{
  "templateId": "4028adf67dc0690b017dc0d4263a0003"
}
```

### Response Sample

```json
{
  "code": "0", 
  "status":true
}
```

## 임시 보고서_데이터 추가

---

### URL
```
POST /rest/reports/template/{templateId}
```

### Parameter Sample

```json
{
  "templateId": "4028adf67dc0690b017dc0d4263a0003"
}
```

### Response Sample

```
"0"
```
