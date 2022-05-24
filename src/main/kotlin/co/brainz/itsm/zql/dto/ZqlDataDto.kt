/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql.dto

import java.time.LocalDateTime

/**
 * ZQL 에서 최종적으로 반환되는 데이터 포맷.
 *
 * 데이터 간격에 따라 카테고리화된 계산 결과가 담겨 있다.
 * 예를 들어 MONTH 간격, 합산방식으로 기간이 1년이라면 12개의 월별일시와 해당 월의 합산 결과가 포함되어 리턴되는데
 * 이 클래스는 그 12개의 데이터 각각의 데이터 포맷을 표현하고, 실제 ZQL 의 리턴은 12개의 리스트로 반환된다.
 */
data class ZqlCalculatedData(
    val categoryDT: LocalDateTime,
    var value: Float
)

/**
 * ZQL 표현식과 설정에 따라 카테고리로 분류된 데이터 포맷.
 *
 * 카테고리와 그에 해당되는 실제 토큰데이터 목록을 포함한다.
 * 예를 들어 월 견격으로 설정되었다면 카테고리는 매월 1일자로 날짜 기준을 삼고 해당 토큰의 리스트를 가진다.
 * 이 토큰 리스트는 ZQL 표현식에서 사용된 태그를 포함하고 있으며, 기간 정보와 인스턴스 상태를 감안해서 가져온 토큰 목록이다.
 */
data class ZqlCategorizedData(
    val categorizedDT: LocalDateTime,
    val zqlTokenList: MutableList<ZqlToken>
)

/**
 * 태그 텍스트와 그 태그를 달고 있는 실제 컴포넌트의 값을 나타내는 데이터 구조.
 *
 * 태그는 같은 텍스트의 태그라도 다른 컴포넌트에 달리면 다른 태그 아이디를 가진다.
 * 실제 ZQL 내부에서는 태그의 아이디는 필요없고 태그 아이디가 다르더라도 실제 사용된 태그 텍스트에 따라 해당 컴포넌트의 값을
 * 매핑해서 사용해야 한다.
 *
 * 1개의 문서에 같은 태그가 여러개 달려 있는 경우(실제로는 그렇게 태그를 달진 않겠지만)에는 첫 번째로 발견되는 컴퍼넌트의 값을 사용한다.
 */
data class ZqlComponentValue(
    val tagValue: String,
    val componentValue: String
)

/**
 * 가장 로우 데이터인 기준일시, 토큰, 태그와 컴포넌트의 값을 포함하는 데이터 구조.
 *
 * ZQL 처리 시 가장 먼저 필요한 것은 ZQL 표현식에서 사용된 태그를 포함하는 토큰들을 찾는 것이다.
 * 이때 기준일시, 실제 태그와 그 값들을 같이 찾아서 이후 표현식에서 태그 대신 치환해서 사용할 수 있도록 한다.
 */
data class ZqlToken(
    val tokenId: String,
    val criteriaDT: LocalDateTime,
    val tagValues: Map<String, String>
)
