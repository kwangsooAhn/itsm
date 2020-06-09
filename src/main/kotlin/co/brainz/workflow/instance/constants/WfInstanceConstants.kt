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
}
