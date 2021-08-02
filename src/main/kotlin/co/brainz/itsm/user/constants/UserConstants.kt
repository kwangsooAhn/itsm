package co.brainz.itsm.user.constants

/**
 * 사용자 관련 상수를 정의한 클래스
 */
enum class UserConstants(val value: String) {

    /** 검색 selectbox 에 사용할 부모 코드 */
    PCODE("user.search"),

    /** 사용자 테마 사용할 부모 코드 */
    PTHEMECODE("user.theme"),

    /** 사용자 언어 사용할 부모 코드 */
    PLANGCODE("user.lang"),

    /** 사용자 날짜 형식 사용할 부모 코드 */
    PDATECODE("user.date"),

    /** 사용자 시간 형식 사용할 부모 코드 */
    PTIMECODE("user.time"),

    /** 부서 관리 사용할 부모 코드 */
    PDEPTCODE("department.group"),

    DEFAULT_TIMEZONE("Asia/Seoul")
    ;

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

            /**
             * JPA Entity 이름으로 code 가져오기
             */
            fun getUserColumnToCode(column: String): String {
                for (key in values()) {
                    if (column == key.column) {
                        return key.code
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
        EDIT_SELF("editself")
    }

    /**
     * 사용자 정의 타입.
     */
    enum class UserCustom(val code: String) {
        COLOR("color")
    }

    /**
     *  PW 변경 결과 정의
     */
    enum class UserUpdatePassword(val code: Long) {
        SUCCESS(0),
        SAME_AS_CURRENT_PASSWORD(1),
        NOT_EQUAL_NOW_PASSWORD(2)
    }
}
