package co.brainz.itsm.certification.constants

import co.brainz.framework.constants.AliceConstants

object OAuthConstants {

    enum class Platform(val code: String, val value: String): AliceConstants.UserEnum {
        GOOGLE("user.platform.google", "google"),
        FACEBOOK("user.platform.facebook", "facebook"),
        KAKAO("user.platform.kakao", "kakao")
    }

}
