package co.brainz.cmdb.ci.constants

object CIConstants {
    /**
     * CI Status.
     */
    enum class CIStatus(val code: String) {
        STATUS_USE("use"),
        STATUS_DELETE("delete")
    }
}
