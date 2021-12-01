package co.brainz.itsm.chart.dto.average

import co.brainz.workflow.engine.manager.dto.WfTokenDataDto
import java.io.Serializable

data class ChartCategoryTokenDataDto(
    val category: String,
    val tokenData: WfTokenDataDto
) : Serializable
