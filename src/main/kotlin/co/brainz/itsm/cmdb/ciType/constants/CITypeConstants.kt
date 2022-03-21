package co.brainz.itsm.cmdb.ciType.constants

object CITypeConstants {

    enum class Status(val code: String) {
        STATUS_FAIL("-1"),
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_TYPE("1"),
        STATUS_FAIL_TYPE_ALIAS_DUPLICATION("2"),
        STATUS_FAIL_PTYPE_AND_TYPENAME_DUPLICATION("3")
    }
}
