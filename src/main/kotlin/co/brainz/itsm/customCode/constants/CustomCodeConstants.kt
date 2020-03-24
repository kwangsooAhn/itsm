package co.brainz.itsm.customCode.constants

object CustomCodeConstants {

    /**
     * 컴포넌트 타입 custom-code
     */
    const val COMPONENT_TYPE_CUSTOM_CODE: String = "custom-code"

    /**
     * 속성 아이디 display
     */
    const val ATTRIBUTE_ID_DISPLAY: String = "display"

    /**
     * 사용자 정의 코드 편집 상태.
     */
    enum class Status(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_CUSTOM_CODE_USED("1"),
        STATUS_ERROR_CUSTOM_CODE_NAME_DUPLICATION("2")
    }

    /**
     * 사용자 정의 코드 컬럼 타입.
     */
    enum class ColumnType(val code: String) {
        SEARCH("search"),
        VALUE("value")
    }
}