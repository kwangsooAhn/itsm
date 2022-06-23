/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.zql.const

/**
 * ZQL 사용 시 인스턴스의 시작일자, 종료일자중에서 어떤 날짜를 기준으로 처리할지를 나타내는 상수이다.
 */
enum class ZqlInstanceDateCriteria {
    START, END
}

/**
 * ZQL 데이터 연산에서 해당 데이터를 어떤 간격으로 계산할지를 결정한다.
 * 예를 들어 MONTH 라면 대상 데이터를 월별로 모아서 합산,평균등을 계산한다.
 */
enum class ZqlPeriodType {
    YEAR, MONTH, DAY, HOUR
}

/**
 * ZQL 표현식 작성 시 태그는 []를 사용해서 둘러싼다.
 * 이렇게 표현된 태그를 뽑아내기 위한 정규식이다.
 */
const val REGEX_TAG = """\[(.*?)[]]"""
