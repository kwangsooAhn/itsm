package co.brainz.workflow.token.repository

import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.instance.dto.WfInstanceListComponentDto
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto
import co.brainz.workflow.token.entity.QWfTokenDataEntity
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
            .where(tokenData.token.tokenId.`in`(tokenIds))
            .fetch()
    }
}
