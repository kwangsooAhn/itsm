package co.brainz.itsm.document.constants

object DocumentConstants {

    /**
     * document status p_code.
     */
    const val DOCUMENT_STATUS_P_CODE = "document.status"

    /**
     * document type.
     *
     * @param value
     */
    enum class DocumentType(val value: String) {
        APPLICATION_FORM("application-form"),
        WORKFLOW("workflow")
    }
}
