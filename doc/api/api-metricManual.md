# 수동 지표 관리

SLA 수동지표를 추가 / 삭제 하는 API이다.

## 목차

---

1. [데이터 등록](#데이터-등록)
2. [데이터 삭제](#데이터-삭제)

## 데이터 등록

---

### URL

```
POST /rest/sla/metric-manuals
```

### Parameter Sample

```json
{
  "metricId": "40288a9d8112d5ec0181132c46190002",
  "referenceDate": "2022-05-30",
  "metricValue": "20.0"
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
DELETE /rest/sla/metric-manuals/{metricManualId}
```

### Parameter Sample

```json
{
  "metricManualId": "40288a9d81134c710181134cdda50000"
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
