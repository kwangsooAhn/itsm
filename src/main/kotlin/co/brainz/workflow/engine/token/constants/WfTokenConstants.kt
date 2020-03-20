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
    enum class AssigneeType(val code:String) {
        ASSIGNEE("assignee"),
        USERS("users"),
        GROUPS("groups")
    };

    /**
     * Mapping id expression.
     */
    const val mappingExpression: String = """[$][{][a-zA-Z0-9]+[}]"""
}
