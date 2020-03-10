package co.brainz.workflow.token.constants

object TokenConstants {

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
        USER("user"),
        GROUP("group")
    }

    /**
     * Mapping id expression.
     */
    const val mappingExpression: String = """[$][{][a-zA-Z0-9]+[}]"""
}
