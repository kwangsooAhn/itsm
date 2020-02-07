package co.brainz.itsm.provider.constants

object ProviderConstants {

    enum class Form(val url: String, val method: String) {
        GET_FORM_LIST("/rest/wf/forms", "GET"),
        GET_FORM("/rest/wf/forms/{formId}", "GET"),
        POST_FORM("/rest/wf/forms", "POST"),
        PUT_FORM("/rest/wf/forms/{formId}", "PUT"),
        DELETE_FORM("/rest/wf/forms/{formId}", "DELETE")
    }

    enum class Process(val url: String, val method: String) {
        GET_PROCESS_LIST("/rest/wf/processes", "GET"),
        GET_PROCESS("/rest/wf/processes/{processId}", "GET"),
        POST_PROCESS("/rest/wf/processes", "POST"),
        PUT_PROCESS("/rest/wf/processes/{processId}", "PUT"),
        DELETE_PROCESS("/rest/wf/processes/{processId}", "DELETE")
    }

    enum class Protocol(val value: String) {
        HTTPS("https"),
        HTTP("http")
    }
}
