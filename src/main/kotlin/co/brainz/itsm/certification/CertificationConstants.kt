package co.brainz.itsm.certification

/**
 * 회원 가입 관련 상수를 정의한 클래스
 */
enum class CertificationConstants(val code: String) {
    USER_DEFAULT_ROLE("user.default.role"),
    /**
     * 가입 상태 코드.
     */
    STATUS_SUCCESS("0"),
    STATUS_ERROR_USER_ID_DUPLICATION("1"),
    STATUS_ERROR("2")
}