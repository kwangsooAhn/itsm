# CMDB_CI Type


## 목차

---

1. [데이터 단일 조회](#데이터-단일-조회)
2. [데이터 목록 조회](#데이터-목록-조회)
3. [데이터 추가](#데이터-추가)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)

## 데이터 단일 조회

---

### URL
```
GET /rest/cmdb/types/{typeId}
```

### Parameter Sample

```json
{
  "typeId": "4028b88179210e1b017921279d29000f"
}
```

### Response Sample

```json
{
  "typeId": "4028b88179210e1b017921279d29000f",
  "typeName": "Linux ",
  "typeDesc": "Linux Type입니다.",
  "typeLevel": 2,
  "typeSeq": 10,
  "typeAlias": "Linux",
  "typeIcon": "image_linux.png",
  "typeIconData": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAAUCAYAAACAl21KAAAB70lEQVR4XmNgwAEEFBQEpJTU1ksrqf2XVlR9L6WkOh8khq6OIJBSVF0ANgQZK6ruR1dHELj7BXxOSs8CGwCiXb39IIYpqBqgq8UL1m7Y+P/o8RNgzWs3bPq/YfNmMBvo3QZ0tTgByNY5E7L/v99h9v/DNvX/H3aZ/18zI5V0g9JDZcP/HdT+//+wLgr2d1UizSBJJZWCTzs0UAy5vMTgPzACgFjtPrp6nACo+MHjJ0/+vz2W8f/FweT//78//g8CsiqaEMOIcRVIUe/EyWCN6KCkvApsEDT2EtD1ogBNQ+OPHz5+RDcDDK5euwYJcJBhwESKM4GCbCkoLUfXjwJUtWFhBXKZ6gR0M8AAGDYbrl67jq4XBbhAE6YUNOugmwHOW+a2juj6MAAstcO8iOG9tR2aNQvbbNH1YYDCkiKU/GdsqJwLN+T/YR0HWHo5taUWXS8KmFBhjWLQh13aHTBDLiAnvI87gfjdE3T9cPBhh87/xGCN/8Ge6v9n1Wj+/7FPewvUINRsAMJ31hhhNezLswMYaiFYZz9Wg0D4+hprFEM+fvr0f+XcSgx1UIMOgLw2AVMCgkMiY+AYFB4hXuqoag7pbvh/VAu1fPq/X0EAKBEAkvx7UOfTiTnaz0EJVEJBxQGG3RyUfIByj6CuQEmMACcza0kkY5O4AAAAAElFTkSuQmCC",
  "classId": "4028b88179210e1b0179211eb65d0006",
  "className": "Linux",
  "createUserKey": "0509e09412534a6e98f04ca79abb6424",
  "createDt": "2021-08-09T07:34:45.411197",
  "updateUserKey": "4028b2217b29bf60017b2aa22d710000",
  "updateDt": "2021-11-24T04:24:13.512144",
  "editable": true,
  "ptypeId": "4028b88179210e1b017921277022000e",
  "ptypeName": "서버"
}
```

## 데이터 목록 조회

---

### URL
```
GET /rest/cmdb/types
```

### Parameter Sample

```json
{
  "search": ""
}
```

### Response Sample

```json
{
  "data": [
    {
      "typeId": "root",
      "typeName": "ROOT",
      "typeDesc": null,
      "typeLevel": 0,
      "typeSeq": 0,
      "typeAlias": "CI",
      "typeIcon": null,
      "typeIconData": null,
      "classId": "root",
      "className": "root",
      "ptypeId": null,
      "ptypeName": null
    },
    ...
  ],
  "totalCount": 56
}
```

## 데이터 추가

---

### URL
```
POST /rest/cmdb/types
```

### Parameter Sample

```json
{
  "typeId": "4028b88179210e1b017921279d29000f",
  "pTypeId": "4028b88179210e1b017921279d29000f",
  "typeName": "박주현_TEST",
  "typeAlias": "TEST",
  "typeDesc": "",
  "typeSeq": "1",
  "typeIcon": "image_linux.png",
  "classId": "4028b88179210e1b0179211eb65d0006",
  "className": "Linux",
  "typeLevel": "2"
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
PUT /rest/cmdb/types/{typeId}
```

### Parameter Sample

```json
{
  "typeId": "4028adf67dbcfb92017dbd34dbe20002",
  "pTypeId": "4028b88179210e1b017921279d29000f",
  "typeName": "박주현_수정_TEST",
  "typeAlias": "TEST",
  "typeDesc": "",
  "typeSeq": "1",
  "typeIcon": "image_linux.png",
  "classId": "4028b88179210e1b0179211eb65d0006",
  "className": "Linux",
  "typeLevel": "3"
}
```

### Response Sample

```
"1"
```

## 데이터 삭제

---

### URL
```
DELETE /rest/cmdb/types/{typeId}
```

### Parameter Sample

```json
{
  "typeId": "4028adf67dbcfb92017dbd34dbe20002"
}
```

### Response Sample

```
"0"
```