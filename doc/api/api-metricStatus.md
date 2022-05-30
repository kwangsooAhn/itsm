# 지표별 SLA 현황

지표별 SLA 현황 API이다.

## 목차

---

1. [지표별 SLA 차트 데이터](#지표별-SLA-차트-데이터)

## 지표별 SLA 차트 데이터

---

### URL 

```
GET /rest/sla/metric-status
```

### parameter Sample

```json
{
  "metricId": "40289ed28113059901811305e63a0000",
  "year": "2022",
  "chartType": "chart.basicLine"
}
```

### Response Sample

```json
{
    "status": "Z-0000",
    "message": null,
    "data": {
        "metricYears": "2022",
        "metricId": "40289ed28113059901811305e63a0000",
        "chartType": "chart.basicLine",
        "metricName": "test",
        "metricDesc": "a",
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
        "zqlString": "a"
    }
}
```
