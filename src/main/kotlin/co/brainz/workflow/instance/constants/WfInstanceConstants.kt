package co.brainz.workflow.instance.constants

class WfInstanceConstants {

    /**
     * Instance Status.
     */
    enum class Status(val code: String) {
        RUNNING("running"),
        WAITING("waiting"),
        FINISH("finish")
    }

    enum class SearchType(val code: String) {
        TODO("token.type.todo"),
        REQUESTED("token.type.requested"),
        PROGRESS("token.type.progress"),
        COMPLETED("token.type.completed")
    }

    companion object {
        fun getTargetStatusGroup(searchType: SearchType): List<String>? {
            return when (searchType) {
                SearchType.TODO -> listOf(Status.RUNNING.code)
                SearchType.REQUESTED -> null
                SearchType.PROGRESS -> listOf(Status.RUNNING.code, Status.WAITING.code)
                SearchType.COMPLETED -> listOf(Status.FINISH.code)
                else -> null
            }
        }
    }
}
