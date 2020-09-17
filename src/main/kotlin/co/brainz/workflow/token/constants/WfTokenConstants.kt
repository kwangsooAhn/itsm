package co.brainz.workflow.token.constants

object WfTokenConstants {

    /**
     * Token Status.
     */
    enum class Status(val code: String) {
        RUNNING("token.status.running"),
        WAITING("token.status.waiting"),
        FINISH("token.status.finish"),
        WITHDRAW("token.status.withdraw"),
        REJECT("token.status.reject"),
        CANCEL("token.status.cancel"),
        TERMINATE("token.status.terminate")
    }

    /**
     * Assignee Type.
     */
    enum class AssigneeType(val code: String) {
        ASSIGNEE("assignee.type.assignee"),
        USERS("assignee.type.candidate.users"),
        GROUPS("assignee.type.candidate.groups")
    }

    /**
     * 토큰의 조회 종류, 문서함에서 사용 한다
     */
    enum class SearchType(val code: String) {
        TODO("token.type.todo"),
        REQUESTED("token.type.requested"),
        PROGRESS("token.type.progress"),
        COMPLETED("token.type.completed")
    }

    /**
     * Mapping id expression.
     */
    const val mappingExpression: String = """[$][{][a-zA-Z0-9]+[}]"""

    /**
     * 문서함 데이터 로딩 개수.
     */
    const val searchDataCount: Long = 15

    /**
     * 토큰의 조회 종류에 따라 문서함에서 조회할 토큰의 상태값을 리턴
     */
    fun getTargetTokenStatusGroup(searchType: SearchType): List<String>? {
        return when (searchType) {
            SearchType.TODO -> listOf(Status.RUNNING.code)
            SearchType.REQUESTED -> null
            SearchType.PROGRESS -> listOf(
                Status.RUNNING.code,
                Status.WAITING.code
            )
            SearchType.COMPLETED -> listOf(Status.FINISH.code)
        }
    }
}
