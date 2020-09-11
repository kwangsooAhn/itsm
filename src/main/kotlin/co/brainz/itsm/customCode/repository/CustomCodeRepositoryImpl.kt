/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.customCode.dto.CustomCodeListDto
import co.brainz.itsm.customCode.dto.CustomCodeQueryResultDto
import co.brainz.itsm.customCode.entity.QCustomCodeColumnEntity
import co.brainz.itsm.customCode.entity.QCustomCodeEntity
import co.brainz.itsm.customCode.entity.QCustomCodeTableEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
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
        val customCodeTable = QCustomCodeTableEntity("customCodeTable")
        val customCodeColumn = QCustomCodeColumnEntity("customCodeColumn")

        val query = from(customCode)
            .select(
                Projections.constructor(
                    CustomCodeQueryResultDto::class.java,
                    customCode.customCodeId,
                    customCode.type,
                    customCode.customCodeName,
                    customCode.targetTable,
                    customCodeTable.customCodeTableName,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(customCodeColumn.customCodeColumn).from(customCodeColumn)
                            .where(
                                customCodeColumn.customCodeTable.eq(customCode.targetTable)
                                    .and(
                                        customCodeColumn.customCodeColumn.eq(customCode.searchColumn)
                                            .and(
                                                customCodeColumn.customCodeType.eq("search")
                                            )
                                    )
                            ), "searchColumn"
                    ),
                    ExpressionUtils.`as`(
                        JPAExpressions.select(customCodeColumn.customCodeColumnName).from(customCodeColumn)
                            .where(
                                customCodeColumn.customCodeTable.eq(customCode.targetTable)
                                    .and(
                                        customCodeColumn.customCodeColumn.eq(customCode.searchColumn)
                                            .and(
                                                customCodeColumn.customCodeType.eq("search")
                                            )
                                    )
                            ), "searchColumnName"
                    ),
                    ExpressionUtils.`as`(
                        JPAExpressions.select(customCodeColumn.customCodeColumn).from(customCodeColumn)
                            .where(
                                customCodeColumn.customCodeTable.eq(customCode.targetTable)
                                    .and(
                                        customCodeColumn.customCodeColumn.eq(customCode.searchColumn)
                                            .and(
                                                customCodeColumn.customCodeType.eq("value")
                                            )
                                    )
                            ), "valueColumn"
                    ),
                    ExpressionUtils.`as`(
                        JPAExpressions.select(customCodeColumn.customCodeColumnName).from(customCodeColumn)
                            .where(
                                customCodeColumn.customCodeTable.eq(customCode.targetTable)
                                    .and(
                                        customCodeColumn.customCodeColumn.eq(customCode.searchColumn)
                                            .and(
                                                customCodeColumn.customCodeType.eq("value")
                                            )
                                    )
                            ), "valueColumnName"
                    ),
                    customCode.pCode,
                    customCode.condition,
                    customCode.createDt,
                    customCode.createUser.userName,
                    customCode.updateDt,
                    customCode.updateUser.userName
                )
            )
            .leftJoin(customCodeTable).on(customCodeTable.customCodeTable.eq(customCode.targetTable))
            .innerJoin(customCode.createUser)
            .leftJoin(customCode.updateUser)
            .orderBy(customCode.customCodeName.asc())

        if (viewType != "formEditor") {
            query.limit(ItsmConstants.SEARCH_DATA_COUNT).offset(offset)
        }

        val customCodeList = mutableListOf<CustomCodeListDto>()
        for (data in query.fetchResults().results) {
            val customCodeListDto = CustomCodeListDto(
                customCodeId = data.customCodeId,
                type = data.type,
                customCodeName = data.customCodeName,
                targetTable = data.targetTable,
                targetTableName = data.targetTableName,
                searchColumn = data.searchColumn,
                searchColumnName = data.searchColumnName,
                valueColumn = data.valueColumn,
                valueColumnName = data.valueColumnName,
                pCode = data.pCode,
                condition = data.condition,
                totalCount = query.fetchResults().total,
                createDt = data.createDt,
                createUserName = data.createUserName,
                updateDt = data.updateDt,
                updateUserName = data.updateUserName
            )
            customCodeList.add(customCodeListDto)
        }

        return customCodeList.toList()
    }
}
