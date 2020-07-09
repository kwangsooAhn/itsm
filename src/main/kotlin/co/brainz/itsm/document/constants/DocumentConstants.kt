package co.brainz.itsm.document.constants

object DocumentConstants {

    /**
     * document status p_code.
     */
    const val DOCUMENT_STATUS_P_CODE = "document.status"

    /**
     * document group p_code.
     */
    const val DOCUMENT_GROUP_P_CODE = "document.group"

    /**
     * document type.
     *
     * @param value
     */
    enum class DocumentType(val value: String) {
        APPLICATION_FORM("application-form"),
        WORKFLOW("workflow")
    }

    /**
     * document view type.
     */
    enum class DocumentViewType(val value: String) {
        ADMIN("admin")
    }
}
