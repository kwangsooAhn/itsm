/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instanceViewer.dto

import java.io.Serializable

data class WfInstanceViewerReviewDto(
    val reviewYn : Boolean? = null
) : Serializable
