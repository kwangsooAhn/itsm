package co.brainz.framework.exception

/**
 * 에러 코드 정의 클래스.
 *
 */
enum class AliceErrorConstants(
    val httpStatus: Int = AliceHttpStatusConstants.INTERNAL_SERVER_ERROR.status,
    val code: String = "ERR-DEFAULT",
    val message: String = AliceHttpStatusConstants.INTERNAL_SERVER_ERROR.reasonPhrase
) {
    ERR(AliceHttpStatusConstants.INTERNAL_SERVER_ERROR.status, "ERR-DEFAULT"),
    ERR_00001(AliceHttpStatusConstants.BAD_REQUEST.status, "ERR-00001"),
    ERR_00002(AliceHttpStatusConstants.FORBIDDEN.status, "ERR-00002"),
    ERR_00003(AliceHttpStatusConstants.UNAUTHORIZED.status, "ERR-00003"),
    ERR_00004(AliceHttpStatusConstants.CONFLICT.status, "ERR-00004"),
    ERR_00005(AliceHttpStatusConstants.NOT_FOUND.status, "ERR-00005")
    ;
}
