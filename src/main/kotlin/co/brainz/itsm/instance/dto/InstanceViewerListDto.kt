/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.dto

import java.io.Serializable

data class InstanceViewerListDto(
    val instanceId: String,
    val documentId: String,
    val viewers: List<ViewerDataDto>
) : Serializable
