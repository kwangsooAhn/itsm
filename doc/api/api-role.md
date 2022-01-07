# 역할

## 목차

---

1. [데이터 목록 조회](#데이터-목록-조회)
2. [데이터 상세 조회](#데이터-상세-조회)
3. [데이터 등록](#데이터-등록)
4. [데이터 수정](#데이터-수정)
5. [데이터 삭제](#데이터-삭제)
6. [데이터 다운로드](#데이터-다운로드)
7. [2022-01 업데이트](#2022-01-업데이트)


## 데이터 목록 조회

### URL

```
GET /rest/roles
```

### Response Sample

```json
{
  "data": [
    {
      "roleId": "faq.all",
      "roleName": "FAQ관리자",
      "roleDesc": null
    },
    ...
  ],
  "paging": {
    "totalCount": 24,
    "totalCountWithoutCondition": 24,
    "currentPageNum": 0,
    "orderType": null
  }
}
```

## 데이터 상세 조회

---

### URL

```
GET /rest/roles/{roleId}
```

### Parameter Sample

```json
{
  "roleId": "document.manager"
}
```

### Response Sample

```json
{
  "data": {
    "roleId": "document.manager",
    "roleName": "문서처리 관리자",
    "roleDesc": null,
    "createUserName": "ADMIN",
    "createDt": "2021-12-09T15:51:18.606923",
    "updateUserName": null,
    "updateDt": null,
    "arrAuthList": [
      {
        "authId": "action.cancel",
        "cuthName": "문서 취소",
        "authDesc": null
      },
      {
        "authId": "action.terminate",
        "cuthName": "문서 종결",
        "authDesc": null
      }
    ],
    "userRoleMapCount": 0
  }
}
```

## 데이터 등록

---

### URL

```
POST /rest/roles
```

### Parameter Sample

```json
{
  "roleId": "role.test",
  "roleName": "역할 등록,수정,삭제 테스트",
  "roleDesc": "테스트용 ",
  "createUserName": null,
  "createDt": null,
  "updateUserName": null,
  "updateDt": null,
  "arrAuthId": {
    0: "document.read.admin",
    1: "role.create",
    2: "document.read.admin"
  },
  "arrAuthList": null,
  "userRoleMapCount": 0
}
```

### Response Sample

```
{
  "role.test"
}
```

## 데이터 수정

---

### URL

```
POST /rest/roles/{roleId}
```

### Parameter Sample

```json
{
  "roleId": "role.test",
  "roleName": "역할 등록,수정,삭제 테스트",
  "roleDesc": "수정",
  "createUserName": null,
  "createDt": null,
  "updateUserName": null,
  "updateDt": null,
  "arrAuthId": {
    0: "cmdb.attribute.create",
    1: "cmdb.ci.update",
    2: "cmdb.attribute.read"
  },
  "arrAuthList": null,
  "userRoleMapCount": 0
}
```

### Response Sample

```
{
  "role.test"
}
```

## 데이터 삭제

---

### URL

```
DELETE /rest/roles/{roleId}
```

### Parameter Sample

```json
{
  "roleId": "role.test"
}
```

### Response Sample

```
{
  "true"
}
```

## 데이터 다운로드

---

### URL

```
GET /rest/roles/excel
```

### Parameter Sample

```
  "roleSearchCondition": RoleSearchCondition
```

### Response Sample

```json
{
  "status": "200 OK",
  "header": {
    "Content-Type": "application/vnd.ms-excel",
    "Content-Disposition": "attachment; filename=6203f83ee98d47c8a967926ed312086d"
  },
  "body": ...
}
```

---

## 2021-01 업데이트

### 히스토리
* 2022년 1월 기준, 권한 메뉴 제거 및 정리에 따라 역할 또한 수정

### 수정방향
* 재정의 된 기본 권한 목록에 따라 기본 역할 목록을 최소화 하여 구성

* 개발 및 사용이 최대한 간단하면서 유연한 구조를 가지도록 수정

### 수정사항
#### 기본 패키지에서 역할의 개수를 최소화 하고 혼란이 없도록 재구성
##### 1) 기본 역할 예제
* 아래 첨부된 표를 기준으로 기본 템플릿에 7개의 역할을 구성한다.

|역할 이름|권한 이름|
|:---:|---|
|시스템 관리자|일반 사용|
| |업무흐름 관리|
| |CMDB 관리|
| |CMDB 조회|
| |보고서 관리|
| |보고서 조회|
| |시스템  관리|
|서비스 관리자|일반 사용|
| |CMDB 조회|
| |보고서 조회|
| |업무 취소|
|서비스 담당자|일반 사용|
| |CMDB 조회|
| |보고서 조회|
|일반 사용자|일반 사용|
| |CMDB 조회|
|업무흐름 관리자|일반 사용|
| |업무흐름 관리|
| |CMDB 조회|
| |보고서 관리|
| |보고서 조회|
|CMDB 관리자|일반 사용|
| |CMDB 관리|
| |CMDB 조회|
| |보고서 조회|
|포털 관리자|일반 사용|
| |포털 관리|

##### 2) 기본 역할 설명
　**시스템 관리자**  
　　- 최초 설치 시 “admin” 계정이 가지는 역할이다.    
　　- 시스템 전체 메뉴에서 모든 기능을 사용할 수 있다.  
　　- 해당 역할을 가진 사용자가 최소 1명은 있어야 한다.  
　　　(마지막 사용자의 역할을 수정하거나 사용자를 삭제하려면 화면에서 막아야 한다.)  
                                                                                                                                                                                                         
　**서비스 관리자**  
　　- 각 업무별 승인을 담당하는 관리자이다.    
　　- 기존 ”서비스데스크 관리자”, ”변경관리 관리자”와 같은 이름으로 사용하던 역할이다.  
　　- 기본 템플릿에서는 ”서비스 관리자”로 통칭하고, 실제 사이트에서는 다양한 이름으로 역할이 생성될 수 있다.  
                                                                                                                                                                                                    
　**서비스 담당자**  
　　- 각 업무 프로세스에서 실제 업무를 처리하는 담당자이다.    
　　- 기존 ”변경관리 담당자”, ”장애처리 담당자”와 같은 이름으로 사용하던 역할이다.  
　　- 기본 템플릿에서는 ”서비스 담당자”로 통칭하고, 실제 사이트에서는 다양한 이름으로 역할이 생성될 수 있다.  

　**일반 사용자**  
　　- 신청서를 작성할 수 있다. 단, 업무흐름을 처리할 수 있는 권한은 없다.   
　　- 일반 사용자는 회원 등록시 자동으로 부여되는 역할이며, 모든 사용자에게서 해당 역할을 제거할 수 없다.

　**업무흐름 관리자**  
　　- 업무흐름을 관리하기 위한 역할이다.  
　　- 업무흐름 하위 메뉴들(문서양식, 프로세스 등)에 대해 모두 편집 가능한 권한을 가지고 문서를 진행시킬 수 있다.  

　**CMDB 관리자**  
　　- CMDB와 관련된 설정을 관리하는 역할이다.  

　**포털<sup>[💡](#portal)</sup>   관리자**  
　　- 포털을 관리하기 위한 역할이다.    
　　- 일반 사용 권한 외에 포털 관리 권한만 가진다.  
   
---

<a id="portal">※  ITSM 내에서 제공하는 기능 중 공지사항, FAQ, 자료실, 게시판을 묶어 포털이라고 정의한다.</a> 
