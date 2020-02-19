package co.brainz.workflow.component.service

import co.brainz.workflow.component.dto.ComponentDto

interface Component {

    /**
     * Search Components.
     *
     * @param search
     * @return List<ComponentDto>
     */
    fun getComponents(search: String): String

    /**
     * Search Component.
     *
     * @param compId
     * @return ComponentDto
     */
    fun getComponent(compId: String): ComponentDto
}
