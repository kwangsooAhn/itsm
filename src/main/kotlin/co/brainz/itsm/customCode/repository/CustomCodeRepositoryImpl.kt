/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.customCode.dto.CustomCodeListDto
import co.brainz.itsm.customCode.entity.QCustomCodeEntity
import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.Expressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CustomCodeRepositoryImpl : QuerydslRepositorySupport(PortalBoardAdminEntity::class.java),
    CustomCodeRepositoryCustom {

    override fun findByCustomCodeList(
        offset: Long,
        viewType: String?
    ): List<CustomCodeListDto> {

        val customCode = QCustomCodeEntity.customCodeEntity
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
            .orderBy(customCode.customCodeName.asc())
        if (viewType != "formEditor") {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT).offset(offset)
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
