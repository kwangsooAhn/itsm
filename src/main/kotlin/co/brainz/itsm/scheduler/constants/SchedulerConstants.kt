package co.brainz.itsm.scheduler.constants

object SchedulerConstants {

    /**
     * 스케줄러 처리 상태.
     */
    enum class Status(val code: String) {
        STATUS_SUCCESS("Z-0000"),
        STATUS_ERROR_SCHEDULE_NAME_DUPLICATION("E-0001"),
        STATUS_ERROR_SCHEDULER_EXECUTE("E-0004"),
        STATUS_ERROR_SCHEDULE_JAR_NOT_EXIST("E-0003"),
        STATUS_ERROR_SCHEDULE_CLASS_NOT_EXIST("E-0007") // 스케쥴러에서만 사용하는 코드
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
