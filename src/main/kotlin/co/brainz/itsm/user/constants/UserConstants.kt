package co.brainz.itsm.user.constants

/**
 * 사용자 관련 상수를 정의한 클래스
 */
object UserConstants {

    /**
     * 사용자 타임존 기본값
     */
    const val DEFAULT_TIMEZONE = "Asia/Seoul"

    /**
     * 사용자 관련  부모 코드 정의
     */
    enum class UserPCode(val code: String) {
        P_THEME_CODE("user.theme"),
        P_LANG_CODE("user.lang"),
        P_DATE_CODE("user.date"),
        P_TIME_CODE("user.time")
    }

    /**
     * DB코드(messages.yml 과 동일한 값) - JPA Entity 맵핑 정보
     */
    enum class UserCodeAndColumnMap(val code: String, val column: String) {
        ID("user.search.id", "userId"),
        NAME("user.search.name", "userName"),
        POSITION("user.search.position", "position"),
        DEPARTMENT("user.search.department", "department"),
        OFFICE("user.search.officeNumber", "officeNumber"),
        MOBILE("user.search.mobileNumber", "mobileNumber")
        ;

        companion object {
            /**
             * code 로 JPA Entity 이름 가져오기
             */
            fun getUserCodeToColumn(code: String): String {
                for (key in values()) {
                    if (code == key.code) {
                        return key.column
                    }
                }
                return ""
            }
        }
    }

    /**
     * 사용자 설정 타입
     */
    enum class UserEdit(val code: String) {
        EDIT("edit"),
        VIEW("view"),
        EDIT_SELF("editself")
    }

    /**
     * 사용자 정의 타입.
     */
    enum class UserCustom(val code: String) {
        COLOR("color"),
        USER_ABSENCE("user_absence")
    }
}
