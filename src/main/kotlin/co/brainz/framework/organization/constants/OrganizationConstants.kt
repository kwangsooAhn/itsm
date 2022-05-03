package co.brainz.framework.organization.constants

class OrganizationConstants {

    enum class Status(val code: String) {
        STATUS_SUCCESS("Z-0000"),
        STATUS_ERROR_USER_EXIST("E-0004"),
        STATUS_ERROR_SUB_DEPT_EXIST("E-0007") // 부서에서만 사용하는 코드 (하위부서가 존재할 경우 삭제 불가능)
    }
}
