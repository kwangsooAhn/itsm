package co.brainz.itsm.cmdb.ciType.constants

object CITypeConstants {

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_CLASS("1"),
        STATUS_FAIL_TYPE_ALIAS_DUPLICATION("2"),
        STATUS_FAIL_PTYPE_AND_TYPENAME_DUPLICATION("3")
    }
    enum class Validation(val code: String) {
        VALIDATION_OK("0"),
        VALIDATION_DUPLICATION_TYPE_ALIAS("1"),
        VALIDATION_DUPLICATION_PTYPE_AND_TYPENAME("2")
    }
}
