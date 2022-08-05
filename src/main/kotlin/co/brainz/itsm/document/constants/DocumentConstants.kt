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
     * document type p_code.
     */
    const val DOCUMENT_TYPE_P_CODE = "document.type"

    /**
     * default document icon.
     */
    const val DEFAULT_DOCUMENT_ICON = "img_document_01.png"

    /**
     * document type.
     *
     * @param value
     */
    enum class DocumentType(val value: String) {
        APPLICATION_FORM("document.type.application-form"),
        WORKFLOW("document.type.workflow"),
        APPLICATION_FORM_WORKFLOW("document.type.application-form-workflow"),
        APPLICATION_FORM_LINK("document.type.application-form-link")
    }

    /**
     * document view type.
     */
    enum class DocumentViewType(val value: String) {
        ADMIN("admin")
    }

    /**
     * document displayType p_code.
     */
    const val DOCUMENT_DISPLAY_TYPE_P_CODE = "document.displayType"

    /**
     * document status.
     */
    enum class DocumentStatus(val value: String) {
        USE("document.status.use"),
        TEMPORARY("document.status.temporary")
    }
}
