# 대시보드

## 목차

---

1. [데이터 조회](#데이터-조회)

## 데이터 조회

---

### URL

```
GET /rest/dashboard/statistic
```

### response sample

```json
{
  "data": [
    {
      "type": "todo",
      "total": 91,
      "incident": 3,
      "inquiry": 0,
      "request": 0,
      "etc": 88
    },
    {
      "type": "running",
      "total": 4,
      "incident": 0,
      "inquiry": 0,
      "request": 0,
      "etc": 4
    },
    {
      "type": "monthDone",
      "total": 31,
      "incident": 0,
      "inquiry": 0,
      "request": 0,
      "etc": 31
    },
    {
      "type": "done",
      "total": 150,
      "incident": 1,
      "inquiry": 0,
      "request": 0,
      "etc": 149
    }
  ]
}
```