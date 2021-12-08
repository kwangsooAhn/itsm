package co.brainz.workflow.token.repository

import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.form.entity.QWfFormEntity
import co.brainz.workflow.instance.dto.WfInstanceListComponentDto
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.token.entity.QWfTokenDataEntity
import co.brainz.workflow.token.entity.QWfTokenEntity
import co.brainz.workflow.token.entity.WfTokenDataEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfTokenDataRepositoryImpl : QuerydslRepositorySupport(WfTokenDataEntity::class.java),
    WfTokenDataRepositoryCustom {
    override fun findTokenDataByTokenIds(tokenIds: Set<String>): List<WfInstanceListTokenDataDto> {
        val tokenData = QWfTokenDataEntity.wfTokenDataEntity
        val component = QWfComponentEntity.wfComponentEntity
        val instance = QWfInstanceEntity.wfInstanceEntity
        val token = QWfTokenEntity.wfTokenEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        val form = QWfFormEntity.wfFormEntity

        return from(tokenData)
            .select(
                Projections.constructor(
                    WfInstanceListTokenDataDto::class.java,
                    Projections.constructor(
                        WfInstanceListComponentDto::class.java,
                        tokenData.component.componentId,
                        tokenData.component.componentType,
                        tokenData.component.mappingId,
                        tokenData.component.isTopic,
                        tokenData.token.tokenId
                    ),
                    tokenData.value
                )
            )
            .innerJoin(tokenData.component, component)
            .innerJoin(tokenData.token, token)
            .innerJoin(token.instance, instance)
            .innerJoin(instance.document, document)
            .innerJoin(document.form, form).on(form.formId.eq(component.form.formId))
            .where(tokenData.token.tokenId.`in`(tokenIds))
            .fetch()
    }
}
