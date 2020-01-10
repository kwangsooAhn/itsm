package co.brainz.framework.constants

/**
 * 사용자 기본 코드.
 */
object UserConstants {

    interface UserEnum {

        /**
         * 사용자 상태.
         */
        enum class Status(val code: String, val value: Int) {
            CERTIFIED("user.status.certified", 1)
        }

        /**
         * 사용자 접근 플랫폼.
         */
        enum class Platform(val code: String, val value: String) {
            ALICE("user.platform.alice", "alice")
        }
    }
}
