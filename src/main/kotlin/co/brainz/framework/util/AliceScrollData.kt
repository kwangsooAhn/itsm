/**
 * 스크롤 리스트 화면에서 사용하는 공통 정보
 *
 *  - [스크롤 리스트] 타입의 화면에서 검색 결과를 화면에 전송할때 같이 전송한다.
 *
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */
package co.brainz.framework.util

import co.brainz.framework.resourceManager.constants.ResourceConstants
import java.io.Serializable

/**
 * @param totalCount : 검색 결과에 대한 전체 건수
 * @param totalCountWithoutCondition : 검색조건과없이 전체 데이터 건수
 * @param currentScrollNum : 현재 스크롤 번호
 * @param totalScrollNum : 전체 스크롤 번호
 * @param contentNumPerScroll : 스크롤시 가져오는 건수
 */
data class AliceScrollData(
    var totalCount: Long = 0L,
    var totalCountWithoutCondition: Long = 0L,
    var currentScrollNum: Long = 0L,
    var totalScrollNum: Long = 0L,
    var contentNumPerScroll: Long = ResourceConstants.OFFSET_MODAL
) : Serializable
