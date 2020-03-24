package co.brainz.framework.exception

/**
 * 에러 코드 정의.
 *
 */
enum class AliceErrorConstants(
    val httpStatus: Int = AliceHttpStatusConstants.INTERNAL_SERVER_ERROR.status,
    val code: String = "DEFAULT",
    val detailMessage: String
) {
    ERR(AliceHttpStatusConstants.NOT_ACCEPTABLE.status, "ERR-DEFAULT", "Default error"),
    ERR_00001(AliceHttpStatusConstants.BAD_REQUEST.status, "ERR-00001", "Wrong id or passwd"),
    ERR_00002(AliceHttpStatusConstants.NOT_FOUND.status, "ERR-00002", "Session expired"),
    ERR_00003(AliceHttpStatusConstants.UNAUTHORIZED.status, "ERR-00003", "Unauthorized page."),
    ERR_00004(AliceHttpStatusConstants.BAD_REQUEST.status, "ERR-00004", "The file extension is not allowed")
    ;

}
