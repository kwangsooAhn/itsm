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
        fun getTargetStatusGroup(searchType: Status): Array<String>? {
            return when (searchType) {
                SearchType.TODO -> arrayOf(Status.RUNNING.code)
                SearchType.REQUESTED -> null
                SearchType.PROGRESS -> arrayOf(Status.RUNNING.code, Status.WAITING.code)
                SearchType.COMPLETED -> arrayOf(Status.FINISH.code)
                else -> null
            }
        }
    }
}
