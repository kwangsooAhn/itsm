package co.brainz.itsm.folder.constants

class FolderConstants {
    /**
     * Instance type in folder.
     */
    enum class RelatedType(val code: String) {
        ORIGIN("origin"),
        REFERENCE("reference"),
        RELATED("related")
    }
}
