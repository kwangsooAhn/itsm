package co.brainz.framework.auth.constants

object AuthConstants {
    /**
     * auth type.
     */
    enum class AuthType(val value: String) {
        CANCEL("workflow.expire"),
        DOCUMENT_VIEW("document.view"),
        PORTAL_MANAGE("portal.manage"),
        WORKFLOW_MANAGE("workflow.manage")
    }
}
