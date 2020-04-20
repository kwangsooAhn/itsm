package co.brainz.workflow.engine.document.constants

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
}
