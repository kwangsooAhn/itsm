/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.process.dto

import co.brainz.workflow.element.entity.WfElementEntity

/**
 * 엘리먼트마다 시뮬레이션 결과를 담는 클래스
 */
class SimulationReport(
    val success: Boolean,
    element: WfElementEntity,
    val failedMessage: String
) {
    val elementId: String = element.elementId
    val elementType: String = element.elementType
    val elementName: String = element.elementName
}
