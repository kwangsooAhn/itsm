package co.brainz.itsm.code.constants

/**
 * 공통 상수.
 */
object CodeConstants {
    const val SEARCH_RANGE_VALUE: Long = 30

    /**
     * 코드 편집 상태.
     */
    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_CODE("1"),
        STATUS_ERROR_CODE_P_CODE_USED("2"),
        STATUS_ERROR_CODE_DUPLICATION("3"),
        STATUS_ERROR_CODE_P_CODE_NOT_EXIST("4")
    }
}
