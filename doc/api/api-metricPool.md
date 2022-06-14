# metricPool

SLA 지표를 추가 / 수정 / 삭제하는 API이다.

## 목차

---

1. [데이터 추가](#데이터-추가)
2. [데이터 수정](#데이터-수정)
3. [데이터 삭제](#데이터-삭제)

## 데이터 추가

---

### URL

```
POST /rest/sla/metric-pools
```

### Parameter Sample

```json
{
  "metricId": "",
  "metricName": "장애점수",
  "metricDesc": "장애점수 항목 설명입니다.",
  "metricGroup": "sla.metricGroup.default",
  "metricType": "sla.metricType.auto",
  "metricUnit": "sla.metricUnit.score",
  "calculationType": "sla.calculationType.sum"
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
PUT /rest/sla/metric-pools/{metricId}
```

### Parameter Sample

```json
{
  "metricId": "40288a8c811266b501811267ebd40000",
  "metricName": "장애점수",
  "metricDesc": "장애점수 항목 설명입니다!",
  "metricGroup": "sla.metricGroup.default",
  "metricType": "자동",
  "metricUnit": "점",
  "calculationType": "합산"
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
DELETE /rest/sla/metric-pools/{metricId}
```

### Parameter Sample

```json
{
  "metricId": "40288a8c811266b501811267ebd40000"
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
