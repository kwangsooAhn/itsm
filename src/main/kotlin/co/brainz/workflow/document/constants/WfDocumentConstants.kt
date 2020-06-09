package co.brainz.workflow.document.constants

class WfDocumentConstants {

    /**
     * Document Status.
     *
     */
    enum class Status(val code: String) {
        TEMPORARY("document.status.temporary"),
        USE("document.status.use"),
        DESTROY("document.status.destroy")
    }

    /**
     * Display Type.
     *
     */
    enum class DisplayType(val value: String) {
        EDITABLE("editable"),
        EDITABLE_REQUIRED("editableRequired"),
        READONLY("readonly"),
        HIDDEN("hidden")
    }
}
