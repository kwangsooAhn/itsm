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
     * Subprocess Search type.
     */
    enum class DocumentViewType(val value: String) {
        SUBPROCESS("subProcess")
    }
    /**
     * Document Type.
     *
     */
    enum class WorkflowType(val value: String) {
        WORKFLOW("workflow"),
        APPLICATION_FORM_WORKFLOW("application-form-workflow")
    }

    /**
     * Display Type.
     *
     */
    enum class DisplayType(val value: String) {
        EDITABLE("document.displayType.editable"),
        READONLY("document.displayType.readonly"),
        HIDDEN("document.displayType.hidden")
    }
}
