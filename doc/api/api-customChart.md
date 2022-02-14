# 사용자 정의 차트

사용자 정의 차트는 3가지 종류의 차트가 제공된다.
1. Basic Line Chart
2. Pie Chart
3. Stacked Column Chart

연산 방법으로는 카운트, 평균, 퍼센트 3가지 방법이 제공되며 '조건식' 항목에는 간단한 조건문을 추가할 수 있다.

조건식의 표기법은 아래와 같다.
1. +, -, *, / 같은 산술연산자중 사칙연산과 논리연산자인 &&, || 와 비교 연산자인 ==, !=, <, >, <=, >= 를 지원한다.
2. 태그를 표현하기 위해 [] 를 사용한다. 만약 태그명에 “[“나 “]”가 들어가 있다면 “\\]”식으로 표현해야 한다.
3. 논리연산의 우선순위 표기를 위해 “()”가 사용이 가능하다. 역시나 태그명에 소괄호가 사용된다면 “\\)”와 같이 태그명을 작성한다.

퍼센트 연산의 경우 조건식에 따라 처리 과정이 다르다.
1. 조건식이 있는 경우 : 각 태그별로 전체 개수를 100%로 잡고 조건을 만족하는 케이스를 비율로 계산한다.
2. 조건식이 없는 경우 : 태그의 전체 개수가 100%가 되고 각 태그의 비율을 계산한다.

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
  "chartName": "월간 구성관리 완료 건수",
  "chartDesc": "월별로 구성관리 완료 건수를 확인한다.",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "M",
    "range": {
      "type": "chart.range.between",
      "from": "2020-12-31T15:00:00.000Z",
      "to": "2021-12-31T14:59:00.000Z"
    },
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "tags": [
    {
      "value": "구성관리"
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
  "chartId": "40288a8c7e898fcd017e8994c6750000",
  "chartType": "chart.basicLine",
  "chartName": "연간 구성관리 완료 건수",
  "chartDesc": "연간 구성관리 완료 건수를 확인한다.",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "Y",
    "range": {
      "type": "chart.range.between",
      "from": "2020-12-31T15:00:00.000Z",
      "to": "2021-12-31T14:59:00.000Z"
    },
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "tags": [
    {
      "value": "구성관리"
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
  "chartId": "40288a8c7e898fcd017e8994c6750000",
  "chartType": "chart.basicLine",
  "chartName": "연간 구성관리 완료 건수",
  "chartDesc": "연간 구성관리 완료 건수를 확인한다.",
  "chartConfig": {
    "operation": "count",
    "periodUnit": "Y",
    "range": {
      "type": "chart.range.between",
      "from": "2020-12-31T15:00:00.000Z",
      "to": "2021-12-31T14:59:00.000Z"
    },
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "tags": [
    {
      "value": "구성관리"
    }
  ]
}
```

### Response Sample

```json
{
  "chartId": "40288a8c7e898fcd017e8994c6750000",
  "chartName": "연간 구성관리 완료 건수",
  "chartType": "chart.basicLine",
  "chartDesc": "연간 구성관리 완료 건수를 확인한다.",
  "tags": [
    {
      "tagId": null,
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
    "periodUnit": "Y",
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "chartData": [
    {
      "id": "null",
      "category": "2020-12-31 15:00:00",
      "value": "0",
      "series": "구성관리"
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
  "chartId": "40288a8c7e898fcd017e8994c6750000"
}
```

### Response Sample

```json
{
  "chartId": "40288a8c7e898fcd017e8994c6750000",
  "chartName": "연간 구성관리 완료 건수",
  "chartType": "chart.basicLine",
  "chartDesc": "연간 구성관리 완료 건수를 확인한다.",
  "tags": [
    {
      "tagId": "40288a8c7e898fcd017e899b973c0002",
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
    "periodUnit": "Y",
    "documentStatus": "only.finish.document",
    "condition": ""
  },
  "chartData": [
    {
      "id": "40288a8c7e898fcd017e899b973c0002",
      "category": "2020-12-31 15:00:00",
      "value": "0",
      "series": "구성관리"
    }
  ]
}
```
