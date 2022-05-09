package co.brainz.itsm.customCode.constants

object CustomCodeConstants {

    /**
     * 컴포넌트 타입 customCode
     */
    const val COMPONENT_TYPE_CUSTOM_CODE: String = "customCode"

    /**
     * 속성 아이디 element
     */
    const val PROPERTY_ID_ELEMENT: String = "element"

    /**
     * 사용자 정의 코드 p_code.
     */
    const val CUSTOM_CODE_TYPE_P_CODE = "customCode.type"

    /**
     * 커스텀 코드 조건 연산자 p_code.
     */
    const val CUSTOM_CODE_OPERATOR_P_CODE = "customCode.operator"

    /**
     * 커스텀 코드 세션 사용 시 기본 값 p_code.
     */
    const val CUSTOM_CODE_SESSION_KEY_P_CODE = "customCode.sessionKey"

    /**
     * 사용자 정의 코드 편집 상태.
     */
    enum class Status(val code: String) {
        STATUS_VALID_SUCCESS("E-0000"),
        STATUS_SUCCESS("Z-0000"),
        STATUS_ERROR_CUSTOM_CODE_USED("E-0004"),
        STATUS_ERROR_CUSTOM_CODE_NAME_DUPLICATION("E-0001"),
        STATUS_ERROR_CUSTOM_CODE_P_CODE_NOT_EXIST("E-0003")
    }

    /**
     * 사용자 정의 코드 타입.
     */
    enum class Type(val code: String) {
        TABLE("table"),
        CODE("code")
    }

    /**
     * 사용자 정의 코드 컬럼 타입.
     */
    enum class ColumnType(val code: String) {
        SEARCH("search"),
        VALUE("value")
    }

    /**
     * 사용자 정의 코드 테이블
     */
    enum class TableName(val code: String) {
        ROLE("awf_role"),
        USER("awf_user"),
        ORGANIZATION("awf_organization")
    }
}
