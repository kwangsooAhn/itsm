package co.brainz.workflow.engine.process.service.simulation.element

import co.brainz.workflow.engine.element.entity.WfElementEntity
import co.brainz.workflow.engine.element.repository.WfElementRepository

/**
 * 시뮬레이션 엘리먼트 추상 클래스
 */
abstract class WfProcessSimulationElement(protected val repository: WfElementRepository) {

    /**
     * 공통 메서드
     */
    fun getValidation(element: WfElementEntity): Boolean {
        return validate(element)
    }

    /**
     * 엘리먼트 검증하기
     */
    abstract fun validate(element: WfElementEntity): Boolean

    /**
     * 엘리먼트 검증 실패시 관련 정보 리턴하기
     */
    abstract fun failInfo(): String
}
