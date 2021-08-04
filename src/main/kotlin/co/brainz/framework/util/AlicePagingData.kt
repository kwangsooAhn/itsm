/**
 * 페이징 리스트 화면에서 사용하는 공통 정보
 *
 *  - [페이징 리스트] 타입의 화면에서 검색 결과를 화면에 전송할때 같이 전송한다.
 *  - [페이징 리스트] 타입 화면의 디자인과 동작이 변경되면 같이 변경될 여지가 있다.
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.framework.util;

import java.io.Serializable

/**
 * @param totalCount : 검색 결과에 대한 전체 건수
 * @param totalCountWithoutCondition : 검색조건과 전체 데이터 건수
 * @param currentPageNum : 현재 페이지 번호
 * @param totalPageNum : 전체 페이지 번호
 * @param orderType : 리스트 목록의 정렬 순서. (이건 사실 여기 성격과 안맞지만 1개라서 여기서 관리. 관련 정보가 많아지면 분리)
 */
data class AlicePagingData(
    var totalCount: Long = 0L,
    var totalCountWithoutCondition: Long = 0L,
    var currentPageNum: Long = 0L,
    var totalPageNum: Long = 0L,
    var orderType: String?
) : Serializable