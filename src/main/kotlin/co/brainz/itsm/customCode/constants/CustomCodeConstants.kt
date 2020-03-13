package co.brainz.itsm.customCode.constants

object CustomCodeConstants {

    /**
     * 사용자 정의 코드 편집 상태.
     */
    enum class Status(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_CUSTOM_CODE_NAME_DUPLICATION("1"),
        STATUS_ERROR_TARGET_TABLE_NOT_EXIST("2"),
        STATUS_ERROR_KEY_COLUMN_NOT_EXIST("3"),
        STATUS_ERROR_VALUE_COLUMN_NOT_EXIST("4")
    }
}