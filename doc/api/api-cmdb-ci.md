# CMDB_CI


## 목차

---

1. [CI_데이터 전체 조회](#CI-데이터-전체-조회)
2. [CI_데이터 상세 조회](#CI-데이터-상세-조회)
3. [CI 컴포넌트_데이터 상세 조회](#CI-컴포넌트-데이터-상세-조회)
4. [CI 컴포넌트_데이터 추가](#CI-컴포넌트-데이터-추가)
5. [CI 컴포넌트_데이터 삭제](#CI-컴포넌트-데이터-삭제)
6. [CI_연관 관계 데이터 조회](#CI-연관-관계-데이터-조회)
7. [CI_엑셀 다운로드](#CI-엑셀-다운로드)

## CI_데이터 전체 조회

---

typeId 가 root일 경우 전체 데이터를 조회한다. 
'전체'가 선택되면 속성항목이 없기 때문에 현재 나오는 기본항목만 출력된다.

### URL
```
POST /cmdb/cis/{typeId}
```

기본 검색 데이터는 url에 포함되어 전달된다.

### Parameter Sample

```json
{
  "searchItems": [
    {
      "attributeId": "a30f37ff527b142afd2a1b35206c142d",
      "searchValue": "사용자가 입력한 값"
    }
  ]
}
```

### Response Sample

```json
{
  "searchItems": [
    {
      "attributeId": "2c9180837c99e748017c9b099d500000",
      "attributeName": "IP주소",
      "attributeText": "",
      "attributeType": "inputbox",
      "attributeDesc": "",
      "attributeValue": "{validate:'','required':'false','maxLength':'100','minLength':'0'}",
      "searchValue": "사용자가 입력한 값"
    },
    ...
  ],
  "columnName": ["ciIconData", "ciId","ciNo", "typeName", "ciName", "ciDesc", "734ab921484883ad7760032a008baf21", "4028b25d791b75ac01791bb0f9140002", "4028b25d7916ef5f017916f397230000"],
  "columnTitle": ["", "","자산 번호", "자산 유형", "자산 이름", "자산 설명", "버전 정보", "담당자", "입고일"],
  "columnWidth": ["20px","0px", "180px", "120px", "150px", "245px", "120px", "120px", "120px"],
  "columnType": ["icon","hidden","string", "string", "string", "string", "string", "string", "dateTime"],
  "contents": [
    ["data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAASCAYAAABrXO8xAAAAQklEQVR4XmMwtnX7YGzr/p9UzIAuQCymjsb7Dx79//7jJ1YMksOpkRRMHY2jTkXFw0EjT+k+MEYXp51GQphMjW4fALjU5CZSJYXqAAAAAElFTkSuQmCC", "88fc78892916dc310b4984858b28120d", "Zenius_SMS_00010", "Linux", "홈페이지 서버#1", "회사 홈페이지 서버 1", "CentOS 7.0", "정희찬", "2021-12-31T15:00:00.000Z"],
    ["data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAA4AAAASCAYAAABrXO8xAAAAQklEQVR4XmMwtnX7YGzr/p9UzIAuQCymjsb7Dx79//7jJ1YMksOpkRRMHY2jTkXFw0EjT+k+MEYXp51GQphMjW4fALjU5CZSJYXqAAAAAElFTkSuQmCC", "d2cc70ca65fd2ac35ae6ee8e0c52e2db", "Zenius_SMS_00020", "Linux", "홈페이지 서버#2", "회사 홈페이지 서버 2", "CentOS 7.0", "최보람", "2021-12-31T15:00:00.000Z"]
  ]
}
```

## CI_데이터 상세 조회

---

### URL
```
GET /rest/cmdb/cis/{ciId}
```

### Parameter Sample

```json
{
  "ciId": "a30f37ff527b142afd2a1b35206c142d"
}
```

### Response Sample

```json
{
  "ciId": "a30f37ff527b142afd2a1b35206c142d",
  "ciNo": "CI_SERVER_TRU64_000005",
  "ciName": "BLACKOUT",
  "ciIcon": "image_linux.png",
  "ciIconData": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAAUCAYAAACAl21KAAAB70lEQVR4XmNgwAEEFBQEpJTU1ksrqf2XVlR9L6WkOh8khq6OIJBSVF0ANgQZK6ruR1dHELj7BXxOSs8CGwCiXb39IIYpqBqgq8UL1m7Y+P/o8RNgzWs3bPq/YfNmMBvo3QZ0tTgByNY5E7L/v99h9v/DNvX/H3aZ/18zI5V0g9JDZcP/HdT+//+wLgr2d1UizSBJJZWCTzs0UAy5vMTgPzACgFjtPrp6nACo+MHjJ0/+vz2W8f/FweT//78//g8CsiqaEMOIcRVIUe/EyWCN6KCkvApsEDT2EtD1ogBNQ+OPHz5+RDcDDK5euwYJcJBhwESKM4GCbCkoLUfXjwJUtWFhBXKZ6gR0M8AAGDYbrl67jq4XBbhAE6YUNOugmwHOW+a2juj6MAAstcO8iOG9tR2aNQvbbNH1YYDCkiKU/GdsqJwLN+T/YR0HWHo5taUWXS8KmFBhjWLQh13aHTBDLiAnvI87gfjdE3T9cPBhh87/xGCN/8Ge6v9n1Wj+/7FPewvUINRsAMJ31hhhNezLswMYaiFYZz9Wg0D4+hprFEM+fvr0f+XcSgx1UIMOgLw2AVMCgkMiY+AYFB4hXuqoag7pbvh/VAu1fPq/X0EAKBEAkvx7UOfTiTnaz0EJVEJBxQGG3RyUfIByj6CuQEmMACcza0kkY5O4AAAAAElFTkSuQmCC",
  "ciDesc": "BLACKOUT1",
  "ciStatus": "use",
  "interlink": false,
  "typeId": "4028b88179210e1b0179217aa9660049",
  "typeName": "TRU64",
  "classId": "4028b88179210e1b017921261f87000c",
  "className": "TRU64",
  "classes": [
    {
      "className": "일반정보 - 인프라",
      "attributes": [
        {
          "attributeId": "27caaeba596663101d55a09ec873a375",
          "attributeName": "상태",
          "attributeDesc": "일반정보 - 인프라",
          "attributeText": "상태",
          "attributeType": "radio",
          "attributeOrder": 1,
          "attributeValue": "{\"option\":[{\"text\":\"사용\",\"value\":\"use\"},{\"text\":\"미사용\",\"value\":\"unused\"},{\"text\":\"폐기\",\"value\":\"disposal\"},{\"text\":\"할당\",\"value\":\"assignment\"},{\"text\":\"반납\",\"value\":\"return\"},{\"text\":\"AS\",\"value\":\"as\"},{\"text\":\"예비\",\"value\":\"spare\"}]}",
          "value": null,
          "childAttributes": []
        },
        ...
      ]
    },
    ...
  ],
  "ciRelations": [],
  "ciTags": [
    {
      "tagId": "4028b22f7d7ecb28017d7ef8d35a0004",
      "value": "1"
    }
  ],
  "createUserKey": "4028b2217b29bf60017b2aa22d710000",
  "createDt": "2021-11-24T05:35:30.99742",
  "updateUserKey": "4028b2217b29bf60017b2aa22d710000",
  "updateDt": "2021-11-24T05:45:04.644643"
}
```

## CI 컴포넌트_데이터 상세 조회

---

### URL
```
GET /rest/cmdb/cis/{ciId}/data
```

### Parameter Sample

```json
{
  "ciId": "a9e10227de9c04e04c904bccd13f94ef"
}
```

### Response Sample

```
null
```

## CI 컴포넌트_데이터 추가

---

### URL
```
POST /rest/cmdb/cis/{ciId}/data
```

### Parameter Sample

```json
{
  "ciId": "a21d570706be12f3c7d9bc538ad3357c",
  "componentId": "a8098cb262444be29be38dc0f5e2e2eb",
  "values": {
    "ciAttributes": [
      {
        "id": "4028b881792074460179209cef74000c",
        "type": "inputbox",
        "value": ""
      },
      ...
      {
        "id": "d47973f063130acab00b2cf203a9788b",
        "type": "group-list",
        "value": "",
        "childAttributes": [
          {
            "id": "2c91808e7c8027a1017c828506300002",
            "type": "inputbox",
            "value": "",
            "seq": 0
          },
          {
            "id": "2c91808e7c8027a1017c8287e6c00003",
            "type": "inputbox",
            "value": "",
            "seq": 0
          }
        ]
      },
      ...
    ],
    "ciTags": [],
    "relatedCIData": []
  },
  "instanceId": "d4925dfc89d44905907bc5537d6b80db"
}
```

### Response Sample

```
true
```

## CI 컴포넌트_데이터 삭제

---

### URL
```
DELETE /rest/cmdb/cis/data
```

### Response Sample

```
true
```

## CI_엑셀 다운로드

---

### URL
```
GET /rest/cmdb/cis/excel
```

### Parameter Sample

```json
{
  "searchValue": "",
  "tagSearch": "",
  "flag": "itsm"
}
```

### Response Sample

```
ResponseEntity<ByteArray>
```