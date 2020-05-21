package co.brainz.workflow.engine.token.constants

object WfTokenConstants {

    /**
     * Token Status.
     */
    enum class Status(val code: String) {
        RUNNING("running"),
        WAITING("waiting"),
        FINISH("finish")
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

    const val SESSION_USER_KEY = "40288ab26fa3219e016fa32231230000"
}
