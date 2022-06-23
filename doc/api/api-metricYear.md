# metricYear

SLA 연도별 지표를 추가 / 수정 / 삭제 / 복사 / 조회 / 엑셀 다운로드하는 API이다.

## 목차

---

1. [데이터 추가](#데이터-추가)
2. [데이터 수정](#데이터-수정)
3. [데이터 삭제](#데이터-삭제)
4. [데이터 복사](#데이터-복사)
5. [엑셀 다운로드](#엑셀-다운로드)
6. [미리보기 차트](#미리보기-차트)

## 데이터 추가

---

### URL

```
POST /rest/sla/metrics
```

### Parameter Sample

```json
{
  "metricId": "40288a8c81129328018112974d660000",
  "year": "2022",
  "minValue": 6,
  "maxValue": 9,
  "weightValue": 20,
  "owner": "담당자",
  "comment": "장애등급에 따른 점수",
  "zqlString": ""
}
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 데이터 수정

---

### URL

```
PUT /rest/sla/metrics
```

### Parameter Sample

```json
{
  "metricId": "40288a8c81129328018112974d660000",
  "year": "2022",
  "minValue": 5,
  "maxValue": 10,
  "weightValue": 20,
  "owner": "담당자",
  "comment": "장애등급에 따른 점수",
  "zqlString": ""
}
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/sla/metrics/{metricId}/{year}
```

### Parameter Sample

```json
{
  "metricId": "40288a8c811266b501811267ebd40000",
  "year": "2022"
}
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 데이터 복사

---

### URL

```
POST /rest/sla/metrics/copy
```

### Parameter Sample

```json
{
  "metricId": "40289ed28112953401811296bc2e0000",
  "source": "2021",
  "target": "2023"
}
```

### Response Sample

```json
{
  "status": "Z-0000",
  "message": null,
  "data": null
}
```

## 엑셀 다운로드

---

### URL

```
GET /rest/sla/metrics/annual/excel
```

### Parameter Sample

```
{
  metricYearSearchCondition: MetricYearSearchCondition
}
```

### Response Sample

```json
{
  "status": "200 OK",
  "headers": {
    "Content-Disposition": "attachment; filename=7b9b5eb6dc744c898025bb1b8f69ba04",
    "Content-Type": "application/vnd.ms-excel"
  },
  "body": ...
}
```


## 미리보기 차트

---

### URL

```
GET /rest/sla/metrics/{metricId}/preview
```

### Parameter Sample

```json
{
  "metricId": "40289ed28113059901811305e63a0000",
  "year": "2022"
}
```

### Response Sample

```json
{
    "status": "Z-0000",
    "message": null,
    "data": {
        "metricYears": "2022",
        "metricId": "40288a8c81129328018112974d660000",
        "chartType": "chart.basicLine",
        "metricName": "장애점수",
        "metricDesc": "장애점수 항목 설명입니다.",
        "tag": [],
        "chartConfig": {
            "range": {
                "type": "chart.range.between",
                "fromDate": "2022-01-01",
                "toDate": "2022-12-31",
                "fromDateTime": "2022-01-01T00:00:00",
                "toDateTime": "2022-12-31T23:59:59",
                "fromDateTimeUTC": null,
                "toDateTimeUTC": null
            },
            "operation": "count",
            "periodUnit": "M",
            "documentStatus": "only.finish.document",
            "condition": null
        },
        "chartData": [],
        "zqlString": "(([incident_level] == 3)? 10:0) + (([incident_level] == 2)? 20:0) + (([incident_level] == 1)? 10:0) "
    }
}
```
