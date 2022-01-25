/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable
import java.time.LocalDateTime

data class ViewerListDto(
    var instanceId: String,
    var viewerKey: String,
    var viewerName: String = "",
    var organizationName: String? = "",
    var avatarPath: String? = "",
    var reviewYn: Boolean = false,
    var displayYn: Boolean = false,
    var createUserKey: String?,
    var createDt: LocalDateTime?,
    var updateUserKey: String?,
    var updateDt: LocalDateTime?
):Serializable

