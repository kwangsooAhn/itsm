package co.brainz.itsm.board.constants

object BoardConstants {

    const val REPLY = "reply"

    enum class Status(val code: String) {
        STATUS_FAIL("-1"),
        STATUS_SUCCESS("0")
    }
}
