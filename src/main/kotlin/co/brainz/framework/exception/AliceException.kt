package co.brainz.framework.exception

/**
 * 에러 처리 클래스
 *
 * @property err 정의된 에러 코드
 * @property errorInfo 추가적으로 전달할 정보
 *
 * @since 2020-03-26 kbh
 */
class AliceException : RuntimeException {
    private val err: AliceErrorConstants
    private var errorInfo: Any

    constructor(error: AliceErrorConstants, message: String?) : super(message) {
        this.err = error
        this.errorInfo = ""
    }

    constructor(error: AliceErrorConstants, message: String?, errorInfo: Any) : super(message) {
        this.err = error
        this.errorInfo = errorInfo
    }

    fun getHttpStatusCode(): Int {
        return this.err.httpStatus
    }

    fun getCode(): String {
        return this.err.code
    }

    fun getCodeDetail(): String {
        return this.err.message
    }

    fun getErrorInfo(): Any {
        return this.errorInfo
    }
}
