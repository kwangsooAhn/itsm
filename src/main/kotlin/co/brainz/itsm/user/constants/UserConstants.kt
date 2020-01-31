package co.brainz.itsm.user.constants

/**
 * 사용자 관련 상수를 정의한 클래스
 */
enum class UserConstants(val value: String) {
    /** 검색 selectbox 에 사용할 부모 코드 */
    PCODE("user.search")
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
            fun getUserCodeToColum(code: String): String {
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

}

