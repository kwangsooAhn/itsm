package co.brainz.itsm.provider.constants

object ProviderConstants {

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
        DELETE_FORM("/rest/wf/forms/{formId}", "DELETE")
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
     * Workflow Url.
     *
     * @param url
     * @param method
     */
    enum class Workflow(val url: String, val method: String) {
        GET_TICKETS("/rest/wf/tickets", "GET"),
        GET_TICKET("/rest/wf/tickets/{ticketsId}", "GET"),
        GET_INSTANCES("/rest/wf/instances", "GET"),
        GET_INSTANCE("/rest/wf/instances/{instanceId}", "GET"),
        POST_INSTANCE("/rest/wf/instances", "POST"),
        PUT_INSTANCE("/rest/wf/instances/{instanceId}", "PUT"),
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
        PUBLISH("publish"),
        DESTROY("destroy")
    }

}
