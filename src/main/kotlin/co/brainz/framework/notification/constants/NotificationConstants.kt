/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.constants

object NotificationConstants {

    /**
     * Notification ITSM Idenfitier
     */
    const val ITSM_IDENTIFIER: String = "zitsm"

    /**
     * Notification Type
     */
    enum class Type(val code: String) {
        DOCUMENT("document"),
        CMDB("cmdb")
    }
}
