package co.brainz.itsm.form.constants

/**
 * Form 관련 상수.
 */
object FormConstants {

    /**
     * Form 지원 언어.
     */
    const val FORM_LANG: String = "form.lang"

    /**
     * Form 상태.
     */
    enum class Status(val code: String) {
        EDIT("form.status.edit"),
        SIMULATION("form.status.simu"),
        PUBLISH("form.status.publish"),
        DESTROY("form.status.destroy")
    }
}