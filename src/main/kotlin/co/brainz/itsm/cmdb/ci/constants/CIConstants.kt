/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.constants

object CIConstants {

    /**
     * CI 컴포넌트 - Action Type
     */
    enum class ActionType(val code: String) {
        REGISTER("register"),
        MODIFY("modify"),
        DELETE("delete"),
        READ("read")
    }

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_CLASS("1")
    }

    /**
     * CI 용량 태그 정보
     */
    enum class CapacityTag(val code: String) {
        MEMORY("Memory"),
        CPU("CPU"),
        DISK("Disk")
    }

    /**
     * 용량 차트 기간 설정
     * 기간을 변경하려면 시간을 기준으로 변경해야한다.
     */
    const val period = 6 * 24
}
