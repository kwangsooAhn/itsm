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
     * Mapping id expression.
     */
    const val mappingExpression: String = """[$][{][a-zA-Z0-9]+[}]"""

    /**
     * 문서함 데이터 로딩 개수.
     */
    const val searchDataCount: Long = 7
}
