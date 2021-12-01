/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.chart.dto

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable

data class ChartTagInstanceDto(
    val tag: AliceTagDto,
    val instances: List<WfInstanceEntity> = emptyList()
) : Serializable
