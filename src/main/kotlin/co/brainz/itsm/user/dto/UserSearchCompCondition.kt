/**
 * 사용자 컴포넌트 검색 조건용 데이터 클래스
 *
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.user.dto

import java.io.Serializable

data class UserSearchCompCondition(
    val searchValue: String? = null,
    val targetCriteria: String? = null,
    val searchKeys: String = ""
) : Serializable
