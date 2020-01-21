package co.brainz.framework.constants

/**
 * 사용자 기본 코드.
 */
object UserConstants {

    /**
     * CREATE USER ID 기본값.
     */
    const val CREATE_USER_ID: String = "system"

    /**
     * 사용자 계정 만료일자 범위 설정값.
     */
    const val USER_EXPIRED_VALUE: Long = 3

    /**
     * 사용자 기본 언어셋.
     */
    const val USER_LOCALE_LANG: String = "ko"

    /**
     * 사용자 상태.
     */
    enum class Status(val code: String, val value: Int) {
        SIGNUP("user.status.signup", 0),
        CERTIFIED("user.status.certified", 1),
        OVER("user.status.over", 2),
        ERROR("user.status.error", 3),
        EDIT("user.status.edit",4)
    }

    /**
     * 사용자 등록 상태.
     */
    enum class SignUpStatus(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_USER_ID_DUPLICATION("1"),
        STATUS_ERROR("2"),
        STATUS_ERROR_EMAIL_DUPLICATION("3")
    }
    
    /**
     * 사용자 수정 상태
     */
    enum class UserEditStatus(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_USER_ID_DUPLICATION("1"),
        STATUS_ERROR("2"),
        STATUS_ERROR_EMAIL_DUPLICATION("3"),
        STATUS_SUCCESS_EDIT_EMAIL("4")
    }

    /**
     * 사용자 기본 권한.
     */
    enum class DefaultRole(val code: String) {
        USER_DEFAULT_ROLE("user.default.role")
    }

    /**
     * 사용자 접근 플랫폼.
     */
    enum class Platform(val code: String, val value: String) {
        ALICE("user.platform.alice", "alice"),
        GOOGLE("user.platform.google", "google"),
        FACEBOOK("user.platform.facebook", "facebook"),
        KAKAO("user.platform.kakao", "kakao")
    }

}
