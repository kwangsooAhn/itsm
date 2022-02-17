# 대시보드 - 템플릿

사용자가 정의한 형태의 템플릿을 바탕으로 대시보드 화면을 제공한다.

## 목차

---

1. [데이터 조회](#데이터-조회)

## 데이터 조회

---

개인현황판은 설정 데이터를 사용하지만 설정용 화면은 현재 단계에서 제외되었다. (2022-02 기준) 
설정화면을 통해서 데이터가 들어간 상태로 가정하고 화면 출력을 구성한다.

### URL
```
GET /dashboard/view
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
          // 부서별 요청현황 참고
          "chartId": "4028b22f7c2fefef017c30054c900002", // comoponent_id와 동일
          "chartName": "",
          "chartType": "chart.basicColumn",
          "chartDesc": "",
          "tags": [
            { "tagId": "2c9180867cc31a25017cc7a68eec0511", "value": "단순의뢰" },
            ...
          ],
          "chartConfig": { ... },
          "chartData": [{ ... }]
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
      // 설계 중 
      "key": "requestListByOrganization.list",
      "title": "요청현황",
      "target": {
        "item": [
          {
            "title": "신청부서",
            "width": "200px",
            "type": "tag",
            "name": "신청자부서명" 
          }, {
            "title": "제목",
            "width": "20%",
            "type": "tag",
            "name": "신청서제목" 
          }, {
            "title": "문서종류",
            "type": "field",
            "name": "document_name" 
          }, {
            "title": "상태",
            "type": "field",
            "name": "instance_status" 
          }, {
            "title": "문서번호",
            "type": "field",
            "name": "document_no" 
          }
        ]
      }
    }
  ]
}
```  

#### 부서별 요청현황

부서별 요청현황 컴포넌트는 Highchart - basicColumn 차트로 구성된다.
관련 데이터는 `public > assets > js > cahrt > dummy > chart.basicColumn.json` 파일을 참고한다.



