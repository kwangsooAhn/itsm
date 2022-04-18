package co.brainz.itsm.cmdb.ciClass.constants

object CIClassConstants {

    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_SUCCESS_EDIT_CLASS("1"),
        STATUS_FAIL_CLASS_HAVE_TYPE("2")
    }
}
