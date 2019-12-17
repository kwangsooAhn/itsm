package co.brainz.framework.exception

class AliceException : Exception {
    private val code: String

    constructor(code: String, message: String) : super(message) {
        this.code = code
    }

    constructor(code: String, message: String, cause: Throwable) : super(message, cause) {
        this.code = code
    }
}