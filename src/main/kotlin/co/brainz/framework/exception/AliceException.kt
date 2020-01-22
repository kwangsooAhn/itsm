package co.brainz.framework.exception

class AliceException : Exception {
    private val err: AliceErrorConstants

    constructor(error: AliceErrorConstants, message: String?) : super(message) {
        this.err = error
    }

    constructor(error: AliceErrorConstants, message: String?, cause: Throwable?) : super(message, cause) {
        this.err = error
    }

    fun getCode(): String {
        return this.err.code
    }
    fun getCodeDetail(): String {
        return this.err.detail
    }
}
