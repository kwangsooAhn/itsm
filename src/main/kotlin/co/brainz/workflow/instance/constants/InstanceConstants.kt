package co.brainz.workflow.instance.constants

class InstanceConstants {

    enum class Status(val code: String) {
        RUNNING("running"),
        WAITING("waiting"),
        FINISH("finish")
    }
}
