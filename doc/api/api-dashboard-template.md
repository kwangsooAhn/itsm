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
          "chartId": "4028b22f7c2fefef017c30054c900002",
          "chartName": "",
          "chartType": "chart.basicColumn",
          "chartDesc": "",
          "tags": [
            { "tagId": "2c9180867cc31a25017cc7a68eec0511", "value": "단순의뢰" }
          ],
          "chartConfig": {  },
          "chartData": [{  }]
      }
    }, {
      "key": "requestStatusByUser.list",
      "title": "개인 요청 현황",
      "target": [{
          "documentID": "4028b22f7c2fefef017c30054c9000c9",
          "documentName": "장애신고서",
          "count": 2
      },{
          "documentID": "4028b22f7c2fefef017c30054c900008",
          "documentName": "IT서비스 요청서서",
          "count": 3
      }] 
    }, {
      "key": "requestListByOrganization.list",
      "title": "요청현황",
      "target": {
        "organizationId" : "4028b2d57d37168e017d371a5f7f0004",
        "organizationName" : "리스크관리본부",
        "columnTitle" : ["순번", "신청부서", "문서종류", "제목", "신청일", "완료 희망일", "상태", "PL", " 신청자", "난이도", "문서번호"],
        "columnWidth" : ["60px", "200px", "200px", "300px", "150px", "150px", "150px", "150px", "150px", "80px", "300px"],
        "columnType" : ["string", "string", "string", "date", "date", "string", "string","string", "string"],
        "contents" : {
            "0": ["리스크 관리부", "개발의뢰신청서", "이메일이 안됩니다.", "2021-02-05", "2021-02-05", "신청서 접수", "정희찬", "상", "CSR-20220117-001"],
            "1": ["리스크 관리부", "개발의뢰신청서", "이메일이 안됩니다.", "2021-02-12", "2021-02-12", "신청서 접수", "정희찬", "상", "CSR-20220117-002"]
        }
      }
    }
  ]
}
```  


