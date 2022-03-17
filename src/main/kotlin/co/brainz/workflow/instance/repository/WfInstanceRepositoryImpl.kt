/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.instance.repository

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.framework.querydsl.QuerydslConstants
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.entity.QAliceTagEntity
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ci.entity.QCIComponentDataEntity
import co.brainz.itsm.code.entity.QCodeEntity
import co.brainz.itsm.folder.constants.FolderConstants
import co.brainz.itsm.folder.entity.QWfFolderEntity
import co.brainz.itsm.instance.constants.InstanceConstants
import co.brainz.itsm.instance.entity.QWfCommentEntity
import co.brainz.itsm.instance.entity.QWfInstanceViewerEntity
import co.brainz.itsm.statistic.customChart.constants.ChartConstants
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.QWfComponentEntity
import co.brainz.workflow.document.constants.WfDocumentConstants
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.QWfElementDataEntity
import co.brainz.workflow.element.entity.QWfElementEntity
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.instance.constants.WfInstanceConstants
import co.brainz.workflow.instance.dto.WfInstanceListDocumentDto
import co.brainz.workflow.instance.dto.WfInstanceListInstanceDto
import co.brainz.workflow.instance.dto.WfInstanceListTokenDto
import co.brainz.workflow.instance.dto.WfInstanceListUserDto
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.QWfTokenDataEntity
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.CaseBuilder
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class WfInstanceRepositoryImpl(
    private val currentSessionUser: CurrentSessionUser
) : QuerydslRepositorySupport(WfInstanceEntity::class.java),
    WfInstanceRepositoryCustom {

    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val token: QWfTokenEntity = QWfTokenEntity.wfTokenEntity
    val user: QAliceUserEntity = QAliceUserEntity.aliceUserEntity
    val tokenData: QWfTokenDataEntity = QWfTokenDataEntity.wfTokenDataEntity
    val comment: QWfCommentEntity = QWfCommentEntity.wfCommentEntity
    val tag: QAliceTagEntity = QAliceTagEntity.aliceTagEntity
    val folder: QWfFolderEntity = QWfFolderEntity.wfFolderEntity
    val document: QWfDocumentEntity = QWfDocumentEntity.wfDocumentEntity
    val element: QWfElementEntity = QWfElementEntity.wfElementEntity
    val ciComponent: QCIComponentDataEntity = QCIComponentDataEntity.cIComponentDataEntity
    val instanceViewer: QWfInstanceViewerEntity = QWfInstanceViewerEntity.wfInstanceViewerEntity
    val code: QCodeEntity = QCodeEntity.codeEntity

    override fun findTodoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): QueryResults<WfInstanceListViewDto> {

        val elementDataSub = QWfElementDataEntity("elementDataSub")
        val roleSub = QAliceUserRoleMapEntity("roleSub")
        val startDtSubToken = QWfTokenEntity.wfTokenEntity
        val builder = getInstancesWhereCondition(
            tokenSearchCondition.searchDocumentId,
            tokenSearchCondition.searchValue,
            tokenSearchCondition.searchFromDt,
            tokenSearchCondition.searchToDt
        )

        val assigneeUsers = JPAExpressions
            .select(elementDataSub.element.elementId)
            .from(elementDataSub)
            .where(
                elementDataSub.element.`in`(
                    JPAExpressions
                        .select(elementDataSub.element)
                        .from(elementDataSub)
                        .innerJoin(elementDataSub.element, element)
                        .where(
                            element.elementType.`in`(WfElementConstants.ElementType.USER_TASK.value),
                            elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE_TYPE.value),
                            elementDataSub.attributeValue.eq(WfTokenConstants.AssigneeType.USERS.code)
                        )
                ),
                elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE.value),
                elementDataSub.attributeValue.eq(tokenSearchCondition.userKey)
            )

        val assigneeGroups = JPAExpressions
            .select(elementDataSub.element.elementId)
            .from(elementDataSub)
            .where(
                elementDataSub.element.`in`(
                    JPAExpressions
                        .select(elementDataSub.element)
                        .from(elementDataSub)
                        .innerJoin(elementDataSub.element, element)
                        .where(
                            element.elementType.`in`(WfElementConstants.ElementType.USER_TASK.value),
                            elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE_TYPE.value),
                            elementDataSub.attributeValue.eq(WfTokenConstants.AssigneeType.GROUPS.code)
                        )
                ),
                elementDataSub.attributeId.eq(WfElementConstants.AttributeId.ASSIGNEE.value),
                elementDataSub.attributeValue.`in`(
                    JPAExpressions
                        .select(roleSub.role.roleId)
                        .from(roleSub)
                        .where(roleSub.user.userKey.eq(tokenSearchCondition.userKey))
                )
            )

        val viewer = JPAExpressions
            .select(instanceViewer.instance.instanceId)
            .from(instanceViewer)
            .where(instanceViewer.viewer.userKey.eq(tokenSearchCondition.userKey))

        builder.and(instance.instanceStatus.`in`(status))
        builder.and(token.tokenStatus.`in`(tokenStatus))
        builder.and(token.element.elementType.`in`(WfElementConstants.ElementType.USER_TASK.value))

        if (!hasDocumentViewAuth()) {
            builder.and(
                token.assigneeId.eq(tokenSearchCondition.userKey)
                    .or(
                        instance.instanceId.`in`(viewer)
                    )
                    .or(
                        token.element.elementId.`in`(assigneeUsers)
                    ).or(
                        token.element.elementId.`in`(assigneeGroups)
                    )
            )
        }
        // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
        builder.and(
            token.tokenId.eq(
                JPAExpressions
                    .select(token.tokenId.max())
                    .from(token)
                    .where(token.tokenStartDt.eq(
                        from(startDtSubToken)
                            .select(startDtSubToken.tokenStartDt.max())
                            .where(startDtSubToken.instance.instanceId.eq(instance.instanceId))
                    ))
            )
        )
        builder.and(
            token.tokenAction.notIn(WfTokenConstants.FinishAction.CANCEL.code)
                .or(token.tokenAction.isNull)
        )
        val query = getInstancesQuery(tokenSearchCondition.tagArray)
            .where(builder)
        this.orderSpecifier(tokenSearchCondition, query)
        if (tokenSearchCondition.isPaging) {
            query.limit(tokenSearchCondition.contentNumPerPage)
            query.offset((tokenSearchCondition.pageNum - 1) * tokenSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }

    /**
     * 화면 정렬 설정
     */
    private fun orderSpecifier(
        tokenSearchCondition: TokenSearchCondition,
        query: JPQLQuery<WfInstanceListViewDto>
    ): JPQLQuery<WfInstanceListViewDto> {
        if (tokenSearchCondition.orderColName.isNullOrEmpty()) {
            query.orderBy(instance.instanceStartDt.desc())
        } else {
            val direction = when (tokenSearchCondition.orderDir) {
                QuerydslConstants.OrderSpecifier.DESC.code -> Order.DESC
                else -> Order.ASC
            }
            when (tokenSearchCondition.orderColName) {
                QuerydslConstants.OrderColumn.CREATE_USER_NAME.code -> {
                    query.orderBy(OrderSpecifier(direction, instance.instanceCreateUser.userName))
                }
                QuerydslConstants.OrderColumn.CREATE_DT.code -> {
                    query.orderBy(OrderSpecifier(direction, instance.instanceStartDt))
                }
                QuerydslConstants.OrderColumn.DOCUMENT_GROUP.code -> {
                    query.orderBy(OrderSpecifier(direction, code.codeName))
                }
                QuerydslConstants.OrderColumn.ASSIGNEE_USER_NAME.code -> {
                    query.orderBy(OrderSpecifier(direction, user.userName))
                }
                QuerydslConstants.OrderColumn.ELEMENT_NAME.code -> {
                    query.orderBy(OrderSpecifier(direction, token.element.elementName))
                }
                else -> {
                    query.orderBy(OrderSpecifier(direction, Expressions.stringPath(instance, tokenSearchCondition.orderColName)))
                }
            }
        }
        return query
    }

    override fun findRequestedInstances(tokenSearchCondition: TokenSearchCondition): QueryResults<WfInstanceListViewDto> {
        val tokenSub = QWfTokenEntity("tokenSub")
        val startDtSubToken = QWfTokenEntity.wfTokenEntity
        val builder = getInstancesWhereCondition(
            tokenSearchCondition.searchDocumentId,
            tokenSearchCondition.searchValue,
            tokenSearchCondition.searchFromDt,
            tokenSearchCondition.searchToDt
        )
        if (!hasDocumentViewAuth()) {
            builder.and(instance.instanceCreateUser.userKey.eq(tokenSearchCondition.userKey))
        }
        // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
        builder.and(
            token.tokenId.eq(
                JPAExpressions
                    .select(tokenSub.tokenId.max())
                    .from(tokenSub)
                    .where(tokenSub.tokenStartDt.eq(
                        from(startDtSubToken)
                            .select(startDtSubToken.tokenStartDt.max())
                            .where(startDtSubToken.instance.instanceId.eq(instance.instanceId))
                    ))
            )
        )
        builder.and(
            token.tokenAction.notIn(WfTokenConstants.FinishAction.CANCEL.code)
                .or(token.tokenAction.isNull)
        )

        val query = getInstancesQuery(tokenSearchCondition.tagArray)
            .where(builder)
        this.orderSpecifier(tokenSearchCondition, query)
        if (tokenSearchCondition.isPaging) {
            query.limit(tokenSearchCondition.contentNumPerPage)
            query.offset((tokenSearchCondition.pageNum - 1) * tokenSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }

    override fun findRelationInstances(
        status: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): QueryResults<WfInstanceListViewDto> {

        val tokenSub = QWfTokenEntity("tokenSub")
        val startDtSubToken = QWfTokenEntity.wfTokenEntity
        val builder = getInstancesWhereCondition(
            tokenSearchCondition.searchDocumentId,
            tokenSearchCondition.searchValue,
            tokenSearchCondition.searchFromDt,
            tokenSearchCondition.searchToDt
        )
        builder.and(instance.instanceStatus.`in`(status))
        // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
        builder.and(
            token.tokenId.eq(
                JPAExpressions
                    .select(tokenSub.tokenId.max())
                    .from(tokenSub)
                    .where(tokenSub.tokenStartDt.eq(
                        from(startDtSubToken)
                            .select(startDtSubToken.tokenStartDt.max())
                            .where(startDtSubToken.instance.instanceId.eq(instance.instanceId))
                    ))
            )
        )
        if (!hasDocumentViewAuth()) {
            builder.and(
                instance.instanceId.`in`(
                    JPAExpressions
                        .select(tokenSub.instance.instanceId)
                        .from(tokenSub)
                        .leftJoin(instanceViewer).on(tokenSub.instance.instanceId.eq(instanceViewer.instance.instanceId))
                        .where(tokenSub.assigneeId.eq(tokenSearchCondition.userKey)
                            .or(
                                instanceViewer.viewer.userKey.eq(tokenSearchCondition.userKey)
                            )
                        )
                )
            )
        }
        status?.forEach { statusValue ->
            if (statusValue == WfInstanceConstants.Status.FINISH.code) {
                builder.and(
                    token.tokenAction.notIn(WfTokenConstants.FinishAction.CANCEL.code)
                )
            }
        }
        val query = getInstancesQuery(tokenSearchCondition.tagArray)
            .where(builder)
        this.orderSpecifier(tokenSearchCondition, query)
        if (tokenSearchCondition.isPaging) {
            query.limit(tokenSearchCondition.contentNumPerPage)
            query.offset((tokenSearchCondition.pageNum - 1) * tokenSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }

    override fun findInstanceHistory(instanceId: String): MutableList<RestTemplateInstanceHistoryDto> {
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
                    token.tokenAction,
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
        delete(tokenData).where(tokenData.token.`in`(tokens)).execute()
        delete(token).where(token.instance.`in`(instances)).execute()
        delete(folder).where(folder.instance.`in`(instances)).execute()
        delete(comment).where(comment.instance.`in`(instances)).execute()
        delete(instance).where(instance.instanceId.`in`(instanceIds)).execute()
        delete(ciComponent).where(ciComponent.instanceId.`in`(instanceIds)).execute()
        delete(tag).where(tag.tagType.eq(AliceTagConstants.TagType.INSTANCE.code).and(tag.targetId.`in`(instanceIds)))
            .execute()
    }

    /**
     * 문서함 목록 조회.
     */
    private fun getInstancesQuery(tags: List<String>): JPQLQuery<WfInstanceListViewDto> {
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
                        document.documentIcon,
                        code.codeName
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
                    ),
                    Projections.constructor(
                        WfInstanceListUserDto::class.java,
                        user.userId,
                        user.userName
                    )
                )
            )
            .innerJoin(instance).on(token.instance.eq(instance))
            .fetchJoin()
            .innerJoin(document).on(instance.document.eq(document))
            .fetchJoin()
            .leftJoin(user).on(token.assigneeId.eq(user.userKey))
            .fetchJoin()
            .leftJoin(code).on(document.documentGroup.eq(code.code))
            .fetchJoin()
        if (tags.isNotEmpty()) {
            query.where(
                instance.instanceId.`in`(
                    JPAExpressions
                        .select(tag.targetId)
                        .from(tag)
                        .where(
                            (tag.tagType.eq(AliceTagConstants.TagType.INSTANCE.code))
                                .and(tag.tagValue.toLowerCase().`in`(tags))
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
        fromDt: String,
        toDt: String
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
            val startDtSubToken = QWfTokenEntity.wfTokenEntity
            builder.and(
                instance.instanceCreateUser.userName.containsIgnoreCase(searchValue.trim())
                    .or(
                        token.tokenId.`in`(
                            JPAExpressions
                                .select(tokenDataSub.token.tokenId)
                                .from(tokenDataSub)
                                // 최신 토큰값 조회를 위해 tokenId.max() 대신 tokenStartDt.max()로 수정 (#12080 참고)
                                .where(
                                    tokenDataSub.token.tokenId.eq(
                                        JPAExpressions
                                            .select(tokenSub.tokenId.max())
                                            .from(tokenSub)
                                            .where(tokenSub.tokenStartDt.eq(
                                                from(startDtSubToken)
                                                    .select(startDtSubToken.tokenStartDt.max())
                                                    .where(startDtSubToken.instance.instanceId.eq(instance.instanceId))
                                            ))
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
                                    tokenDataSub.value.containsIgnoreCase(searchValue.trim())
                                )
                        )
                    )
                    .or(
                        instance.documentNo.containsIgnoreCase(searchValue.trim())
                    )
            )
        }
        if (fromDt.isNotEmpty()) builder.and(
            instance.instanceStartDt.goe(
                LocalDateTime.parse(
                    fromDt,
                    DateTimeFormatter.ISO_DATE_TIME
                )
            )
        )
        if (toDt.isNotEmpty()) builder.and(
            instance.instanceStartDt.lt(
                LocalDateTime.parse(
                    toDt,
                    DateTimeFormatter.ISO_DATE_TIME
                )
            )
        )
        return builder
    }

    override fun findAllInstanceListByRelatedCheck(
        instanceId: String,
        searchValue: String
    ): MutableList<RestTemplateInstanceListDto> {
        val query = from(instance)
            .select(
                Projections.constructor(
                    RestTemplateInstanceListDto::class.java,
                    instance.instanceId,
                    document.documentName,
                    instance.documentNo,
                    instance.instanceStartDt,
                    instance.instanceEndDt,
                    user.userKey,
                    user.userName,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(
                            CaseBuilder()
                                .`when`(folder.count().gt(0)).then(true).otherwise(false))
                            .from(folder)
                            .where(folder.relatedType.`in`(
                                FolderConstants.RelatedType.REFERENCE.code, FolderConstants.RelatedType.RELATED.code)
                                .and(folder.instance.eq(instance))
                                .and(folder.folderId.eq(
                                    from(folder)
                                        .select(folder.folderId)
                                        .where(folder.instance.instanceId.eq(instanceId)
                                            .and(folder.relatedType.eq(FolderConstants.RelatedType.ORIGIN.code)))
                                ))
                            ), "related"
                    )
                )
            )
            .distinct()
            .innerJoin(document).on(document.documentId.eq(instance.document.documentId))
            .leftJoin(user).on(user.userKey.eq(instance.instanceCreateUser.userKey))
            .where(
                instance.instanceId.notIn(instanceId).and(
                    instance.instanceId.notIn(
                        JPAExpressions
                            .select(token.instance.instanceId)
                            .from(token)
                            .where(token.tokenAction.eq(WfTokenConstants.FinishAction.CANCEL.code))
                    )
                )
            )
        if (searchValue.isNotEmpty()) {
            query.where(
                document.documentName.likeIgnoreCase("%$searchValue%")
                    .or(user.userName.likeIgnoreCase("%$searchValue%"))
            )
        }
        query.orderBy(instance.instanceStartDt.asc())
        return query.fetch()
    }

    override fun getInstanceListInTag(
        tagValue: String,
        range: ChartRange,
        documentStatus: String?
    ): List<WfInstanceEntity> {
        val component = QWfComponentEntity.wfComponentEntity
        val query = from(instance)
            .where(
                instance.document.documentId.`in`(
                    JPAExpressions.select(document.documentId)
                        .from(document)
                        .where(
                            document.form.formId.`in`(
                                JPAExpressions.select(component.form.formId)
                                    .from(component)
                                    .where(
                                        component.componentId.`in`(
                                            JPAExpressions.select(tag.targetId)
                                                .from(tag)
                                                .where(tag.tagValue.eq(tagValue))
                                                .where(tag.tagType.eq(AliceTagConstants.TagType.COMPONENT.code))
                                        )
                                    )
                                    .where(component.form.formStatus.ne(WfFormConstants.FormStatus.EDIT.value))
                            )
                        )
                        .where(document.documentStatus.ne(WfDocumentConstants.Status.TEMPORARY.code))
                )
            )
        if (documentStatus == ChartConstants.DocumentStatus.EVEN_RUNNING.code) {
            query.where(
                (instance.instanceStatus.eq(WfInstanceConstants.Status.FINISH.code)
                    .and(instance.instanceStartDt.goe(range.from))
                    .and(instance.instanceEndDt.loe(range.to)))
                    .or(
                        (instance.instanceStatus.eq(WfInstanceConstants.Status.RUNNING.code)
                            .and(instance.instanceStartDt.goe(range.from)))
                    )
            )
        } else {
            query.where(
                instance.instanceStatus.eq(WfInstanceConstants.Status.FINISH.code)
                    .and(
                        instance.instanceStartDt.goe(range.from)
                            .and(instance.instanceEndDt.loe(range.to))
                    )
            )
        }
        return query.fetch()
    }

    /**
     * 전체 문서 허용 권한 여부.
     */
    private fun hasDocumentViewAuth(): Boolean {
        return currentSessionUser.getAuth().contains(AuthConstants.AuthType.DOCUMENT_VIEW.value)
    }
}
