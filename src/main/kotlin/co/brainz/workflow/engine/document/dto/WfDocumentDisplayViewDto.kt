package co.brainz.workflow.engine.document.dto

import co.brainz.workflow.engine.process.dto.WfElementDto
import java.io.Serializable

data class WfDocumentDisplayViewDto(
        val document: WfDocumentDto,
        val elements: List<WfElementDto>,
        val components: MutableMap<String, Any>? = null    //List<WfDocumentDisplayDataDto>
) : Serializable
