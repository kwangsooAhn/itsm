# 보고서

보고서 템플릿에서는 사용자 정의 차트를 이용한 템플릿을 설정할 수 있다.

보고서 조회에서는 생성한 보고서를 조회할 수 있다.

해당 API는 보고서 템플릿을 추가 / 수정 / 삭제 하고, 차트 데이터 조회 / 임시 보고서를 생성하는 URL추가를 하는 API이다.

## 목차

---

1. [보고서 템플릿_데이터 추가](#보고서-템플릿-데이터-추가)
2. [보고서 템플릿_데이터 수정](#보고서-템플릿-데이터-수정)
3. [보고서 템플릿_데이터 삭제](#보고서-템플릿-데이터-삭제)
4. [보고서 템플릿_차트 데이터 조회](#보고서-템플릿-차트-데이터-조회)
5. [보고서 템플릿_임시 보고서_데이터 추가](#보고서-템플릿-임시-보고서-데이터-추가)

## 보고서 템플릿_데이터 추가

---

### URL
```
POST /rest/statistics/customReportTemplate
```

### Parameter Sample

```json
{
  "templateId": "",
  "templateName": "2022년 월간 구성관리 현황",
  "templateDesc": "월간 구성관리 등록 건수, 월간 구성관리 완료 건수, 월간 구성관리 납기 준수율을 확인할 수 있다.",
  "reportName": "2022년 월간 구성관리 현황",
  "automatic": true,
  "charts": [
    {
      "id": "4028b8817cd4629c017cd4aa4abf001b",
      "order": 1
    }
  ]
}
```

### Response Sample

```json
{
  "code": "0", 
  "status": true
}
```

## 보고서 템플릿_데이터 수정

---

### URL
```
PUT /rest/statistics/customReportTemplate/{templateId}
```

### Parameter Sample

```json
{
  "templateId": "40288a8c7e8a3577017e8a36e50c0000",
  "templateName": "2022년 연간 구성관리 현황",
  "templateDesc": "연간 구성관리 등록 건수, 월간 구성관리 완료 건수, 월간 구성관리 납기 준수율을 확인할 수 있다.",
  "reportName": "2022년 연간 구성관리 현황",
  "automatic": true,
  "charts": [
    {
      "id": "40288a8c7e898fcd017e8994c6750000",
      "order": 1
    }
  ]
}
```

### Response Sample

```json
{
  "code": "0", 
  "status": true
}
```

## 보고서 템플릿_데이터 삭제

---

### URL
```
DELETE /rest/statistics/customReportTemplate/{templateId}
```

### Parameter Sample

```json
{
  "templateId": "40288a8c7e8a3577017e8a36e50c0000"
}
```

### Response Sample

```json
{
  "code": "0", 
  "status": true
}
```

## 보고서 템플릿_차트 데이터 조회

---

### URL
```
GET /rest/statistics/customReportTemplate/charts
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
    "chartId": "4028b8817cd4629c017cd4aa4abf001b",
    "chartName": "월간 구성관리 완료건수",
    "chartType": "chart.basicLine",
    "chartDesc": "월별로 구성관리 완료 건수를 확인한다.",
    "tags": [
      {
        "tagId": "4028b21f7dbcb982017dbcc0b25f0017",
        "value": "구성관리"
      }
    ],
    "chartConfig": {
      "range": {
        "type": "chart.range.between",
        "from": "2020-12-31T15:00:00",
        "to": "2021-12-31T14:59:00"
      },
      "operation": "count",
      "periodUnit": "M",
      "documentStatus": "only.finish.document",
      "condition": ""
    },
    "chartData": [
      {
        "id": "4028b21f7dbcb982017dbcc0b25f0017",
        "category": "2020-12-31 15:00:00",
        "value": "0",
        "series": "구성관리"
      },
      ...
    ]
  }
]
```


## 보고서 템플릿_임시 보고서_데이터 추가

---

### URL
```
POST /rest/statistics/customReportTemplate/{templateId}
```

### Parameter Sample

```json
{
  "templateId": "40288a8c7e8a3577017e8a36e50c0000"
}
```

### Response Sample

```
"0"
```
