package co.brainz.workflow.provider.dto

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.token.entity.WfTokenEntity
import java.io.Serializable

data class RestTemplateInstanceViewDtoTest(
    val tokenEntity: WfTokenEntity,
    val documentEntity: WfDocumentEntity,
    val instanceEntity: WfInstanceEntity
) : Serializable
