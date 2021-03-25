package co.brainz.cmdb.constants

object RestTemplateConstants {

    /**
     * CMDB 처리 상태.
     */
    enum class Status(val code: String) {
        STATUS_VALID_SUCCESS("-1"),
        STATUS_SUCCESS("0"),
        STATUS_ERROR_DUPLICATION("1"),
        STATUS_ERROR_NOT_EXIST("2")
    }

    /**
     * CMDB Object.
     */
    enum class CmdbObject(val value: String) {
        ATTRIBUTE("attribute"),
        CI("ci"),
        CIDetail("ciDetail"),
        CLASS("class"),
        TYPE("type")
    }

    /**
     * CI Status.
     */
    enum class CIStatus(val code: String) {
        STATUS_USE("use"),
        STATUS_DELETE("delete")
    }

    /**
     * CI ActionType.
     */
    enum class ActionType(val code: String) {
        REGISTER("register"),
        MODIFY("modify"),
        DELETE("delete")
    }
}
