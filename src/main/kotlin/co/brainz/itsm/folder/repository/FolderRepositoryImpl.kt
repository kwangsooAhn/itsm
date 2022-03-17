/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.folder.constants.FolderConstants
import co.brainz.itsm.folder.entity.QWfFolderEntity
import co.brainz.itsm.folder.entity.WfFolderEntity
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FolderRepositoryImpl : QuerydslRepositorySupport(WfFolderEntity::class.java), FolderRepositoryCustom {

    override fun findRelatedDocumentListByFolderId(folderId: String): List<RestTemplateRelatedInstanceDto> {
        val folder = QWfFolderEntity.wfFolderEntity
        val user = QAliceUserEntity.aliceUserEntity
        val token = QWfTokenEntity.wfTokenEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        val startDtSubToken = QWfTokenEntity.wfTokenEntity

        return from(folder)
            .select(
                Projections.constructor(
                    RestTemplateRelatedInstanceDto::class.java,
                    folder.folderId,
                    folder.instance.instanceId,
                    folder.relatedType,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(token.tokenId)
                            .from(token)
                            .where(token.tokenStartDt.eq(
                                from(startDtSubToken)
                                    .select(startDtSubToken.tokenStartDt.max())
                                    .where(startDtSubToken.instance.instanceId.eq(folder.instance.instanceId))
                            )),
                        "tokenId"
                    ),
                    folder.instance.document.documentId,
                    folder.instance.documentNo,
                    folder.instance.document.documentName,
                    folder.instance.document.documentColor,
                    folder.createUserKey,
                    folder.createDt,
                    folder.instance.instanceStartDt,
                    folder.instance.instanceEndDt,
                    folder.instance.instanceStatus,
                    user.userKey,
                    user.userName,
                    user.avatarValue
                )
            )
            .innerJoin(folder.instance.document, document)
            .leftJoin(user).on(folder.instance.instanceCreateUser.userKey.eq(user.userKey))
            .where(
                folder.folderId.eq(folderId)
            )
            .where(folder.relatedType.eq("reference").or(folder.relatedType.eq("related")))
            .orderBy(folder.instance.instanceStartDt.asc())
            .fetch()
    }

    override fun findFolderOriginByInstanceId(instanceId: String): WfFolderEntity {
        val folder = QWfFolderEntity.wfFolderEntity
        return from(folder)
            .where(folder.instance.instanceId.eq(instanceId)
                .and(folder.relatedType.eq(FolderConstants.RelatedType.ORIGIN.code))
            )
            .fetchOne()
    }
}
