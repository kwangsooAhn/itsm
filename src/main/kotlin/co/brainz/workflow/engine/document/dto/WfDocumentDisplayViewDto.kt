package co.brainz.workflow.engine.document.dto

import co.brainz.workflow.engine.component.entity.WfComponentDataEntity
import java.io.Serializable

data class WfDocumentDisplayViewDto(
        val documentId: String,
        val components:  List<WfComponentDataEntity>,
        val elements:  List<Map<String, Any>>,
        val displays: MutableList<WfDocumentDisplayDto> = mutableListOf()
) : Serializable
