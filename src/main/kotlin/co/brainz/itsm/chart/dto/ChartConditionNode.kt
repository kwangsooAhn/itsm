package co.brainz.itsm.chart.dto

import java.io.Serializable

data class ChartConditionNode(
    var data: ChartConditionNodeDataDto = ChartConditionNodeDataDto(),
    var leftNode: ChartConditionNode? = null,
    var rightNode: ChartConditionNode? = null
) : Serializable
