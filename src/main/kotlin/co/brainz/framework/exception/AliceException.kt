package co.brainz.framework.exception

class AliceException : Exception {
    private val err: AliceErrorConstants

    constructor(code: AliceErrorConstants, message: String) : super(message) {
        this.err = code
    }

    constructor(code: AliceErrorConstants, message: String, cause: Throwable) : super(message, cause) {
        this.err = code
    }

    fun getCode(): String {
        return this.err.code
    }
    fun getCodeDetail(): String {
        return this.err.detail
    }
}