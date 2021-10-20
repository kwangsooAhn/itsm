package co.brainz.itsm.scheduler.constants

object SchedulerConstants {

    /**
     * 스케줄러 처리 상태.
     */
    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_ERROR_SCHEDULE_USED("1"),
        STATUS_ERROR_SCHEDULE_NAME_DUPLICATION("2"),
        STATUS_ERROR_SCHEDULER_EXECUTE("3"),
        STATUS_ERROR_SCHEDULE_JAR_NOT_EXIST("4")
    }

    /**
     * 스케줄러 TASK TYPE
     */
    enum class Types(val type: String) {
        JAR("jar")
    }
}