package co.brainz.itsm.chart.dto

import co.brainz.framework.tag.dto.AliceTagDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import java.io.Serializable

data class ChartCategoryTagInstance(
    val category: String,
    val tag: AliceTagDto,
    val instances: List<WfInstanceEntity> = emptyList()
) : Serializable
