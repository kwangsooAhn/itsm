package co.brainz.workflow.form.dto

import co.brainz.workflow.component.dto.ComponentDto
import java.io.Serializable

data class FormComponentDto(
        val form: FormViewDto,
        val components: MutableList<ComponentDto> = mutableListOf()
): Serializable
