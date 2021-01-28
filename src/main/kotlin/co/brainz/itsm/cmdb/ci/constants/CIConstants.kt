package co.brainz.itsm.cmdb.ci.constants

object CIConstants {

    /**
     * CI 컴포넌트 - Action Type
     */
    enum class ActionType(val code: String) {
        REGISTER("register"),
        MODIFY("modify"),
        DELETE("delete"),
        READ("read")
    }

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_CLASS("1")
    }

}
