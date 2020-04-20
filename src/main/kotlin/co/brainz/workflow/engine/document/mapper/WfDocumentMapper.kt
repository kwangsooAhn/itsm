package co.brainz.workflow.engine.document.mapper

import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import org.mapstruct.Mapper

@Mapper
interface WfDocumentMapper {
    fun toDocumentEntity(documentDto: WfDocumentDto): WfDocumentEntity
}
