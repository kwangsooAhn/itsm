/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.framework.auth.entity.QAliceUserEntity
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.customCode.dto.CustomCodeListDto
import co.brainz.itsm.customCode.dto.CustomCodeSearchDto
import co.brainz.itsm.customCode.entity.QCustomCodeEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomCodeRepositoryImpl : QuerydslRepositorySupport(PortalBoardAdminEntity::class.java),
    CustomCodeRepositoryCustom {

    override fun findByCustomCodeList(customCodeSearchDto: CustomCodeSearchDto): List<CustomCodeListDto> {
        val customCode = QCustomCodeEntity.customCodeEntity
        val user = QAliceUserEntity.aliceUserEntity
        val query = from(customCode)
            .select(
                Projections.constructor(
                    CustomCodeListDto::class.java,
                    customCode.customCodeId,
                    customCode.type,
                    customCode.customCodeName,
                    Expressions.numberPath(Long::class.java, "0"),
                    customCode.createDt,
                    customCode.createUser.userName
                )
            )
            .innerJoin(customCode.createUser, user)
            .where(
                super.like(customCode.type, customCodeSearchDto.searchType)
            )
            .where(
                super.like(customCode.customCodeName, customCodeSearchDto.search.toString())
            )
            .orderBy(customCode.customCodeName.asc())
        if (customCodeSearchDto.viewType != "editor") {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT).offset(customCodeSearchDto.offset)
        }

        val result = query.fetchResults()
        val customCodeList = mutableListOf<CustomCodeListDto>()
        for (data in result.results) {
            data.totalCount = result.total
            customCodeList.addAll(
                listOf(data)
            )
        }

        return customCodeList
    }
}
