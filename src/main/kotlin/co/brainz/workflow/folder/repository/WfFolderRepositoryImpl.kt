package co.brainz.workflow.folder.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.workflow.folder.entity.QWfFolderEntity
import co.brainz.workflow.folder.entity.WfFolderEntity
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfFolderRepositoryImpl : QuerydslRepositorySupport(WfFolderEntity::class.java),
    WfFolderRepositoryCustom {

    override fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateFolderDto> {
        val folder = QWfFolderEntity.wfFolderEntity
        val user = QAliceUserEntity.aliceUserEntity
        val token = QWfTokenEntity.wfTokenEntity
        val queryTokenId = JPAExpressions.select(folder.folderId)
            .from(folder, token)
            .where(
                folder.instance.eq(token.instance).and(token.tokenId.eq(tokenId)).and(folder.relatedType.eq("origin"))
            )
        return from(folder)
            .select(
                Projections.constructor(
                    RestTemplateFolderDto::class.java,
                    folder.folderId,
                    folder.instance.instanceId,
                    folder.relatedType,
                    ExpressionUtils.`as`(folder.instance.documentNo,"tokenId"),
                    folder.instance.documentNo,
                    folder.instance.document.documentName,
                    folder.createUserKey,
                    folder.createDt,
                    folder.instance.instanceStartDt,
                    folder.instance.instanceEndDt,
                    user.userKey,
                    user.userName
                )
            )
            .leftJoin(user).on(folder.instance.instanceCreateUser.userKey.eq(user.userKey))
            .leftJoin(token).on(folder.instance.instanceId.eq(token.instance.instanceId))
            .where(
                folder.folderId.eq(queryTokenId)
            )
            .where(folder.relatedType.eq("reference").or(folder.relatedType.eq("related")))
            .orderBy(folder.instance.instanceStartDt.asc())
            .fetch()
    }
}
