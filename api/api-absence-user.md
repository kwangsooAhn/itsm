# 사용자 부재 설정 및 업무 대리인 설정

담당자 부재 시 승인 및 업무처리를 위해 권한을 위임할 수 있어야 한다.

권한 위임의 경우 사용자 부재 여부 설정이 선행이 되어야 하며   
사용자 부재 설정은 각 사용자 정보 화면 혹은 자기 정보 수정 화면에서 가능하다.  
( url 정보는 개별 사용자 정보 CRUD url과 동일하다. )

사용자 부재 설정 시 부재 여부, 부재 기간, 업무 대리인에 대한 정보가 등록되어야 한다.

권한 위임을 처리하거나 조회할 때 참고가 되는 `사용자 부재 설정`을 추가/조회/삭제하는 API 이다.

## 목차

---

1. [데이터 조회](#데이터-조회)
2. [데이터 추가](#데이터-추가)
3. [데이터 삭제](#데이터-삭제)

## 데이터 조회

---

### URL
```
GET /users/{userKey}/{target}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK",
    "data": [
      {
        "userKey": "2c9180ab7b2a039b017b2a15b1f40001",
        "userId": "jylim",
        "userName": "임지영",
        /* 이하 기본 회원 정보 */
        "absenceYn": "true",
        "absenceStartDt": "2021-11-18T23:16:34.277465",
        "absenceEndDt": "2021-11-19T23:16:34.277465",
        "substituteUserKey": "0509e09412534a6e98f04ca79abb6424"    
      },
      {
            "userKey": "2c9180ab7b2a039b017b2a15b1f40001",
            "userId": "jylim",
            "userName": "임지영",
            /* 이하 기본 회원 정보 */
            "absenceYn": "false",
            "absenceStartDt": null,
            "absenceEndDt": null,
            "substituteUserKey": null    
      }
    ]
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|

## 데이터 추가

---

### URL
```
PUT /rest/users/{userKey}/info
```

### Parameter Sample
※ absenceYn = true 이고, awf_user_custom에 입력 시,
```json
{
  "userKey": "40288a9d7be7aefc017be7d150910000",
  "custom_type": "user_absence",
  "custom_value": "{'absenceStartDt':'2021-11-18T23:16:34.277465', 'absenceEndDt':'2021-11-19T23:16:34.277465', 'substituteUserKey':'0509e09412534a6e98f04ca79abb6424'}"
}
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|

## 데이터 삭제

---

* 사용자를 직접적으로 삭제할 수 없습니다.   
   단, 부재 여부(absence_yn)가 false 일 때 awf_user_custom 데이터에 해당하는  
   absenceStartDt, absenceEndDt, substituteUserKey에 대한 정보를 제거합니다.
  

### URL
```
PUT /rest/users/{userKey}/info
```

### Response Sample

```json
{
  "responseCode": 200,
  "errorMessage": "OK"
}
```

### Error

| 에러코드 | 설명 | 
|:---|:---|
|
