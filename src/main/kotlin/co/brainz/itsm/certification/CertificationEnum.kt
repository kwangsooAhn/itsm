package co.brainz.itsm.certification

enum class CertificationEnum(val code: String, val value: Int) {
    SIGNUP("user.status.signup", 0), CERTIFIED("user.status.certified", 1), OVER("user.status.over", 2), ERROR("user.status.error", 3);
}
