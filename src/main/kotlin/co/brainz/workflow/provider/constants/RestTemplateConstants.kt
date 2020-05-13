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
        GET_PROCESS_SIMULATION("/rest/wf/processes/{processId}/simulation", "GET"),
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
        POST_TOKEN("/rest/wf/tokens", "POST"),
        PUT_TOKEN("/rest/wf/tokens/{tokenId}", "PUT"),
        GET_TOKEN("/rest/wf/tokens/{tokenId}", "GET"),
        GET_TOKENS("/rest/wf/tokens", "GET"),
        GET_TOKEN_DATA("/rest/wf/tokens/{tokenId}/data", "GET")
    }

    /**
     * Comment Url.
     *
     * @param url
     * @param method
     */
    enum class Comment(val url: String, val method: String) {
        POST_COMMENT("/rest/wf/comments", "POST"),
        DELETE_COMMENT("/rest/wf/comments/{commentId}", "DELETE")
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
        GET_DOCUMENT_DATA("/rest/wf/documents/{documentId}/data", "GET"),
        POST_DOCUMENT("/rest/wf/documents", "POST"),
        PUT_DOCUMENT("/rest/wf/documents/{documentId}", "PUT"),
        DELETE_DOCUMENT("/rest/wf/documents/{documentId}", "DELETE"),
        GET_DOCUMENTS_DISPLAY("/rest/wf/documents/{documentId}/display", "GET"),
        PUT_DOCUMENTS_DISPLAY("/rest/wf/documents/{documentId}/display", "PUT"),
        GET_INSTANCES("/rest/wf/instances", "GET"),
        GET_INSTANCES_COUNT("/rest/wf/instances/count", "GET"),
        TASK_COMPLETE("/rest/wf/tasks/{instanceId}/complete", "PUT"),
        TASK_GATEWAY("/rest/wf/tasks/{instanceId}/gateway", "PUT")
    }

    /**
     * Instance Url.
     */
    enum class Instance(val url: String, val method: String) {
        GET_INSTANCE_HISTORY("/rest/wf/instances/{instanceId}/history", "GET"),
        GET_RELATED_INSTANCE("/rest/wf/folders", "GET"),
        GET_ALL_INSTANCE("/rest/wf/folders/list", "GET"),
        GET_INSTANCE_COMMENTS("/rest/wf/instances/{instanceId}/comments", "GET")
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
        PUBLISH("form.status.publish"),
        USE("form.status.use"),
        DESTROY("form.status.destroy")
    }

    /**
     * Process Status.
     *
     * @param value
     */
    enum class ProcessStatus(val value: String) {
        EDIT("process.status.edit"),
        PUBLISH("process.status.publish"),
        USE("process.status.use"),
        DESTROY("process.status.destroy")
    }

    /**
     * Document Status.
     *
     * @param value
     */
    enum class DocumentStatus(val value: String) {
        TEMPORARY("document.status.temporary"),
        USE("document.status.use"),
        DESTROY("document.status.destroy")
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

    /**
     * 이미지 컴포넌트 디렉터리.
     */
    const val RESOURCES_DIR: String = "src/main/resources/public"
    const val FORM_IMAGE_DIR: String = "assets/media/image/form"
}
