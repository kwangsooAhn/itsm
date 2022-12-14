package co.brainz.workflow.instance.constants

import co.brainz.workflow.token.constants.WfTokenConstants

class WfInstanceConstants {

    companion object {
        fun getTargetStatusGroup(searchType: WfTokenConstants.SearchType): List<String>? {
            return when (searchType) {
                WfTokenConstants.SearchType.TODO -> listOf(InstanceStatus.RUNNING.code)
                WfTokenConstants.SearchType.REQUESTED -> null
                WfTokenConstants.SearchType.PROGRESS -> listOf(InstanceStatus.RUNNING.code, InstanceStatus.WAITING.code)
                WfTokenConstants.SearchType.COMPLETED -> listOf(InstanceStatus.FINISH.code)
                WfTokenConstants.SearchType.STORED -> null
            }
        }

        const val TOKEN_DATA_DEFAULT: String = "\${default}"
    }

    /**
     * Order Column
     */
    enum class OrderColumn(val code: String) {
        CREATE_USER_NAME("createUserName"),
        DOCUMENT_GROUP("documentGroup"),
        ASSIGNEE_USER_NAME("assigneeUserName"),
        ELEMENT_NAME("elementName"),
        DOCUMENT_NO("documentNo"),
        INSTANCE_START_DT("instanceStartDt")
    }
}

/**
 * Instance Status.
 */
enum class InstanceStatus(val code: String) {
    RUNNING("running"),
    WAITING("waiting"),
    FINISH("finish")
}
