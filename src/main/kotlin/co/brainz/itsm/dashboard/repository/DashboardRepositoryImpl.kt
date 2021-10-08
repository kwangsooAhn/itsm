/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.auth.entity.QAliceUserRoleMapEntity
import co.brainz.itsm.dashboard.dto.DashboardGroupCountDto
import co.brainz.itsm.dashboard.dto.DashboardSearchCondition
import co.brainz.workflow.document.entity.QWfDocumentEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.QWfElementDataEntity
import co.brainz.workflow.element.entity.QWfElementEntity
import co.brainz.workflow.instance.entity.QWfInstanceEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.QWfTokenEntity
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import com.querydsl.jpa.JPQLQuery
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Repository
class DashboardRepositoryImpl : QuerydslRepositorySupport(WfInstanceEntity::class.java), DashboardRepositoryCustom {

    val instance: QWfInstanceEntity = QWfInstanceEntity.wfInstanceEntity
    val token: QWfTokenEntity = QWfTokenEntity.wfTokenEntity
    val document: QWfDocumentEntity = QWfDocumentEntity.wfDocumentEntity
    val element: QWfElementEntity = QWfElementEntity.wfElementEntity

    override fun findTodoStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        val elementDataSub = QWfElementDataEntity("elementDataSub")
        val roleSub = QAliceUserRoleMapEntity("roleSub")
        val tokenSub = QWfTokenEntity("tokenSub")
        val builder = this.getInstancesWhereCondition(dashboardSearchCondition)

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
                elementDataSub.attributeValue.eq(dashboardSearchCondition.userKey)
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
                        .where(roleSub.user.userKey.eq(dashboardSearchCondition.userKey))
                )
            )

        builder.and(instance.instanceStatus.`in`(dashboardSearchCondition.instanceStatus))
        builder.and(token.tokenStatus.`in`(dashboardSearchCondition.tokenStatus))
        builder.and(token.element.elementType.`in`(WfElementConstants.ElementType.USER_TASK.value))
        builder.and(
            token.assigneeId.eq(dashboardSearchCondition.userKey)
                .or(
                    token.element.elementId.`in`(assigneeUsers)
                ).or(
                    token.element.elementId.`in`(assigneeGroups)
                )
        )

        builder.and(
            token.tokenId.eq(
                JPAExpressions
                    .select(tokenSub.tokenId.max())
                    .from(tokenSub)
                    .where(tokenSub.instance.instanceId.eq(instance.instanceId))
            )
        )
        builder.and(
            token.tokenAction.notIn(WfTokenConstants.FinishAction.CANCEL.code)
                .or(token.tokenAction.isNull)
        )

        val query = getDocumentGroupCountQuery()
        return query
            .where(builder)
            .fetchResults()
    }

    override fun findRunningStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        val tokenSub = QWfTokenEntity("tokenSub")
        val builder = this.getInstancesWhereCondition(dashboardSearchCondition)

        builder.and(instance.instanceStatus.`in`(dashboardSearchCondition.instanceStatus))
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
                    .where(tokenSub.assigneeId.eq(dashboardSearchCondition.userKey))
            )
        )
        builder.and(
            token.tokenAction.notIn(WfTokenConstants.FinishAction.CANCEL.code)
        )

        val query = this.getDocumentGroupCountQuery()
        return query
            .where(builder)
            .fetchResults()
    }

    override fun findMonthDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        return this.findRunningStatistic(dashboardSearchCondition)
    }

    override fun findDoneStatistic(dashboardSearchCondition: DashboardSearchCondition): QueryResults<DashboardGroupCountDto> {
        return this.findRunningStatistic(dashboardSearchCondition)
    }

    /**
     * 인스턴스 공통 조건절 (날짜 범위 등)
     */
    private fun getInstancesWhereCondition(dashboardSearchCondition: DashboardSearchCondition): BooleanBuilder {
        val builder = BooleanBuilder()

        if (dashboardSearchCondition.searchFromDt.isNotEmpty()) {
            builder.and(
                instance.instanceEndDt.goe(
                    LocalDateTime.parse(
                        dashboardSearchCondition.searchFromDt,
                        DateTimeFormatter.ISO_DATE_TIME
                    )
                )
            )
        }
        if (dashboardSearchCondition.searchToDt.isNotEmpty()) {
            builder.and(
                instance.instanceEndDt.lt(
                    LocalDateTime.parse(
                        dashboardSearchCondition.searchToDt,
                        DateTimeFormatter.ISO_DATE_TIME
                    )
                )
            )
        }

        return builder
    }

    /**
     * 통계 카운트 조회
     */
    private fun getDocumentGroupCountQuery(): JPQLQuery<DashboardGroupCountDto> {
        return from(token)
            .select(
                Projections.constructor(
                    DashboardGroupCountDto::class.java,
                    document.documentGroup,
                    document.documentGroup.count()
                )
            )
            .innerJoin(instance).on(token.instance.eq(instance))
            .fetchJoin()
            .innerJoin(document).on(instance.document.eq(document))
            .fetchJoin()
            .groupBy(document.documentGroup)
            .fetchJoin()
    }
}
