package co.brainz.framework.numbering.constants

object AliceNumberingConstants {

    const val DEFAULT_START_WITH = 1
    const val DEFAULT_FUL_FILL = "Y"
    const val DEFAULT_DIGIT = 3
    const val DEFAULT_DATE_FORMAT = "yyyyMMdd"
    const val DEFAULT_DATE_FORMAT_PARENT_CODE = "numbering.pattern.format"

    /**
     * Numbering Pattern Type.
     *
     * @param code
     */
    enum class PatternType(val code: String) {
        TEXT("numbering.pattern.text"),
        DATE("numbering.pattern.date"),
        SEQUENCE("numbering.pattern.sequence")
    }

    /**
     * Numbering Pattern Value Id.
     *
     * @param value
     */
    enum class PatternValueId(val value: String) {
        TEXT_VALUE("value"),
        DATE_CODE("code"),
        SEQUENCE_DIGIT("digit"),
        SEQUENCE_START_WITH("start-with"),
        SEQUENCE_FULL_FILL("full-fill")
    }
}
