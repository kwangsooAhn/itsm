package co.brainz.workflow.folder.constants

class WfFolderConstants {
    /**
     * Instance type in folder.
     */
    enum class RelatedType(val code: String) {
        ORIGIN("origin"),
        REFERENCE("reference"),
        RELATED("related")
    }
}
