# Token

## 목차

---

1. [탭 화면 요청](#탭(세부정보)-화면-요청)
2. [데이터 조회](#데이터-조회)
3. [데이터 추가](#데이터-추가)
4. [데이터 수정](#데이터-수정)
5. [데이터 개수](#데이터-개수)
6. [엑셀 다운로드](#엑셀-다운로드)

## 탭(세부정보) 화면 요청

---

### URL
```
GET /tokens/{tokenId}/tokenTab
```

### Response Sample

tokenTab.html

### Error

| 에러코드 | 설명 | 
|:---|:---|
|

## 데이터 조회

---

### URL
```
GET /rest/tokens/{tokenId}/data
```

### Parameter Sample

```json
{
  "tokenId": "40288a9d7da216a0017da2184ec10001"
}
```

### Response Sample

```json
{
  "token": {
    "tokenId": [
      "40288a9d7da216a0017da2184ec10001"
    ],
    "status": [
      "token.status.running"
    ],
    "action": [
      null
    ]
  },
  "instanceId": "71d6ef306bf04323a16d48363f07f45f",
  "form": {
    "id": "4028b8817cc5aed3017cc5c78e8d001d",
    "name": "10463-1",
    "status": "form.status.use",
    "desc": "10463-1",
    "category": "process",
    "display": {
      "width": "960",
      "margin": "0 0 0 0",
      "padding": "15 15 15 15"
    },
    "updateDt": [
      2021,
      10,
      28,
      7,
      24,
      10,
      374244000
    ],
    "updateUserKey": "0509e09412534a6e98f04ca79abb6424",
    "createDt": [
      2021,
      10,
      28,
      7,
      22,
      42,
      184454000
    ],
    "createUserKey": "0509e09412534a6e98f04ca79abb6424",
    "group": [
      {
        "id": "4028b8817cc5aed3017cc5c8e747002f",
        "name": "신청서작성",
        "displayType": "document.displayType.editable",
        "display": {
          "displayOrder": 0,
          "isAccordionUsed": true,
          "margin": "10 0 10 0"
        },
        "label": {
          "visibility": true,
          "fontColor": "rgba(141, 146, 153, 1)",
          "fontSize": "14",
          "bold": false,
          "italic": false,
          "underline": false,
          "align": "left",
          "text": "신청서작성"
        },
        "row": [
          {
            "id": "4028b8817cc5aed3017cc5c8e74f0030",
            "display": {
              "displayOrder": 0,
              "margin": "4 0 4 0",
              "padding": "0 0 0 0"
            },
            "component": [
              {
                "id": "a070f584854f6811854aa037c903f115",
                "type": "inputBox",
                "mapId": "1",
                "value": "",
                "isTopic": true,
                "tags": [
                  {
                    "tagId": "4028b8817cc5aed3017cc5c8e7510031",
                    "value": "1"
                  }
                ],
                "display": {
                  "displayOrder": 0,
                  "columnWidth": "12"
                },
                "label": {
                  "position": "left",
                  "fontSize": "14",
                  "fontColor": "rgba(141, 146, 153, 1)",
                  "bold": false,
                  "italic": false,
                  "underline": false,
                  "align": "left",
                  "text": "제목"
                },
                "validation": {
                  "validationType": "none",
                  "required": false,
                  "minLength": "0",
                  "maxLength": "100"
                },
                "element": {
                  "placeholder": "",
                  "columnWidth": "10",
                  "defaultValueSelect": "input|"
                }
              }
            ]
          },
          ...
        ]
      }
    ]
  },
  "stakeholders": {
    "type": "assignee.type.candidate.groups",
    "assignee": [
      "admin"
    ],
    "revokeAssignee": ""
  },
  "actions": [
    {
      "name": "common.btn.process",
      "value": "progress",
      "customYn": false
    },
    ...
  ]
}
```

## 데이터 추가

---

### URL
```
POST /rest/tokens/data
```

### Parameter Sample

```json
{
  "documentId": "4028b8817cc5aed3017cc5c9a11a0033",
  "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742",
  "tokenId": "",
  "isComplete": false,
  "assigneeId": "40288a8c7be6bdd0017be73ace110004",
  "assigneeType": "assignee.type.assignee",
  "action": "save",
  "componentData": [
    {
      "componentId": "7888ac59a85b426b921b448be9838658",
      "value": "test"
    },
    ...
  ]
}
```

### Response Sample

```
true
```

## 데이터 수정

---

### URL
```
PUT /rest/tokens/{tokenId}/data
```

### Parameter Sample

```json
{
  "instanceId": "f5ecbd947a714e8581aa13dcc4cf4742",
  "tokenId": "4028adf67dc5de46017dc5e456300003",
  "isComplete": false,
  "assigneeId": "40288a8c7be6bdd0017be73ace110004",
  "assigneeType": "assignee.type.assignee",
  "action": "save",
  "componentData": [
    {
      "componentId": "7888ac59a85b426b921b448be9838658",
      "value": "수정_TEST"
    },
    ...
  ]
}
```

### Response Sample

```
true
```

## 데이터 개수

---

### URL
```
GET /rest/tokens/todoCount
```

### Response Sample

```
8
```

## 엑셀 다운로드

---

### URL
```
GET /rest/tokens/excel
```

### Parameter Sample

```json
{
  "searchTokenType": "token.type.todo",
  "searchDocumentId": "",
  "searchValue": "수정",
  "searchFromDt": "2021-11-17T00:00:00.000Z",
  "toDt": "2021-12-17T00:00:00.000Z",
  "searchToDt": "2021-12-18T00:00:00.000Z",
  "searchTag": ""
}
```

### Response Sample

```
excel download
```
