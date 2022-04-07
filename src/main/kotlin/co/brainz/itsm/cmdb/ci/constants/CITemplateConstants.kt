/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.constants

object CITemplateConstants {

    /**
     * CI 일괄 등록 템플릿 CI 기본 속성
     */
    enum class BasicProperties(val value: String) {
        TYPE_NAME("CI 유형"),
        CI_NAME("CI 이름"),
        CI_DESC("CI 설명")
    }
}
