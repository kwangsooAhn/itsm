# 참조인

참조인이라는 것은 문서를 열람할 수 있는, 혹은 열람을 요청하는 대상을 뜻한다.

신청서 작성 및 문서 진행 중간이나 완료 이후에도 참조인을 추가 / 삭제 할 수 있다.

참조인이 문서를 읽은 경우 참조인을 더 이상 삭제할 수 없다.

참조인으로 선택되면 toast 알림, 이메일, 메신저 등으로 알림이 전달된다.

참조인은 문서에 접근하는 사용자는 문서를 열람만 할 수 있고 편집은 불가하다. 단, 댓글 및 관련문서 추가 등은 가능하다.

해당 API는 참조인의 정보를 추가 / 조회 / 수정 / 삭제 하는 API 이다.

## 목차

---

1. [전체 데이터 조회](#전체-데이터-조회)
2. [데이터 등록](#데이터-등록)
3. [데이터 삭제](#데이터-삭제)
4. [참조인 읽음](#참조인-읽음)

## 전체 데이터 조회

---

### URL

```
GET /rest/instances/{instanceId}/viewer/
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": [
    {
      "viewerKey": "2c9180ab7b2a039b017b2a15b1f40001",
      "viewerName": "ADMIN",
      "organizationName": "연구개발본부 / 개발 2그룹 / ITSM 팀",
      "avatarPath": "/assets/media/images/avatar/img_avatar_01.png",
      "reviewYn": false,
      "displayYn ": false,
      "createUserKey": "0509e09412534a6e98f04ca79abb6424",
      "createDt": "2022-01-17T15:00:00.615042",
      "updateUserKey": null,
      "updateDt": null
    },
    ...
  ],
  "totalCount": 3
}
```

## 데이터 등록

---

### URL

```
POST /rest/instances/{instanceId}/viewer/
```

### Parameter Sample

```json
{
  "data": [
    {
      "documentId": "4028b21f7c90d996017c91ae7987004f",
      "viewerKey": "2c9180ab7b2a039b017b2a15b1f40001",
      "reviewYn": false,
      "displayYn ": false,
      "viewerType": "register|modify|delete"
    },
    ...
  ]
}
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": null
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/instances/{instanceId}/viewer/{viewerKey}
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": null
}
```

## 참조인 읽음

---

참조인이 읽음 버튼을 누른 경우 wf_instance_viewer 테이블의 review_yn 의 값을 true 로 변경한다.

### URL

```
POST /rest/instances/{instanceId}/viewer/{viewerKey}/read
```
### Parameter Sample

```json
{
  "viewerKey": "2c9180ab7b2a039b017b2a15b1f40001"
}
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": null
}