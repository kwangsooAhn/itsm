package co.brainz.itsm.statistic.customChart.dto

import java.io.Serializable
import org.w3c.dom.Node

data class ChartConditionNodeDto(
    val leftNode: Node?,
    val rightNode: Node?,
    val data: ChartConditionNodeDataDto
) : Serializable