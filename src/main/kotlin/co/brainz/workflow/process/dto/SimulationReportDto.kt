/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.process.dto

import java.io.Serializable

/**
 * 프로세스 디자이너에서 시뮬레이션 수행시 리턴하는 dto
 */
data class SimulationReportDto(
    var success: Boolean = true,
    val simulationReport: MutableList<SimulationReport> = mutableListOf()
) : Serializable {

    fun addSimulationReport(simulationReport: SimulationReport) {
        if (!this.simulationReport.any { it.elementId === simulationReport.elementId }) {
            this.simulationReport.add(simulationReport)
        }
        if (this.success) {
            this.success = simulationReport.success
        }
    }
}
