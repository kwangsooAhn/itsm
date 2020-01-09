package co.brainz.itsm.certification.constants

import co.brainz.framework.constants.AliceConstants

object CertificationConstants {

    /**
     * 사용자 상태.
     */
    enum class Status(val code: String, val value: Int): AliceConstants.UserEnum {
        SIGNUP("user.status.signup", 0),
        OVER("user.status.over", 2),
        ERROR("user.status.error", 3)
    }

    enum class DefaultRole(val code: String) {
        USER_DEFAULT_ROLE("user.default.role")
    }

    enum class SignUpStatus(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_USER_ID_DUPLICATION("1"),
        STATUS_ERROR("2"),
        STATUS_ERROR_EMAIL_DUPLICATION("3")
    }

}
