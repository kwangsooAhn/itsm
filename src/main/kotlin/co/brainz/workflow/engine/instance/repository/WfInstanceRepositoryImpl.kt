package co.brainz.workflow.engine.instance.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.workflow.engine.document.entity.QWfDocumentEntity
import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.engine.instance.entity.QWfInstanceEntity
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.engine.token.entity.QWfTokenEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class WfInstanceRepositoryImpl : QuerydslRepositorySupport(WfInstanceEntity::class.java),
    WfInstanceRepositoryCustom {

    override fun findTodoInstances(
            status: String,
            documentId: String,
            fromDt: String,
            toDt: String,
            dateFormat: String
    ): List<WfInstanceListViewDto> {
        val instance = QWfInstanceEntity.wfInstanceEntity
        val token = QWfTokenEntity.wfTokenEntity
        val document = QWfDocumentEntity.wfDocumentEntity

        val from = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", fromDt, dateFormat)

        val to = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", toDt, dateFormat)

        val builder = BooleanBuilder()
        builder.and(instance.instanceStatus.eq(status))
        builder.and(token.tokenStatus.eq(status))
        if (documentId.isNotEmpty()) {
            builder.and(document.documentId.eq(documentId))
        }
        builder.and(instance.instanceStartDt.between(from, to))

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
            .where(builder)
            .orderBy(instance.instanceStartDt.desc())
            .fetch()
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
        val instance = QWfInstanceEntity.wfInstanceEntity
        val token = QWfTokenEntity.wfTokenEntity
        val document = QWfDocumentEntity.wfDocumentEntity

        val tokenSub = QWfTokenEntity("tokenSub")

        val from = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", fromDt, dateFormat)

        val to = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", toDt, dateFormat)

        val builder = BooleanBuilder()
        builder.and(instance.instanceCreateUser.userKey.eq(userKey))
        builder.and(token.tokenId.eq(
            JPAExpressions.select(tokenSub.tokenId.max()).from(tokenSub).where(tokenSub.instance.instanceId.eq(instance.instanceId)))
        )
        if (documentId.isNotEmpty()) {
            builder.and(document.documentId.eq(documentId))
        }
        if (searchValue.isNotEmpty()) {
            builder.and(instance.instanceCreateUser.userName.contains(searchValue))
        }
        builder.and(instance.instanceStartDt.between(from, to))

        /*
        SELECT array_agg(t.value)
        FROM   WfTokenDataEntity t
        WHERE  t.tokenId = :tokenId
        AND    t.componentId IN (SELECT c.componentId
               FROM   WfComponentEntity c
               WHERE  c.form.formId = :formId
               AND    c.isTopic = :isTopic
               AND    c.componentType IN :componentTypes)
        */

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
                .where(builder)
                .orderBy(instance.instanceStartDt.desc())
                .limit(7)
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
        val instance = QWfInstanceEntity.wfInstanceEntity
        val token = QWfTokenEntity.wfTokenEntity
        val document = QWfDocumentEntity.wfDocumentEntity

        val from = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", fromDt, dateFormat)

        val to = Expressions.dateTemplate(
                LocalDateTime::class.java, "TO_TIMESTAMP({0}, {1})", toDt, dateFormat)

        val tokenSub = QWfTokenEntity("tokenSub")
        val builder = BooleanBuilder()
        builder.and(instance.instanceStatus.eq(status))
        builder.and(
            token.tokenId.eq(
                JPAExpressions.select(tokenSub.tokenId.max()).from(tokenSub).where(tokenSub.instance.instanceId.eq(instance.instanceId)))
        )
        builder.and(
            instance.instanceId.`in`(
                JPAExpressions.select(tokenSub.instance.instanceId).from(tokenSub).where(tokenSub.assigneeId.eq(userKey)))
        )
        if (documentId.isNotEmpty()) {
            builder.and(document.documentId.eq(documentId))
        }
        if (searchValue.isNotEmpty()) {
            builder.and(instance.instanceCreateUser.userName.contains(searchValue))
        }
        builder.and(instance.instanceStartDt.between(from, to))

        /*
        SELECT array_agg(t.value)
        FROM   WfTokenDataEntity t
        WHERE  t.tokenId = :tokenId
        AND    t.componentId IN (SELECT c.componentId
               FROM   WfComponentEntity c
               WHERE  c.form.formId = :formId
               AND    c.isTopic = :isTopic
               AND    c.componentType IN :componentTypes)
        */

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
                .where(builder)
                .orderBy(instance.instanceStartDt.desc())
                .limit(7)
                .offset(offset)
                .fetchResults()
    }

    override fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto> {
        val token = QWfTokenEntity.wfTokenEntity
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
