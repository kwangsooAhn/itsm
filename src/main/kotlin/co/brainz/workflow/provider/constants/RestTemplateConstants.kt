package co.brainz.workflow.provider.constants

object RestTemplateConstants {

    /**
     * Form Url.
     *
     * @param url
     * @param method
     */
    enum class Form(val url: String, val method: String) {
        GET_FORMS("/rest/wf/forms", "GET"),
        GET_FORM("/rest/wf/forms/{formId}", "GET"),
        POST_FORM("/rest/wf/forms", "POST"),
        PUT_FORM("/rest/wf/forms/{formId}", "PUT"),
        POST_FORM_SAVE_AS("/rest/wf/forms/{formId}", "POST"),
        DELETE_FORM("/rest/wf/forms/{formId}", "DELETE"),
        GET_FORM_COMPONENT_DATA("/rest/wf/forms/components", "GET")
    }

    /**
     * Process Url.
     *
     * @param url
     * @param method
     */
    enum class Process(val url: String, val method: String) {
        GET_PROCESSES("/rest/wf/processes", "GET"),
        GET_PROCESS("/rest/wf/processes/{processId}", "GET"),
        POST_PROCESS("/rest/wf/processes", "POST"),
        PUT_PROCESS("/rest/wf/processes/{processId}", "PUT"),
        DELETE_PROCESS("/rest/wf/processes/{processId}", "DELETE")
    }

    /**
     * Token Url.
     *
     * @param url
     * @param method
     */
    enum class Token(val url: String, val method: String) {
        POST_TOKEN_DATA("/rest/wf/tokens", "POST"),
        PUT_TOKEN_DATA("/rest/wf/tokens/{tokenId}/data", "PUT"),
        PUT_TOKEN("/rest/wf/tokens/{tokenId}", "PUT"),
        GET_TOKEN("/rest/wf/tokens/{tokenId}", "GET"),
        GET_TOKENS("/rest/wf/tokens", "GET"),
        GET_TOKEN_DATA("/rest/wf/tokens/{tokenId}/data", "GET")
    }

    /**
     * Workflow Url.
     *
     * @param url
     * @param method
     */
    enum class Workflow(val url: String, val method: String) {
        GET_DOCUMENTS("/rest/wf/documents", "GET"),
        GET_DOCUMENT("/rest/wf/documents/{documentId}", "GET"),
        GET_INSTANCES("/rest/wf/instances", "GET"),
        TASK_COMPLETE("/rest/wf/tasks/{instanceId}/complete", "PUT"),
        TASK_GATEWAY("/rest/wf/tasks/{instanceId}/gateway", "PUT")
    }

    /**
     * Protocol.
     *
     * @param value
     */
    enum class Protocol(val value: String) {
        HTTPS("https"),
        HTTP("http")
    }

    /**
     * Form Status.
     *
     * @param value
     */
    enum class FormStatus(val value: String) {
        EDIT("form.status.edit"),
        SIMULATION("form.status.simu"),
        PUBLISH("form.status.publish"),
        DESTROY("form.status.destroy")
    }

    /**
     * Token Status.
     *
     * @param value
     */
    enum class TokenStatus(val value: String) {
        RUNNING("running"),
        WAITING("waiting"),
        FINISH("finish")
    }
}
