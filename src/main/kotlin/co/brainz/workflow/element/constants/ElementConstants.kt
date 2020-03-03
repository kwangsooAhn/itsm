package co.brainz.workflow.element.constants

object ElementConstants {
    /**
     * Element Type.
     */
    enum class ElementType(val value: String) {
        USER_TASK("userTask"),
        MANUAL_TASK("manualTask"),
        SEND_TASK("sendTask"),
        RECEIVE_TASK("receiveTask"),
        SCRIPT_TASK("scriptTask"),
        EXCLUSIVE_GATEWAY("exclusiveGateway"),
        PARALLEL_GATEWAY("parallelGateway"),
        INCLUSIVE_GATEWAY("inclusiveGateway"),
        COMMON_START_EVENT("commonStart"),
        MESSAGE_START_EVENT("messageStart"),
        TIMER_START_EVENT("timerStart"),
        COMMON_END_EVENT("commonEnd"),
        MESSAGE_END_EVENT("messageEnd"),
        ARROW_CONNECTOR("arrowConnector"),
        COMMON_SUBPROCESS("subprocess"),
        ANNOTATION_ARTIFACT("annotationArtifact"),
        GROUP_ARTIFACT("groupArtifact")
    }

    /**
     * Token attribute data id.
     */
    enum class AttributeId(val value: String) {
        ASSIGNEE("assignee"),
        ASSIGNEE_TYPE("assignee-type"),
        SOURCE_ID("start-id"),
        TARGET_ID("end-id"),
        CONDITION("condition")
    }

    /**
     * Element mst elemType data id.
     */
    enum class ElementStatusType(val value: String) {
        START("start"),
        USER("user"),
        END("end")
    }
}