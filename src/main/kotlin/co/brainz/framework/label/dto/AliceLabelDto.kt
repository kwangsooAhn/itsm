/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.label.dto

import java.io.Serializable

data class AliceLabelDto(
    var labelTarget: String = "",
    var labelTargetId: String = "",
    var labels: HashMap<String, String?>? = null
) : Serializable
