package co.brainz.itsm.certification.constants

import co.brainz.framework.constants.UserConstants

object OAuthConstants {

    enum class Platform(val code: String, val value: String): UserConstants.UserEnum {
        GOOGLE("user.platform.google", "google"),
        FACEBOOK("user.platform.facebook", "facebook"),
        KAKAO("user.platform.kakao", "kakao")
    }

}
