package co.brainz.framework.organization.constants

class OrganizationConstants {

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_ERROR_USER_EXIST("1"),
        STATUS_ERROR_SUB_DEPT_EXIST("2")
    }
}
