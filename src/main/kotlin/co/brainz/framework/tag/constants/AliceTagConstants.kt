/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.constants

object AliceTagConstants {
    enum class TagType(val code: String) {
        INSTANCE("instance"),
        COMPONENT("component"),
        CI("ci")
    }

    const val TAG_SUGGESTION_MAX_COUNT: Long = 20
}
