package co.brainz.workflow.element.constants

object WfElementConstants {

    /**
     * Script Task에서 선택한 파일의 Root 디렉토리.
     */
    const val SCRIPT_FILE_PATH = "document"

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
        ID("id"),
        IS_DEFAULT("is-default"),
        SCRIPT_TYPE("script-type"),
        TARGET_MAPPING_ID("target-mapping-id"),
        SOURCE_MAPPING_ID("source-mapping-id"),
        SCRIPT_DETAIL("script-detail"),
        SCRIPT_ACTION("script-action"),
        CONDITION("condition"),
        FILE("file"),
        ACTION("action"),
        AUTO_COMPLETE("auto-complete")
    }

    /**
     * Script Task - Script Type.
     */
    enum class ScriptType(val value: String) {
        DOCUMENT_ATTACH_FILE("script.type.document.attachFile"),
        DOCUMENT_CMDB("script.type.cmdb"),
        DOCUMENT_PLUGIN("script.type.plugin")
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
        CLOSE("close"),
        REVIEW("review")
        ;

        companion object {
            fun isApplicationAction(actionValue: String): Boolean {
                return when (actionValue) {
                    SAVE.value,
                    REJECT.value,
                    WITHDRAW.value,
                    CANCEL.value,
                    TERMINATE.value,
                    REVIEW.value -> true
                    else -> false
                }
            }
        }
    }

    /**
     * 엘리먼트 데이터에 사용하는 고정값.
     */
    enum class AttributeValue(val value: String) {
        ACTION("#{action}"),
        WITHDRAW_ENABLE("Y"),
        IS_DEFAULT_ENABLE("Y")
    }

    /**
     * 컨넥터 엘리먼트에서 사용하는 condition 속성 종류
     */
    enum class ConnectorConditionValue(val value: Int) {
        /** attribute 데이터 없음 */
        NONE(0),

        /** condition-value 속성 */
        CONDITION(1),

        /** action-name & action-value 속성 */
        ACTION(10),

        /** condition-value 와 (action-name & action-value) 중복됨 */
        DUPLICATION(11)
    }
}
