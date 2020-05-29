package co.brainz.workflow.engine.instance.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.workflow.engine.component.constants.WfComponentConstants
import co.brainz.workflow.engine.component.entity.QWfComponentEntity
import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.engine.instance.entity.QWfInstanceEntity
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.token.constants.WfTokenConstants
import co.brainz.workflow.engine.token.entity.QWfCandidateEntity
import co.brainz.workflow.engine.token.entity.QWfTokenDataEntity
import co.brainz.workflow.engine.token.entity.QWfTokenEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class WfInstanceRepositoryImpl : QuerydslRepositorySupport(WfInstanceEntity::class.java),
    WfInstanceRepositoryCustom {

    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val token: QWfTokenEntity = QWfTokenEntity.wfTokenEntity
    val document: QWfDocumentEntity = QWfDocumentEntity.wfDocumentEntity
    val searchDataCount: Long = WfTokenConstants.searchDataCount

    override fun findTodoInstances(
            status: String,
            userKey: String,
            documentId: String,
            searchValue: String,
            fromDt: String,
            toDt: String,
            dateFormat: String,
            offset: Long
    ): QueryResults<WfInstanceListViewDto> {

        val candidateSub = QWfCandidateEntity("candidateSub")
        val roleSub = QAliceUserRoleMapEntity("roleSub")

        val builder = getInstancesWhereCondition(documentId, searchValue, fromDt, toDt, dateFormat)
        builder.and(instance.instanceStatus.eq(status))
        builder.and(token.tokenStatus.eq(status))
        builder.and(token.assigneeId.eq(userKey).or(token.tokenId.`in`(
            JPAExpressions
                    .select(candidateSub.token.tokenId)
                    .from(candidateSub)
                    .where(
                        candidateSub.token.tokenId.eq(token.tokenId),
                        candidateSub.candidateValue.eq(userKey).or(
                            candidateSub.candidateValue.`in`(
                                JPAExpressions
                                        .select(roleSub.role.roleId)
                                        .from(roleSub)
                                        .where(roleSub.user.userKey.eq(userKey))
                            )
                        )
                    )
        )))

        val query = getInstancesQuery()
        return query
                .where(builder)
                .orderBy(instance.instanceStartDt.desc())
                .limit(searchDataCount)
                .offset(offset)
                .fetchResults()
    }

    override fun findRequestedInstances(
            userKey: String,
            documentId: String,
            searchValue: String,
            fromDt: String,
            toDt: String,
            dateFormat: String,
            offset: Long
    ): QueryResults<WfInstanceListViewDto> {

        val tokenSub = QWfTokenEntity("tokenSub")

        val builder = getInstancesWhereCondition(documentId, searchValue, fromDt, toDt, dateFormat)
        builder.and(instance.instanceCreateUser.userKey.eq(userKey))
        builder.and(token.tokenId.eq(
            JPAExpressions
                    .select(tokenSub.tokenId.max())
                    .from(tokenSub)
                    .where(tokenSub.instance.instanceId.eq(instance.instanceId)))
        )

        val query = getInstancesQuery()
        return query
                .where(builder)
                .orderBy(instance.instanceStartDt.desc())
                .limit(searchDataCount)
                .offset(offset)
                .fetchResults()
    }

    override fun findRelationInstances(
            status: String,
            userKey: String,
            documentId: String,
            searchValue: String,
            fromDt: String,
            toDt: String,
            dateFormat: String,
            offset: Long
    ): QueryResults<WfInstanceListViewDto> {

        val tokenSub = QWfTokenEntity("tokenSub")
        val builder = getInstancesWhereCondition(documentId, searchValue, fromDt, toDt, dateFormat)
        builder.and(instance.instanceStatus.eq(status))
        builder.and(
            token.tokenId.eq(
                JPAExpressions
                        .select(tokenSub.tokenId.max())
                        .from(tokenSub)
                        .where(tokenSub.instance.instanceId.eq(instance.instanceId)))
        )
        builder.and(
            instance.instanceId.`in`(
                JPAExpressions
                        .select(tokenSub.instance.instanceId)
                        .from(tokenSub)
                        .where(tokenSub.assigneeId.eq(userKey)))
        )


        val query = getInstancesQuery()
        return query
                .where(builder)
                .orderBy(instance.instanceStartDt.desc())
                .limit(searchDataCount)
                .offset(offset)
                .fetchResults()
    }

    /**
     * 문서함 목록 조회.
     */
    private fun getInstancesQuery(): JPQLQuery<WfInstanceListViewDto> {
        return from(token)
                .select(
                    Projections.constructor(
                        WfInstanceListViewDto::class.java,
                        token,
                        document,
                        instance
                    )
                )
                .innerJoin(instance).on(token.instance.eq(instance))
                .fetchJoin()
                .innerJoin(document).on(instance.document.eq(document))
                .fetchJoin()
    }

    /**
     * 문서함 목록 공통 검색 조건.
     */
    private fun getInstancesWhereCondition(
            documentId: String,
            searchValue: String,
            fromDt: String,
            toDt: String,
            dateFormat: String
    ): BooleanBuilder {
        val builder = BooleanBuilder()
        if (documentId.isNotEmpty()) {
            builder.and(document.documentId.eq(documentId))
        }
        if (searchValue.isNotEmpty()) {
            val tokenSub = QWfTokenEntity("tokenSub2")
            val tokenDataSub = QWfTokenDataEntity("tokenDataSub")
            val componentSub = QWfComponentEntity("componentSub")
            val componentTypeForTopicDisplay = WfComponentConstants.ComponentType.getComponentTypeForTopicDisplay()
            builder.and(
                instance.instanceCreateUser.userName.contains(searchValue)
                        .or(token.tokenId.`in`(
                            JPAExpressions
                                    .select(tokenDataSub.tokenId)
                                    .from(tokenDataSub)
                                    .where(
                                        tokenDataSub.tokenId.eq(
                                            JPAExpressions
                                                    .select(tokenSub.tokenId.max())
                                                    .from(tokenSub)
                                                    .where(tokenSub.instance.instanceId.eq(instance.instanceId))),
                                        tokenDataSub.componentId.`in`(
                                            JPAExpressions
                                                    .select(componentSub.componentId)
                                                    .from(componentSub)
                                                    .where(
                                                            componentSub.form.formId.eq(document.form.formId),
                                                            componentSub.isTopic.isTrue,
                                                            componentSub.componentType.`in`(componentTypeForTopicDisplay)
                                                    )
                                        ),
                                        tokenDataSub.value.contains(searchValue)
                                    )
                        )))
        }
        val from = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", fromDt, dateFormat)
        val to = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", toDt, dateFormat)
        builder.and(instance.instanceStartDt.between(from, to))
        return builder
    }

    override fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto> {
        val user = QAliceUserEntity.aliceUserEntity
        return from(token)
            .select(
                Projections.constructor(
                    RestTemplateInstanceHistoryDto::class.java,
                    token.tokenStartDt,
                    token.tokenEndDt,
                    token.element.elementName,
                    token.element.elementType,
                    token.assigneeId,
                    user.userName
                )
            )
            .innerJoin(token.instance)
            .innerJoin(token.element)
            .leftJoin(user).on(token.assigneeId.eq(user.userKey))
            .where(token.instance.instanceId.eq(instanceId))
            .orderBy(token.tokenStartDt.asc())
            .fetch()
    }

}
