package co.brainz.workflow.engine.element.constants

object WfElementConstants {
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
        SUB_PROCESS("subprocess"),
        ANNOTATION_ARTIFACT("annotationArtifact"),
        GROUP_ARTIFACT("groupArtifact"),
        SIGNAL_SEND("signalSend"),
    }

    /**
     * Token attribute data id.
     */
    enum class AttributeId(val value: String) {
        ASSIGNEE("assignee"),
        ASSIGNEE_TYPE("assignee-type"),
        SOURCE_ID("start-id"),
        TARGET_ID("end-id"),
        CONDITION_ITEM("condition-item"),
        CONDITION_VALUE("condition-value"),
        ACTION_NAME("action-name"),
        ACTION_VALUE("action-value"),
        SAVE("save"),
        REJECT_ID("reject-id"),
        SUB_DOCUMENT_ID("sub-document-id"),
        TARGET_DOCUMENT_LIST("target-document-list")
    }

    /**
     * 엘리먼트의 condition 값 비교를 위해 정규식에 사용할 문자열
     *
     * GENERAL - ""(따옴표)로 묶인 일반 값
     * MAPPINGID - "${value}" 형태로 엘리먼트 mappingid  값
     * CONSTANT -  "#{value}" 형태로 클라이언트에서 넘어오는 action 등의 값
     */
    enum class RegexCondition(val value: String) {
        GENERAL("\\x22[^\\x22]+\\x22"),
        MAPPINGID("\\x24\\x7b[^\\x22\\x24\\x7b\\x7d]+\\x7d"),
        CONSTANT("\\x23\\x7b[^\\x22\\x24\\x7b\\x7d]+\\x7d")
    }

    /**
     * Action Type.
     */
    enum class Action(val value: String) {
        SAVE("save"),
        REJECT("reject"),
        PROCESS("process"),
        WITHDRAW("withdraw")
    }

    enum class AttributeValue(val value: String) {
        ACTION("#{action}")
    }
}
