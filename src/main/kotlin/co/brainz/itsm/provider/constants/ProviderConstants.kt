package co.brainz.itsm.provider.constants

object ProviderConstants {

    enum class Form(val url: String, val method: String) {
        GET_FORM_LIST("/rest/wf/forms", "GET"),
        GET_FORM("/rest/wf/forms/{formId}", "GET"),
        POST_FORM("/rest/wf/forms/{formId}", "POST"),
        PUT_FORM("/rest/wf/forms/{formId}", "PUT"),
        DELETE_FORM("/rest/wf/forms/{formId}", "DELETE")
    }

    enum class Protocol(val value: String) {
        HTTPS("https"),
        HTTP("http")
    }
}
