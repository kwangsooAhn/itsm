package co.brainz.workflow.element.constants

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

        // 상위 호환, 엘리먼트 타입의 종류를 하나로 묶어서 구분하도록 한다.
        // 예를 들어 TASK - user, manual, send task등 모두 포함하는 상위 개념이다
        TASK("atomicTask"),
        GATEWAY("atomicGateway"),
        EVENT("atomicEvent"),
        ARTIFACT("atomicArtifact")
        ;

        // 엘리먼트 타입이 어떤 종류인지 해당 종류를 리턴한다.
        companion object {
            fun getAtomic(elementType: String): ElementType? {
                return when (elementType) {
                    USER_TASK.value, MANUAL_TASK.value, SEND_TASK.value, RECEIVE_TASK.value, SCRIPT_TASK.value -> TASK
                    EXCLUSIVE_GATEWAY.value, PARALLEL_GATEWAY.value, INCLUSIVE_GATEWAY.value -> GATEWAY
                    COMMON_START_EVENT.value, COMMON_END_EVENT.value, SIGNAL_SEND.value -> EVENT
                    ARROW_CONNECTOR.value -> ARROW_CONNECTOR
                    SUB_PROCESS.value -> SUB_PROCESS
                    ANNOTATION_ARTIFACT.value, GROUP_ARTIFACT.value -> ARTIFACT
                    else -> null
                }
            }
        }
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
        WITHDRAW("withdraw"),
        SUB_DOCUMENT_ID("sub-document-id"),
        TARGET_DOCUMENT_LIST("target-document-list"),
        NAME("name"),
        ID("id")
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
        PROGRESS("progress"),
        WITHDRAW("withdraw"),
        CANCEL("cancel"),
        TERMINATE("terminate"),
        CLOSE("close")
    }

    enum class AttributeValue(val value: String) {
        ACTION("#{action}"),
        WITHDRAW_ENABLE("Y")
    }
}