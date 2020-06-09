package co.brainz.workflow.form.constants

object WfFormConstants {

    /**
     * Form 관련 상수를 정의한 클래스.
     */
    enum class FormLang(val value: String) {
        /** 지원 언어 부모 코드 */
        P_CODE("form.lang")
    }

    /**
     * Form Status.
     *
     * @param value
     */
    enum class FormStatus(val value: String) {
        EDIT("form.status.edit"),
        PUBLISH("form.status.publish"),
        USE("form.status.use"),
        DESTROY("form.status.destroy")
    }

    /**
     * Form SaveType.
     *
     * @param value
     */
    enum class FormSaveType(val value: String) {
        SAVE_AS("saveas")
    }
}
