package co.brainz.workflow.token.constants

object TokenConstants {

    enum class Status(val code: String) {
        RUNNING("running"),
        WAITING("waiting"),
        FINISH("finish")
    }
}
