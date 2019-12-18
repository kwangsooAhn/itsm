package co.brainz.itsm.certification

enum class UserStatus(val code: String, val value: Int) {
    SIGNUP("user.status.signup", 0),
    CERTIFIED("user.status.certified", 1),
    OVER("user.status.over", 2),
    ERROR("user.status.error", 3)
}

enum class DefaultRole(val code: String) {
    USER_DEFAULT_ROLE("user.default.role")
}

enum class SignUpStatus(val code: String) {
    STATUS_SUCCESS("0"),
    STATUS_ERROR_USER_ID_DUPLICATION("1"),
    STATUS_ERROR("2")
}
