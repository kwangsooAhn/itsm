# Numbering Pattern


## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 상세 조회](#데이터-상세-조회)
3. [데이터 추가](#데이터-추가)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)

## 데이터 조회

---

### URL
```
GET /rest/numberingPatterns
```

### Response Sample

```json
[
  {
    "patternId": "2c9180837cb3a706017cb4e3ad940000",
    "patternName": "테스트4",
    "patternType": "date",
    "patternValue": "yyyyMMdd"
  },
  ...
]
```

## 데이터 상세 조회

---

### URL
```
GET /rest/numberingPatterns/{patternId}
```

### Parameter Sample

```json
{
  "patternId": "2c9180837cb3a706017cb4e3ad940000"
}
```

### Response Sample

```json
{
  "patternId": "2c9180837cb3a706017cb4e3ad940000",
  "patternName": "테스트4",
  "patternType": "numbering.pattern.date",
  "patternValue": "yyyyMMdd",
  "editable": true
}
```

## 데이터 추가

---

### URL
```
POST /rest/numberingPatterns
```

### Parameter Sample

```json
{
  "patternId": "",
  "patternName": "박주현_TEST",
  "patternType": "numbering.pattern.text",
  "patternValue": "TEST"
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
PUT /rest/numberingPatterns/{patternId}
```

### Parameter Sample

```json
{
  "patternId": "4028adf67dc0690b017dc11989590005",
  "patternName": "박주현_수정_TEST",
  "patternType": "numbering.pattern.text",
  "patternValue": "TEST"
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
DELETE /rest/numberingPatterns/{patternId}
```

### Parameter Sample

```json
{
  "patternId": "4028adf67dc0690b017dc11989590005"
}
```

### Response Sample

```
"0"
```