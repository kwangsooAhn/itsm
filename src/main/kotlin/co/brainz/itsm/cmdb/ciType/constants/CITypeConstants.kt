package co.brainz.itsm.cmdb.ciType.constants

object CITypeConstants {

    enum class Status(val code: String) {
        STATUS_FAIL("E-0000"),
        STATUS_SUCCESS("Z-0000"),
        STATUS_SUCCESS_EDIT_TYPE("Z-0000"),
        STATUS_FAIL_TYPE_ALIAS_DUPLICATION("E-0001"),
        STATUS_FAIL_PTYPE_AND_TYPENAME_DUPLICATION("E-0004")
    }
}
