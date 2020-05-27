package co.brainz.workflow.process.constants

/**
 * 프로세스 관련 상수.
 */
object WfProcessConstants {

    /**
     * 프로세스 상태.
     */
    enum class Status(val code: String) {
        EDIT("process.status.edit"),
        PUBLISH("process.status.publish"),
        USE("process.status.use"),
        DESTROY("process.status.destroy")
    }

    /**
     * 프로세스 저장 타입.
     */
    enum class SaveType(val code: String) {
        SAVE_AS("saveas")
    }
}
