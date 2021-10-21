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
        STATUS_ERROR_SCHEDULE_JAR_NOT_EXIST("4"),
        STATUS_ERROR_SCHEDULE_CLASS_NOT_EXIST("5")
    }

    /**
     * 스케줄러 TASK TYPE
     */
    enum class Types(val type: String) {
        JAR("jar"),
        CLASS("class")
    }

    /**
     * 스케줄러 파일 확장자
     */
    enum class Extension(val extension: String) {
        JAR("jar"),
        CLASS("kt")
    }

    /**
     * 스케줄러 상위 디렉토리
     */
    enum class Directory(val code: String) {
        ADDRESS("co.brainz.framework.scheduling.task.")
    }
}
