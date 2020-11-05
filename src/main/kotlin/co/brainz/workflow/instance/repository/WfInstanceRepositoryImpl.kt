package co.brainz.workflow.instance.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.itsm.instance.constants.InstanceConstants
import co.brainz.workflow.comment.entity.QWfCommentEntity
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.QWfElementDataEntity
import co.brainz.workflow.folder.entity.QWfFolderEntity
import co.brainz.workflow.instance.dto.WfInstanceListDocumentDto
import co.brainz.workflow.instance.dto.WfInstanceListInstanceDto
import co.brainz.workflow.instance.dto.WfInstanceListTokenDto
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.tag.entity.QWfTagEntity
import co.brainz.workflow.tag.entity.QWfTagMapEntity
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.QWfTokenDataEntity
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfInstanceRepositoryImpl : QuerydslRepositorySupport(WfInstanceEntity::class.java),
    WfInstanceRepositoryCustom {

    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val token: QWfTokenEntity = QWfTokenEntity.wfTokenEntity
    val tokenData: QWfTokenDataEntity = QWfTokenDataEntity.wfTokenDataEntity
    val comment: QWfCommentEntity = QWfCommentEntity.wfCommentEntity
    val tagMap: QWfTagMapEntity = QWfTagMapEntity.wfTagMapEntity
    val tag: QWfTagEntity = QWfTagEntity.wfTagEntity
    val folder: QWfFolderEntity = QWfFolderEntity.wfFolderEntity
    val document: QWfDocumentEntity = QWfDocumentEntity.wfDocumentEntity
    val searchDataCount: Long = WfTokenConstants.searchDataCount

    override fun findTodoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        tags: Set<String>,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto> {

        val elementDataSub = QWfElementDataEntity("elementDataSub")
        val roleSub = QAliceUserRoleMapEntity("roleSub")
        val builder = getInstancesWhereCondition(documentId, searchValue, fromDt, toDt)

        val assigneeUsers = JPAExpressions
            .select(elementDataSub.element.elementId)
            .from(elementDataSub)
            .where(
                elementDataSub.element.`in`(
                    JPAExpressions
                        .select(elementDataSub.element)
                        .from(elementDataSub)
                        .where(
                            token.element.eq(elementDataSub.element),
                            elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE_TYPE.value),
                            elementDataSub.attributeValue.eq(WfTokenConstants.AssigneeType.USERS.code)
                        )
                ),
                elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE.value),
                elementDataSub.attributeValue.eq(userKey)
            )

        val assigneeGroups = JPAExpressions
            .select(elementDataSub.element.elementId)
            .from(elementDataSub)
            .where(
                elementDataSub.element.`in`(
                    JPAExpressions
                        .select(elementDataSub.element)
                        .from(elementDataSub)
                        .where(
                            token.element.eq(elementDataSub.element),
                            elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE_TYPE.value),
                            elementDataSub.attributeValue.eq(WfTokenConstants.AssigneeType.GROUPS.code)
                        )
                ),
                elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE.value),
                elementDataSub.attributeValue.`in`(
                    JPAExpressions
                        .select(roleSub.role.roleId)
                        .from(roleSub)
                        .where(roleSub.user.userKey.eq(userKey))
                )
            )

        builder.and(instance.instanceStatus.`in`(status))
        builder.and(token.tokenStatus.`in`(tokenStatus))
        builder.and(token.element.elementType.`in`(WfElementConstants.ElementType.USER_TASK.value))
        builder.and(
            token.assigneeId.eq(userKey)
                .or(
                    token.element.elementId.`in`(assigneeUsers)
                ).or(
                    token.element.elementId.`in`(assigneeGroups)
                )
        )

        val query = getInstancesQuery(tags)
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
        tags: Set<String>,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto> {

        val tokenSub = QWfTokenEntity("tokenSub")
        val builder = getInstancesWhereCondition(documentId, searchValue, fromDt, toDt)
        builder.and(instance.instanceCreateUser.userKey.eq(userKey))
        builder.and(
            token.tokenId.eq(
                JPAExpressions
                    .select(tokenSub.tokenId.max())
                    .from(tokenSub)
                    .where(tokenSub.instance.instanceId.eq(instance.instanceId))
            )
        )

        val query = getInstancesQuery(tags)
        return query
            .where(builder)
            .orderBy(instance.instanceStartDt.desc())
            .limit(searchDataCount)
            .offset(offset)
            .fetchResults()
    }

    override fun findRelationInstances(
        status: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        tags: Set<String>,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto> {

        val tokenSub = QWfTokenEntity("tokenSub")
        val builder = getInstancesWhereCondition(documentId, searchValue, fromDt, toDt)
        builder.and(instance.instanceStatus.`in`(status))
        builder.and(
            token.tokenId.eq(
                JPAExpressions
                    .select(tokenSub.tokenId.max())
                    .from(tokenSub)
                    .where(tokenSub.instance.instanceId.eq(instance.instanceId))
            )
        )
        builder.and(
            instance.instanceId.`in`(
                JPAExpressions
                    .select(tokenSub.instance.instanceId)
                    .from(tokenSub)
                    .where(tokenSub.assigneeId.eq(userKey))
            )
        )

        val query = getInstancesQuery(tags)
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
    private fun getInstancesQuery(tags: Set<String>): JPQLQuery<WfInstanceListViewDto> {
        val query = from(token)
            .select(
                Projections.constructor(
                    WfInstanceListViewDto::class.java,
                    Projections.constructor(
                        WfInstanceListTokenDto::class.java,
                        token.tokenId,
                        token.element,
                        token.assigneeId,
                        token.tokenStartDt,
                        token.tokenEndDt
                    ),
                    Projections.constructor(
                        WfInstanceListDocumentDto::class.java,
                        document.documentId,
                        document.documentType,
                        document.documentName,
                        document.documentStatus,
                        document.documentDesc,
                        document.documentColor,
                        document.process,
                        document.form,
                        document.numberingRule,
                        document.documentIcon
                    ),
                    Projections.constructor(
                        WfInstanceListInstanceDto::class.java,
                        instance.instanceId,
                        instance.instanceStatus,
                        instance.instanceStartDt,
                        instance.instanceEndDt,
                        instance.instanceCreateUser,
                        instance.pTokenId,
                        instance.document,
                        instance.documentNo
                    )
                )
            )
            .innerJoin(instance).on(token.instance.eq(instance))
            .fetchJoin()
            .innerJoin(document).on(instance.document.eq(document))
            .fetchJoin()
        if (tags.isNotEmpty()) {
            query.where(
                instance.instanceId.`in`(
                    JPAExpressions
                        .select(tagMap.instance.instanceId)
                        .from(tagMap)
                        .join(tag).on(
                            tagMap.tagId.eq(tag.tagId).and(tag.tagContent.`in`(tags))
                        )
                )
            )
                .fetchJoin()
        }
        return query
    }

    /**
     * 문서함 목록 공통 검색 조건.
     */
    private fun getInstancesWhereCondition(
        documentId: String,
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime
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
                    .or(
                        token.tokenId.`in`(
                            JPAExpressions
                                .select(tokenDataSub.token.tokenId)
                                .from(tokenDataSub)
                                .where(
                                    tokenDataSub.token.tokenId.eq(
                                        JPAExpressions
                                            .select(tokenSub.tokenId.max())
                                            .from(tokenSub)
                                            .where(tokenSub.instance.instanceId.eq(instance.instanceId))
                                    ),
                                    tokenDataSub.component.componentId.`in`(
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
                        )
                    )
            )
        }
        builder.and(instance.instanceStartDt.goe(fromDt))
        builder.and(instance.instanceStartDt.lt(toDt))
        return builder
    }

    override fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto> {
        val user = QAliceUserEntity.aliceUserEntity
        val elementTypes = listOf(
            InstanceConstants.ElementListForHistoryViewing.USER_TASK.value,
            InstanceConstants.ElementListForHistoryViewing.SCRIPT_TASK.value,
            InstanceConstants.ElementListForHistoryViewing.MANUAL_TASK.value,
            InstanceConstants.ElementListForHistoryViewing.COMMON_END_EVENT.value,
            InstanceConstants.ElementListForHistoryViewing.TIMER_START_EVENT.value,
            InstanceConstants.ElementListForHistoryViewing.SUB_PROCESS.value,
            InstanceConstants.ElementListForHistoryViewing.SIGNAL_SEND.value
        )
        return from(token)
            .select(
                Projections.constructor(
                    RestTemplateInstanceHistoryDto::class.java,
                    token.tokenStartDt,
                    token.tokenEndDt,
                    token.element.elementName,
                    token.element.elementType,
                    token.tokenStatus,
                    token.assigneeId,
                    user.userName
                )
            )
            .innerJoin(token.instance)
            .innerJoin(token.element)
            .leftJoin(user).on(token.assigneeId.eq(user.userKey))
            .where(token.instance.instanceId.eq(instanceId).and(token.element.elementType.`in`(elementTypes)))
            .orderBy(token.tokenStartDt.asc())
            .fetch()
    }

    override fun deleteInstances(instances: MutableList<WfInstanceEntity>) {
        val tokens = from(token).where(token.instance.`in`(instances)).fetch()
        val instanceIds = mutableListOf<String>()
        instances.forEach { instanceIds.add(it.instanceId) }

        // Delete instance relation data.
        delete(tagMap).where(tagMap.instance.`in`(instances)).execute()
        delete(tokenData).where(tokenData.token.`in`(tokens)).execute()
        delete(token).where(token.instance.`in`(instances)).execute()
        delete(folder).where(folder.instance.`in`(instances)).execute()
        delete(comment).where(comment.instance.`in`(instances)).execute()
        delete(instance).where(instance.instanceId.`in`(instanceIds)).execute()
    }
}
