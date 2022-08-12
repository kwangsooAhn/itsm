package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceDocumentRoleMapEntity
import co.brainz.framework.auth.entity.QAliceDocumentRoleMapEntity
import co.brainz.framework.auth.entity.QAliceRoleEntity
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.document.entity.QWfDocumentLinkEntity
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceDocumentRoleMapRepositoryImpl : QuerydslRepositorySupport(AliceDocumentRoleMapEntity::class.java),
    AliceDocumentRoleMapRepositoryCustom {

    override fun findRoleByDocumentId(documentId: String, type: String?): MutableList<RoleListDto> {
        val role = QAliceRoleEntity.aliceRoleEntity
        val roleMap = QAliceDocumentRoleMapEntity.aliceDocumentRoleMapEntity
        val document = QWfDocumentEntity.wfDocumentEntity
        val documentLink = QWfDocumentLinkEntity.wfDocumentLinkEntity

        val query = from(role)
            .select(
                Projections.constructor(
                    RoleListDto::class.java,
                    role.roleId,
                    role.roleName,
                    role.roleDesc
                )
            )
        if (type == DocumentConstants.DocumentType.APPLICATION_FORM_LINK.value) {
            query.innerJoin(roleMap).on(
                role.eq(roleMap.role).and(
                    roleMap.documentId.eq(
                        JPAExpressions.select(documentLink.documentLinkId)
                            .from(documentLink)
                            .where(documentLink.documentLinkId.eq(documentId))
                    )
                )
            )
        } else {
            query.innerJoin(roleMap).on(
                role.eq(roleMap.role).and(
                    roleMap.documentId.eq(
                        JPAExpressions.select(document.documentId)
                            .from(document)
                            .where(document.documentId.eq(documentId))
                    )
                )
            )
        }
        query.orderBy(role.roleId.asc())

        return query.fetch()
    }

    override fun findDocumentIdsByRoles(roleIds: MutableList<String>): List<String> {
        val roleMap = QAliceDocumentRoleMapEntity.aliceDocumentRoleMapEntity

        return from(roleMap).distinct()
            .select(roleMap.documentId)
            .where(roleMap.role.roleId.`in`(roleIds))
            .fetch()
    }
}
