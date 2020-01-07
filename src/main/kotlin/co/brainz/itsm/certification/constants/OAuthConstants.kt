package co.brainz.itsm.certification.constants

object OAuthConstants {

    enum class PlatformEnum(val code: String, val value: String) {
        ALICE("user.platform.alice", "alice"),
        GOOGLE("user.platform.google", "google"),
        FACEBOOK("user.platform.facebook", "facebook"),
        KAKAO("user.platform.kakao", "kakao")
    }

}
