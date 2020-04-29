package co.brainz.workflow.engine.process.service.simulation.element

import co.brainz.workflow.engine.element.entity.WfElementEntity

abstract class WfProcessSimulationElement {
    fun getValidation(element: WfElementEntity) {
        validate(element)
    }

    abstract fun validate(element: WfElementEntity): Boolean
}
