/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.customCode.dto.CustomCodeCoreDto
import co.brainz.itsm.customCode.dto.CustomCodeListDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchCondition
import co.brainz.itsm.customCode.entity.QCustomCodeEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomCodeRepositoryImpl : QuerydslRepositorySupport(PortalBoardAdminEntity::class.java),
    CustomCodeRepositoryCustom {

    override fun findByCustomCodeList(customCodeSearchCondition: CustomCodeSearchCondition): QueryResults<CustomCodeListDto> {
        val customCode = QCustomCodeEntity.customCodeEntity
        val user = QAliceUserEntity.aliceUserEntity
        val query = from(customCode)
            .select(
                Projections.constructor(
                    CustomCodeListDto::class.java,
                    customCode.customCodeId,
                    customCode.type,
                    customCode.customCodeName,
                    customCode.sessionKey,
                    Expressions.numberPath(Long::class.java, "0"),
                    customCode.createDt,
                    customCode.createUser.userName
                )
            )
            .innerJoin(customCode.createUser, user)
            .where(
                super.eq(customCode.type, customCodeSearchCondition.searchType)
            )
            .where(
                super.likeIgnoreCase(customCode.customCodeName, customCodeSearchCondition.searchValue)
            )
            .orderBy(customCode.customCodeName.asc())
        if (customCodeSearchCondition.isPaging) {
            query.limit(customCodeSearchCondition.contentNumPerPage)
            query.offset((customCodeSearchCondition.pageNum - 1) * customCodeSearchCondition.contentNumPerPage)
        }

        return query.fetchResults()
    }

    override fun findByCustomCode(customCodeId: String): CustomCodeCoreDto {
        val customCode = QCustomCodeEntity.customCodeEntity
        val query = from(customCode)
            .select(
                Projections.constructor(
                    CustomCodeCoreDto::class.java,
                    customCode.customCodeId,
                    customCode.customCodeName,
                    customCode.type,
                    customCode.targetTable,
                    customCode.searchColumn,
                    customCode.valueColumn,
                    customCode.pCode,
                    customCode.condition,
                    customCode.sessionKey
                )
            )
            .where(
                super.eq(customCode.customCodeId, customCodeId)
            )

        val result = query.fetchResults()
        return CustomCodeCoreDto(
            customCodeId = result.results[0].customCodeId,
            customCodeName = result.results[0].customCodeName,
            type = result.results[0].type,
            targetTable = result.results[0].targetTable,
            searchColumn = result.results[0].searchColumn,
            valueColumn = result.results[0].valueColumn,
            pCode = result.results[0].pCode,
            condition = result.results[0].condition,
            sessionKey = result.results[0].sessionKey
        )
    }
}
