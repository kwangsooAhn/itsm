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
import co.brainz.workflow.instance.constants.InstanceStatus
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FolderRepositoryImpl : QuerydslRepositorySupport(WfFolderEntity::class.java), FolderRepositoryCustom {
    val folder: QWfFolderEntity = QWfFolderEntity.wfFolderEntity
    val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity
    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val token: QWfTokenEntity = QWfTokenEntity.wfTokenEntity
    val document: QWfDocumentEntity = QWfDocumentEntity.wfDocumentEntity
    val startDtSubToken: QWfTokenEntity = QWfTokenEntity.wfTokenEntity


    override fun findRelatedDocumentListByFolderId(folderId: String): List<RestTemplateRelatedInstanceDto> {

        return from(folder)
            .select(
                Projections.constructor(
                    RestTemplateRelatedInstanceDto::class.java,
                    folder.folderId,
                    folder.instance.instanceId,
                    folder.relatedType,
                    // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
                    ExpressionUtils.`as`(
                        JPAExpressions.select(token.tokenId.max())
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
            .where(
                folder.instance.instanceId.notIn(
                    JPAExpressions.select(instance.instanceId)
                        .from(instance)
                        .innerJoin(token).on(instance.instanceId.eq(token.instance.instanceId))
                        .where(
                            token.tokenAction.eq(WfTokenConstants.FinishAction.CANCEL.code)
                                .and(instance.instanceStatus.eq(InstanceStatus.FINISH.code))
                        )
                )
            )
            .orderBy(folder.instance.instanceStartDt.asc())
            .fetch()
    }

    override fun findFolderOriginByInstanceId(instanceId: String): WfFolderEntity {
        return from(folder)
            .where(folder.instance.instanceId.eq(instanceId)
                .and(folder.relatedType.eq(FolderConstants.RelatedType.ORIGIN.code))
            )
            .fetchOne()
    }
}
