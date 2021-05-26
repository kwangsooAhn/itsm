package co.brainz.workflow.instance.constants

import co.brainz.workflow.token.constants.WfTokenConstants

class WfInstanceConstants {

    /**
     * Instance Status.
     */
    enum class Status(val code: String) {
        RUNNING("running"),
        WAITING("waiting"),
        FINISH("finish")
    }

    companion object {
        fun getTargetStatusGroup(searchType: WfTokenConstants.SearchType): List<String>? {
            return when (searchType) {
                WfTokenConstants.SearchType.TODO -> listOf(Status.RUNNING.code)
                WfTokenConstants.SearchType.REQUESTED -> null
                WfTokenConstants.SearchType.PROGRESS -> listOf(Status.RUNNING.code, Status.WAITING.code)
                WfTokenConstants.SearchType.COMPLETED -> listOf(Status.FINISH.code)
            }
        }
    }
}
