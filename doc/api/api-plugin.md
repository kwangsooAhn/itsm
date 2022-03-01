# 플러그인 관리

외부 연동을 위한 플러그인 관련 API 이다.

## 목차

---

1. [전체 데이터 조회](#전체-데이터-조회)
2. [플러그인 실행 요청](#플러그인-실행-요청)
3. [플러그인 목록](#플러그인-목록)

## 전체 데이터 조회

---

### URL

```
GET /rest/plugins
```

### Response Sample

```json
{
  "status": 200,
  "message": "OK",
  "data": [
    {
      "pluginId": "4028b21e7d1ab894017d1ac0bf50000d",
      "pluginName": "방화벽연동-FOCS"
    },
    {
      "pluginId": "4028b21e7d1ab894017d1ac0bf50001d",
      "pluginName": "Test-Woo"
    }]
}
```

## 플러그인 실행 요청

---

### URL

```
POST /rest/plugins/{pluginId}
```

### Parameter Sample

```json
{
  "pluginId": "",
  "data": [
    ["출발지","목적지","서비스","시작일", "만료일"],
    ["1.1.1.1","2.2.2.2-2.2.2.22","TCP/80","2021-12-31T15:00:00.000Z","2022-02-27T15:00:00.000Z"],
    ["1.1.1.1","2.2.2.2-2.2.2.22","UDP/80","",""],
    ["1.1.1.1","2.2.2.2-2.2.2.22","TCP/8080-8088","",""],
    ["1.1.1.1","3.3.3.0/24","TCP/80","",""],
    ["1.1.1.1","3.3.3.0/24","UDP/80","",""],
    ["1.1.1.1","3.3.3.0/24","TCP/8080-8088","",""]
  ]
}
```

### Response Sample

#### 성공시
```
{
  "status": 200,
  "message": "Success",
  "data": {
     "result": true
   }
}
```

#### 실패시
```
{
  "status": 200,
  "message": "Fail",
  "data": {
     "result": false
   }
}
```

## 플러그인 목록

---

### 1. 방화면 연동(FOCS) 플러그인

* 규정감사 OPEN API

   * Yaml 설정 파일 : assessmentConfig.yml
    
   * API 설명 : DR 테이블 내에 등록된 데이터를 API 통신으로 전송하여 값을 리턴받는다.

### URL

```
POST /api/public/v1/assessment/prechange
```

### [Request Sample]
```
{
    "sources": ["1.1.1.1"],
    "destinations": ["2.2.2.2-2.2.2.22", "3.3.3.0/24"],
    "services": ["TCP/80", "UDP/80", "TCP/8080-8088"]
}
```
### [Response Sample]

```
{
    "result": true, //정상 응답일 경우 true, 오류등일 경우 false
    "message": “success”, //정상 응답일 경우 "success", 오류일 경우 오류 메시지
    "data": {
        "contents": [//응답 데이터
            {
                "deviceName": "연구소_Secui 4.0.2", //[사용] 방화벽명
                "assessments": {
                    "ece7619a-5bcb-4c28-9f39-c5381302eb65": "A, B, C 클래스 포함 정책" //[사용]
                    규정집
                },
                "controlName": "목적지에 C 클래스 객체 포함 정책", //[사용] 세부규정명
                "controlDescription": "목적지에 C 클래스 객체 포함 정책 조회", //[사용]
                세부규정설명
                "deviceId": "83",
                "controlId": "c40dffc0-d057-48ec-9360-5d54459460c3",
                "controlSeq": 5,
                "controlCode": "0005",
                "controlSeverity": 8,
                "complianceStatus": null
            }
        ]
    }
}
```
* 중복검사 OPEN API
    
    * Yaml 설정 파일 : duplicateConfig.yml
    
    * API 설명 : DR 테이블 내에 등록된 데이터를 통해 API 통신으로 전송하여 값을 리턴받는다.

### URL

```
POST /api/public/v1/rule/duplicate/search
```

### [Request Sample]
```
{
    "sources": ["1.1.1.1"], //출발지 (필수)
    "destinations": ["2.2.2.2-2.2.2.22", "3.3.3.0/24"], //목적지 (필수)
    "services": ["TCP/80", "UDP/80", "TCP/8080-8088"], //서비스 (필수)
    "startDate": “2021-01-01”, //시작일 (필수 X)
    "expirationDate": “2021-01-31” //만료일 (필수 X)
}
```

### [Response Sample]
```
{
    "result": true, //정상 응답일 경우 true, 오류등일 경우 false
    "message": “success”, //정상 응답일 경우 "success", 오류일 경우 오류 메시지
    "data": {
        "contents": [//응답 데이터
            {
                "deviceId": 83,
                "deviceName": "연구소_Secui 4.0.2",
                "ruleNumber": 151,
                "ruleOrder": 11,
                "ruleId": "151",
                "sources": [{
                        "name": "cclass1",
                        "matchId": "1945401",
                        "changeUsers": [],
                        "type": "SINGLE",
                        "members": [],
                        "parents": [],
                        "secRuleCount": 0,
                        "hitcount": 0,
                        "addresses": [{
                                "address": "1.1.1.0"
                            }
                        ],
                        "derived": false,
                        "duplicationObjects": []
                    }
                ],
                "destinations": [{
                        "name": "cclass2",
                        "matchId": "1945402",
                        "changeUsers": [],
                        "type": "SINGLE",
                        "members": [],
                        "parents": [],
                        "secRuleCount": 0,
                        "hitcount": 0,
                        "addresses": [{
                                "address": "2.2.2.0"
                            }
                        ],
                        "derived": false,
                        "duplicationObjects": []
                    }
                ],
                "services": [{
                        "name": "Any",
                        "matchId": "any",
                        "changeUsers": [],
                        "type": "ANY",
                        "members": [],
                        "parents": [],
                        "services": [],
                        "derived": false,
                        "duplicationObjects": []
                    }
                ],
                "users": [],
                "apps": [],
                "disabled": false,
                "hitCount": 0
            }
        ]
    }
}
``` 
* 신청서 등록 API
    
    * Yaml 설정 파일 : applicationConfig.yml

    * API 설명 : 신청서에 설정한 컴포넌트 및 DR 테이블 값을 이용하여 방화벽 연동 신청서와 관련하여 신청서 등록을 진행한다.

### URL

```
[POST] /api/public/v1/application/form
```

### [Request Sample]
``` 
{
    "seq": "신청번호", //신청번호: 필수 X (64 자 까지 허용되며, 입력 안할경우 FOCS 고유 생성규칙에 따라 생성 됨)
    "title": "신청서 제목", //신청서 제목: 필수 X
    "applicantId": "yohan2", //신청자 아이디: 필수 O
    "applicant": "이대리", //신청자 이름: 필수 O
    "applicantSign": true, //신청자 상신결재유무: 필수 O - true 로 고정
    "department": "기술연구소", //신청자 부서명: 필수 X
    "phoneNumber": "010-0000-0002", //신청자 전화번호: 필수 X
    "email": "yohan2@focs.co.kr", //신청자 이메일: 필수 X
    "author": "이대리", //소유자 이름: 필수 O
    "owners": [//신청서 소유자 정보: 필수 O
        {   
            "id": "yohan2", //신청서 소유자 아이디: 필수 O
            "name": "이대리" //신청서 소유자 이름: 필수 O
        }],
    ...
}
``` 
### [Response Sample]
``` 
{
    "result": true, //정상 응답일 경우 true, 오류등일 경우 false
    "message": “success”, //정상 응답일 경우 "success", 오류일 경우 오류 메시지
    "data": {
        "contents": { //응답 데이터 - 최종 등록된 신청서 정보
        ...
        }
    }
}
``` 