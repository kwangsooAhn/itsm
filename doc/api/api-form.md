# Form


## 목차

---

1. [문서양식_일반정보 조회](#문서양식-일반정보-조회)
2. [문서양식_전체 데이터 조회](#문서양식-전체-데이터-조회)
3. [신규 문서양식 추가](#신규-문서양식-추가)
4. [문서양식_일반정보 저장](문서양식-일반정보-저장)
5. [문서양식_전체 데이터 저장](#문서양식-전체-데이터-저장)
6. [문서양식_데이터 삭제](#문서양식-데이터-삭제)


## 문서양식_일반정보 조회

---

### URL
```
GET /rest/forms/{formId}
```

### Parameter Sample

```json
{
  "formId": "4028b2267d9a7065017d9a76bc3c0000"
}
```

### Response Sample

```json
{
  "id": "4028b2267d9a7065017d9a76bc3c0000",
  "name": "#11820",
  "status": "form.status.edit",
  "desc": "DR 컴포넌트 오류",
  "display": {},
  "category": "",
  "editable": true,
  "createUserKey": "0509e09412534a6e98f04ca79abb6424",
  "createUserName": "ADMIN",
  "createDt": "2021-12-08T14:33:32.457781",
  "updateUserKey": "0509e09412534a6e98f04ca79abb6424",
  "updateUserName": "ADMIN",
  "updateDt": "2021-12-08T14:33:48.465645"
}
```

## 문서양식_전체 데이터 조회

---

### URL
```
GET /rest/forms/{formId}/data
```

### Parameter Sample

```json
{
  "formId": "4028b2267d9a7065017d9a76bc3c0000"
}
```

### Response Sample

```json
{
  "id": "4028b2267d9a7065017d9a76bc3c0000",
  "name": "#11820",
  "status": "form.status.edit",
  "desc": "DR 컴포넌트 오류",
  "category": "process",
  "display": {
    "width": "960",
    "margin": "0 0 0 0",
    "padding": "15 15 15 15"
  },
  "updateDt": "2021-12-16T05:34:57.047334",
  "updateUserKey": "40288a8c7be6bdd0017be73ace110004",
  "createDt": "2021-12-08T14:33:32.457781",
  "createUserKey": "0509e09412534a6e98f04ca79abb6424",
  "group": [
    {
      "id": "a616338c9083475b051f8bb1cd8de9e1",
      "name": "",
      "display": {
        "displayOrder": 0,
        "isAccordionUsed": true,
        "margin": "10 0 10 0"
      },
      "label": {
        "visibility": true,
        "fontColor": "#8d9299",
        "fontSize": "14",
        "bold": false,
        "italic": false,
        "underline": false,
        "align": "left",
        "text": "GROUP LABEL"
      },
      "row": [
        {
          "id": "a10028a16b1b7a45e3d976fa1d210a5c",
          "display": {
            "displayOrder": 0,
            "margin": "4 0 4 0",
            "padding": "0 0 0 0"
          },
          "component": [
            {
              "id": "a77349f2e8dd8ecd03b702de429554e1",
              "type": "inputBox",
              "mapId": "",
              "isTopic": false,
              "tags": [],
              "display": {
                "displayOrder": 0,
                "columnWidth": "12"
              },
              "label": {
                "position": "left",
                "fontSize": "14",
                "fontColor": "#8d9299",
                "bold": false,
                "italic": false,
                "underline": false,
                "align": "left",
                "text": "COMPONENT LABEL"
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
        }
      ]
    }
  ]
}
```

## 신규 문서양식 추가

---

### URL
```
POST /rest/forms
```

### Parameter Sample

```json
{
  "id": "4028adf67dc18b72017dc1c141560001",
  "name": "박주현_TEST3",
  "desc": "",
  "status": "form.status.edit",
  "category": "process",
  "display": {
    "width": "960",
    "margin": "0 0 0 0",
    "padding": "15 15 15 15"
  },
  "group": []
}
```

### Response Sample

```
"4028adf67dc18b72017dc1c235ba0003"
```

## 문서양식_일반정보 저장

---

### URL
```
PUT /rest/forms/{formId}
```

### Parameter Sample

```json
{
  "name": "TEST_1",
  "desc": "",
  "id": "4028adf67dc18b72017dc1c6ff1d0004",
  "status": "form.status.edit"
}
```

### Response Sample

```
"0"
```

## 문서양식_전체 데이터 저장

---

### URL
```
PUT /rest/forms/{formId}/data
```

### Parameter Sample

```json
{
  "id": "4028adf67dc18b72017dc1c141560001",
  "name": "박주현_TEST",
  "desc": "",
  "status": "form.status.edit",
  "category": "process",
  "display": {
    "width": "960",
    "margin": "0 0 0 0",
    "padding": "15 15 15 15"
  },
  "group": [
    {
      "id": "a61bae5a63cebc452fb8492b09225430",
      "name": "",
      "display": {
        "displayOrder": 0,
        "isAccordionUsed": true,
        "margin": "10 0 10 0"
      },
      "label": {
        "visibility": true,
        "fontColor": "#8d9299",
        "fontSize": "14",
        "bold": false,
        "italic": false,
        "underline": false,
        "align": "left",
        "text": "GROUP LABEL"
      },
      "row": [
        {
          "id": "ae387369bc8334c895532883eb989143",
          "display": {
            "displayOrder": 0,
            "margin": "4 0 4 0",
            "padding": "0 0 0 0"
          },
          "component": [
            {
              "id": "aabab3632f2c6a3f160c00aebc109197",
              "type": "textArea",
              "display": {
                "displayOrder": 0,
                "columnWidth": "12"
              },
              "isTopic": false,
              "mapId": "",
              "tags": [],
              "value": "",
              "label": {
                "position": "top",
                "fontSize": "14",
                "fontColor": "#8d9299",
                "bold": false,
                "italic": false,
                "underline": false,
                "align": "left",
                "text": "COMPONENT LABEL"
              },
              "element": {
                "columnWidth": "12",
                "rows": "2",
                "placeholder": ""
              },
              "validation": {
                "required": false,
                "minLength": "0",
                "maxLength": "512"
              }
            }
          ]
        }
      ]
    }
  ]
}
```

### Response Sample

```
"0"
```

## 문서양식_데이터 삭제

---

### URL
```
DELETE /rest/forms/{formId}
```

### Parameter Sample

```json
{
  "formId": "4028adf67dc18b72017dc1c141560001"
}
```

### Response Sample

```
true
```
