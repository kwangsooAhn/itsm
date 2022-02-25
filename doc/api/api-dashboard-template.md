# 대시보드 - 템플릿

사용자가 정의한 형태의 템플릿을 바탕으로 대시보드 화면을 제공한다.

## 목차

---

1. [데이터 조회](#데이터-조회)

## 데이터 조회

---

개인현황판은 설정 데이터를 사용하지만 설정용 화면은 현재 단계에서 제외되었다. (2022-02 기준) 
설정화면을 통해서 데이터가 들어간 상태로 가정하고 화면 출력을 구성한다.

---

### 컴포넌트 구성 (Template-001 기준)

#### 부서별 요청현황

`부서별 요청현황` 컴포넌트는 Highchart - basicColumn 차트로 구성된다.
관련 데이터는 `public > assets > js > cahrt > dummy > chart.basicColumn.json` 파일을 참고한다.

#### 개인 요청현황

`개인 요청현황` 컴포넌트는 접속한 사용자가 신청한 문서들 중 문서별로 미처리 된 건수를 조회할 수 있다.

#### 요청현황

`요청현황` 컴포넌트는 전체 혹은 부서별 미처리 요청들을 조회할 수 있다.
최초 화면 접근 시 전체 미처리 요청을 조회한다.
`부서별 요청현황` 컴포넌트에서 부서명을 클릭할 경우, 해당하는 부서의 미처리 요청들을 조회하여 `요청현황` 컴포넌트에 출력한다.
문서별 설정된 정보에 따라 `요청현황`의 컬럼은 동적으로 변경될 수 있다.

※ 현재(2022-02) 버전에서 sort 기능은 제공하지 않는다. 

### URL

#### 템플릿 조회
```
GET /dashboard/view
```
#### 요청현황 조회
```
GET rest/dashboard/unprocessedRequests/{organization_id}
```

### Response
리턴되는 내용은 model 내의 `template`를 참고한다.

### response sample
템플릿을 구헌하는 컴포넌트의 정보를 `components` 내에 아래와 같은 형태로 정의한다.   
컴포넌트 정보는 `key`, `title`, `target`으로 구성되며,  
특히 `target`의 경우 각 컴포넌트를 구성하기 위한 디스플레이 및 데이터 정보로 구성된다.    

```json
{
  "components": [
    {
      "key": "requestStatusByOrganization.chart",
      "title": "부서별 요청현황",
      "target": {
        "organizations": ["4028b2d57d37168e017d3715fae00002", "4028b2d57d37168e017d3713bb430003", "4028b2d57d37168e017d3715fae00004", "4028b2d57d37168e017d3715fae00005"],
        "documents": ["4028b21f7c90d996017c91ae7987004f", "4028b21f7c9adb6a017c9b18489900c9", "4028b21f7c9ff7c8017ca06bde520058", "2c9180867cc31a25017cc7a779d70523"]
      }
    }, {
      "key": "requestStatusByUser.list",
      "title": "개인 요청 현황",
      "target": {
        "documents": ["4028b21f7c90d996017c91ae7987004f", "4028b21f7c9adb6a017c9b18489900c9", "4028b21f7c9ff7c8017ca06bde520058", "2c9180867cc31a25017cc7a779d70523"]
      }
    }, {
      "key": "requestListByOrganization.list",
      "title": "요청현황",
      "target": {
        "documents": ["4028b21f7c90d996017c91ae7987004f", "4028b21f7c9adb6a017c9b18489900c9", "4028b21f7c9ff7c8017ca06bde520058", "2c9180867cc31a25017cc7a779d70523"],
        "items": [
         {
            "title": "순번",
            "width": "60px",
            "type": "index",
            "name": "",
            "dataType": "number" 
          }, {
            "title": "신청부서",
            "width": "200px",
            "type": "mapping",
            "name": "z-sd-requester-department",
            "dataType": "string" 
          }, {
            "title": "문서 종류",
            "width": "200px",
            "type": "field",
            "name": "document_name",
            "dataType": "string" 
          }, {
            "title": "제목",
            "width": "300px",
            "type": "mapping",
            "name": "z-sd-request-title",
            "dataType": "string" 
          }, {
            "title": "신청일시",
            "width": "150px",
            "type": "mapping",
            "name": "z-sd-request-date",
            "dataType": "dateTime" 
          }, {
            "title": "완료 희망일시",
            "width": "150px",
            "type": "mapping",
            "name": "z-sd-request-deadline",
            "dataType": "dateTime" 
          }, {
            "title": "상태",
            "width": "150px",
            "type": "field",
            "name": "instance_status",
            "dataType": "string" 
          }, {
            "title": "PL",
            "width": "150px",
            "type": "mapping",
            "name": "",
            "dataType": "string" 
          }, {
            "title": "신청자",
            "width": "150px",
            "type": "mapping",
            "name": "z-sd-requester",
            "dataType": "string" 
          }, {
            "title": "난이도",
            "width": "80px",
            "type": "mapping",
            "name": "",
            "dataType": "string" 
          }, {
            "title": "문서번호",
            "width": "300px",
            "type": "field",
            "name": "document_no",
            "dataType": "string" 
          }
        ]
      }
    }
  ]
}
```  


