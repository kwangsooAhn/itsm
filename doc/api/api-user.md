# 사용자

## 목차

---

1. [사용자 목록 조회](#사용자-목록-조회)
2. [사용자 등록](#사용자-등록)
3. [다른 사용자 정보 수정](#다른-사용자-정보-수정)
4. [본인 정보 수정](#본인-정보-수정)
5. [문서 위임자 변경](#문서-위임자-변경)
6. [비밀번호 초기화](#비밀번호-초기화)
7. [비밀번호 즉시 변경](#비밀번호-즉시-변경)
8. [비밀번호 나중에 변경](#비밀번호-나중에-변경)
9. [색상 조회](#색상-조회)
10. [색상 추가](#색상-추가)
11. [사용자 목록 다운로드](#사용자-목록-다운로드)

## 사용자 목록 조회

---

### URL

```
GET /rest/users/all
```

### Response Sample

```json
{
  "data": [
    {
      "userKey": "0509e09412534a6e98f04ca79abb6424",
      "userId": "admin",
      "userName": "ADMIN"
    },
    ...
  ]
}
```

## 사용자 등록

---

### URL

```
POST /rest/users
```

### Parameter Sample

```json
{
  "userId": "jy.lee",
  "userName": "JAEYONG",
  "password": "30D4A816F07A13E1889592F6D7589155A762C2EC72D50D60333036A634301B26B54205A126FC89200DD8FF734842C5E9EBEB58213CB26D19CA034324ED2D11A93D496DA4358268EA321A6458B8A532FDE8CEE10D028098077A0431B3A411EE653E02B8ECF567C8A36C8C5D64C66067215CEFC4FE0FC24D27EAE26585B8F1D0D6B27A45149FB7556B08CB2E7085BB14E85234564BEE606FB7771BA0FD95062DF43920A5E76D73243C5CD788A45838EF4D5703351D2DBEA57BFC1F4EEDEA52B5CB222C856221C144E933DDB4E27ED6E2DF8CF151EB38932F274CB36CD1694C550F30B175BCB58A68C7933DE8CDC80F75830E191A4AE6B0D775CDCF06B2F0A603A8",
  "email": "jy.lee@brainz.co.kr",
  "position": "사원",
  "department": "department.group.itsm",
  "officeNumber": "",
  "mobileNumber": "",
  "roles": {
    0: "document",
    1: "document.manager"
  },
  "timeZone": "Asia/Seoul",
  "lang": "ko",
  "timeFormat": "yyyy-MM-dd HH:mm",
  "theme": "default",
  "fileSeq": null,
  "avatarUUID": ""
}
```

### Response Sample

```
{
  "0"
}
```

## 다른 사용자 정보 수정

---

### URL

```
PUT /rest/users/{userKey}/all
```

### Parameter Sample

```json
{
  "userKey": "40288a9d7dc05ea6017dc0c601460000",
  "userId": "jy.lee",
  "userName": "JAEYONG",
  "password": null,
  "email": "jy.lee@brainz.co.kr",
  "position": "대리",
  "department": "department.group.itsm",
  "officeNumber": "",
  "mobileNumber": "",
  "roles": {
    0: "document",
    1: "document.manager",
    2: "application.change.assignee",
    3: "application.change.manager"
  },
  "timeZone": "Asia/Seoul",
  "lang": "ko",
  "timeFormat": "yyyy-MM-dd HH:mm",
  "theme": "default",
  "fileSeq": null,
  "useYn": true,
  "avatarUUID": "40288a9d7dc05ea6017dc0c601460000",
  "absenceYn": false,
  "absence": {
    "startDt": null,
    "endDt": null,
    "substituteUserKey": "",
    "substituteUser": ""
  }
}
```

### Response Sample

```
{
  "5"
}
```

## 본인 정보 수정

---

### URL

```
PUT /rest/users/{userKey}/info
```

### Parameter Sample

```json
{
  "userKey": "0509e09412534a6e98f04ca79abb6424",
  "userId": "admin",
  "userName": "ADMIN",
  "password": null,
  "email": "admin.gmail.com",
  "position": "대표",
  "department": "d",
  "officeNumber": "",
  "mobileNumber": "",
  "roles": null,
  "certificationCode": null,
  "status": null,
  "timeZone": "Asia/Seoul",
  "lang": "ko",
  "timeFormat": "yyyy-MM-dd HH:mm",
  "theme": "default",
  "fileSeq": null,
  "useYn": true,
  "avatarUUID": "0509e09412534a6e98f04ca79abb6424",
  "absenceYn": false,
  "absence": {
    "startDt": null,
    "endDt": null,
    "substituteUserKey": "",
    "substituteUser": ""
  },
  "request": HttpServletRequest,
  "response": HttpServletResponse
}
```

### Response Sample

```
{
  "0"
}
```

## 문서 위임자 변경

---

### URL

```
POST /rest/users/absence
```

### Parameter Sample

```json
{
  "absenceInfo": "{"userKey":"","substituteUserKey":"40288a9d7dc05ea6017dc0c601460000"}"
}
```

### Response Sample

```
{
  true
}
```

## 비밀번호 초기화

---

### URL

```
Put /rest/users/{userKey}/resetpassword
```

### Parameter Sample

```json
{
  "userKey": "40288a9d7dc05ea6017dc0c601460000"
}
```

### Response Sample

```
{
  "6"
}
```

## 비밀번호 즉시 변경

---

### URL

```
PUT /rest/users/udatePassword
```

### Parameter Sample

```json
{
  "userId": "jy.lee",
  "nowPassword": "4a61686567d607880fe3f1534e7bd41e9dfd6d388d7a232e5b9ae64a948493820191a8dbdd10f852a750378357ed3754b21ac4a84058ab3c33325a28a5ebc6527bc83e5fe65d35bafb687acc056ba5754f2c7dc4338d4785bf2aed1fba6119dc884962ffb5e0486f3828d4c6f87a9b686a7350125e7423e14c2eee0258defaea92d30f3553658ae6c53cd3751f1ec90a5a084017a3b285bd556b7fe0b84ef5beed5402690d0e37f67b77ebb10d8387baf85836ade8c6eaee9feec394448632549163e018f99513dbc95d5d9b15c9eaeb8909185f70b6d058f41f0b6c969541af202532b39654bf476a3d6156aa479b3c939b166428725dd4b60e1a537e3d211f",
  "newPassword": "5d516ec186211d5dc51f02a8ef04b45d39e1e0020a72964574e11e16fe9081182ce960d49cfb23cefcc4347625504952ddc0237d59b792f79786ac0a34469bc56285d6fcdeb441f19595f1280f4b01940a09dc731ea89e37a4b0521712108021542eacfb8103fe9a3d06e93b172044a3e41c5de5736112b611c9371476d02a910703ac5f7b3fed930b8797911ef9d530faa3f6b8972935b5a7eadb19b4b3f4abfd72da322f569be0e4668edcca176179edc069b98c56c6c7bfb3d52909ee57b7b99858d8743de54e528fe397238bfe5b76558e48214513a89dab4fce383b9fdb849249c7e91923e5850402bd89b09622e09c34d4dda9937fc08658d1a918b997"
}
```

### Response Sample

```
{
  1
}
```

## 비밀번호 나중에 변경

---

### URL

```
PUT /rest/users/nextTime
```

### Parameter Sample

```json
{
  "userId": "jy.lee",
  "nowPassword": null,
  "newPassword": null
}
```

### Response Sample

```
{
  0
}
```


## 색상 조회

---

### URL

```
PUT /rest/users/udatePassword
```

### Response Sample

```json
{
  "userKey": "0509e09412534a6e98f04ca79abb6424",
  "customType": "color",
  "customValue": "#000000"
}
```

## 색상 추가

---

### URL

```
PUT /rest/users/updatePassword
```

### Parameter Sample

```json
{
  "userKey": "",
  "customType": "",
  "customValue": "##000000|#7765f2"
}
```

### Response Sample

```
{
  true
}

```

## 사용자 목록 다운로드

---

### URL

```
GEt /rest/users/excel
```

### Parameter Sample

```
{
  "userSearchCondition": UserSearchCondition
}
```

### Response Sample

```json
{
  "status": "200 OK",
  "headers": {
    "Content-Disposition": "attachment; filename=61c87f24f0b84fe69b8ffafca9a39ee9",
    "Content-Type": "application/vnd.ms-excel"
  },
  "body": ...
}

```