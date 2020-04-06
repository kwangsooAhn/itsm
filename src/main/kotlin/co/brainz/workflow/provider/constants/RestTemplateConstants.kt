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
        GET_FORM_DATA("/rest/wf/forms/{formId}/data", "GET"),
        POST_FORM("/rest/wf/forms", "POST"),
        POST_FORM_SAVE_AS("/rest/wf/forms?saveType=saveas", "POST"),
        PUT_FORM("/rest/wf/forms/{formId}", "PUT"),
        PUT_FORM_DATA("/rest/wf/forms/{formId}/data", "PUT"),
        DELETE_FORM("/rest/wf/forms/{formId}", "DELETE"),
        GET_FORM_COMPONENT_DATA("/rest/wf/forms/components", "GET")
    }

    /**
     * Form SaveType.
     *
     * @param code
     */
    enum class FormSaveType(val code: String) {
        SAVE_AS("saveas")
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
        GET_PROCESS_DATA("/rest/wf/processes/{processId}/data", "GET"),
        POST_PROCESS("/rest/wf/processes", "POST"),
        POST_PROCESS_SAVE_AS("/rest/wf/processes?saveType=saveas", "POST"),
        PUT_PROCESS("/rest/wf/processes/{processId}", "PUT"),
        PUT_PROCESS_DATA("/rest/wf/processes/{processId}/data", "PUT"),
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
        POST_DOCUMENT("/rest/wf/documents", "POST"),
        DELETE_DOCUMENT("/rest/wf/documents/{documentId}", "DELETE"),
        GET_INSTANCES("/rest/wf/instances", "GET"),
        GET_INSTANCES_COUNT("/rest/wf/instances/count", "GET"),
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
     * Process Status.
     *
     * @param value
     */
    enum class ProcessStatus(val value: String) {
        EDIT("process.status.edit"),
        SIMULATION("process.status.simu"),
        PUBLISH("process.status.publish"),
        DESTROY("process.status.destroy")
    }

    /**
     * Process SaveType.
     */
    enum class ProcessSaveType(val code: String) {
        SAVE_AS("saveas")
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
