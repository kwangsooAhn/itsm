package co.brainz.itsm.numberingPattern.constants

object NumberingPatternConstants {

    const val DEFAULT_DIGIT = 3
    const val DEFAULT_DATE_FORMAT = "yyyyMMdd"
    const val DEFAULT_DATE_FORMAT_PARENT_CODE: String = "numbering.pattern.format"

    /**
     * 패턴 편집 상태.
     */
    enum class Status(val code: String) {
        STATUS_SUCCESS("0"),
        STATUS_ERROR_PATTERN_USED("1")
    }

    /**
     * 패턴 타입
     */
    enum class PatternType(val code: String) {
        SUMMARZIE_TEXT("text"),
        SUMMARZIE_DATE("date"),
        SUMMARZIE_SEQUENCE("sequence"),
        TEXT("numbering.pattern.text"),
        DATE("numbering.pattern.date"),
        SEQUENCE("numbering.pattern.sequence")
    }

    /**
     * 패턴 데이터 Property
     */
    enum class ObjProperty(val property: String) {
        VALUE("value"),
        CODE("code"),
        DIGIT("digit"),
        STARTWITH("start-with"),
        FULLFILL("full-fill"),
        INITIALINTERVAL("initial-interval")
    }

    /**
     * 날짜 타입 관련 화면 출력 데이터 및 코드
     */
    enum class PatternDateValue(val code: String) {
        SUMMARIZE_YYYYMMDD("yyyyMMdd"),
        SUMMARIZE_DDMMYYYY("ddMMyyyy"),
        SUMMARIZE_MMDDYYYY("MMddyyyy"),
        SUMMARIZE_YYYYDDMM("yyyyddMM"),
        YYYYMMDD("numbering.pattern.format.yyyyMMdd"),
        DDMMYYYY("numbering.pattern.format.ddMMyyyy"),
        MMDDYYYY("numbering.pattern.format.MMddyyyy"),
        YYYYDDMM("numbering.pattern.format.yyyyddMM")
    }

    /**
     * 패턴 데이터 고정 값
     */
    enum class PatternFixedValue(val key: String) {
        STARTWITH_KEY("1"),
        FULLFILL_KEY("Y"),
        INITIALINTERVAL_KEY("none")
    }

    /**
     * 인터벌 타입
     */
    enum class INTERVAL(val value: String) {
        NONE("none"),
        DAY("day"),
        MONTH("month"),
        YEAR("year")
    }
}
