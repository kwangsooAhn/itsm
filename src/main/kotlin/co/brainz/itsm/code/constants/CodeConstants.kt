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
        STATUS_SUCCESS("Z-0000"),
        STATUS_ERROR_CODE_DUPLICATION("E-0001"),
        STATUS_ERROR_CODE_P_CODE_NOT_EXIST("E-0003"),
        STATUS_ERROR_CODE_P_CODE_USED("E-0004")
    }
}
