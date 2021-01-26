package co.brainz.itsm.cmdb.ciType.constants

object CITypeConstants {

    /**
     * CMDB type group p_code.
     */
    const val CMDB_TYPE_P_CODE = "cmdb.type"

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_CLASS("1")
    }
}
