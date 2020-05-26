package co.brainz.workflow.engine.folder.repository

import co.brainz.workflow.engine.folder.entity.QWfFolderEntity
import co.brainz.workflow.engine.folder.entity.WfFolderEntity
import co.brainz.workflow.engine.token.entity.QWfTokenEntity
import co.brainz.workflow.provider.dto.RestTemplateFolderDto
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfFolderRepositoryImpl : QuerydslRepositorySupport(WfFolderEntity::class.java), WfFolderRepositoryCustom {

    override fun findRelatedDocumentListByTokenId(tokenId: String): List<RestTemplateFolderDto> {
        val folder = QWfFolderEntity.wfFolderEntity
        //val user = QAliceUserEntity.aliceUserEntity
        val token = QWfTokenEntity.wfTokenEntity
        return from(folder)
            .select(
                Projections.constructor(
                    RestTemplateFolderDto::class.java,
                    folder.folderId,
                    folder.instance.instanceId,
                    folder.relatedType,
                    folder.instance.document.documentName,
                    folder.createUserKey,
                    folder.createDt,
                    folder.instance.instanceStartDt,
                    folder.instance.instanceEndDt,
                    folder.instance.instanceCreateUser.userKey,
                    folder.instance.instanceCreateUser.userName
                )
            )
            .innerJoin(folder.instance)
            .where(
                folder.folderId.eq(
                    JPAExpressions.select(folder.folderId)
                        .from(folder, token)
                        .where(folder.instance.eq(token.instance).and(token.tokenId.eq(tokenId)))
                )
            )
            .orderBy(folder.instance.instanceStartDt.asc())
            //instanceCreateUser 값이 없으면 안나온다....
            //.innerJoin(token).on(token.tokenId.eq(tokenId))
            //.innerJoin(folder.instance.)
            //.leftJoin(user).on(folder.instance.instanceCreateUser.userKey.eq (user.userKey))
            .fetch()
    }

}
