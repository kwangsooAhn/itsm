package co.brainz.workflow.process

/**
 * 프로세스 관련 상수.
 */
object ProcessConstants {

    /**
     * 프로세스 상태.
     */
    enum class Status(val code: String) {
        EDIT("process.status.edit"),
        SIMULATION("process.status.simu"),
        PUBLISH("process.status.publish"),
        DESTROY("process.status.destroy")
    }
}
