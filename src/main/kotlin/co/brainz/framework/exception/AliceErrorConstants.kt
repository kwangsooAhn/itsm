package co.brainz.framework.exception

enum class AliceErrorConstants(val code: String, val detail: String) {
    ERR("ERR", "Default error"),
    ERR_00001("ERR-00001", "Wrong id or passwd"),
    ERR_00002("ERR-00002", "Session expired")
}
