/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDto
import co.brainz.itsm.boardAdmin.entity.PortalBoardCategoryEntity
import co.brainz.itsm.boardAdmin.entity.QPortalBoardCategoryEntity
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardCategoryRepositoryImpl : QuerydslRepositorySupport(PortalBoardCategoryEntity::class.java),
    BoardCategoryRepositoryCustom {

    override fun findByCategoryList(
        boardAdminId: String
    ): List<BoardCategoryDto> {
        val boardCategory = QPortalBoardCategoryEntity.portalBoardCategoryEntity
        val board = QPortalBoardEntity("board")
        val query = from(boardCategory)
            .select(
                Projections.constructor(
                    BoardCategoryDto::class.java,
                    boardCategory.boardCategoryId,
                    boardCategory.boardAdmin.boardAdminId,
                    boardCategory.boardAdmin,
                    boardCategory.boardCategoryName,
                    boardCategory.boardCategorySort,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(board.boardId.count()).from(board)
                            .where(board.boardCategoryId.eq(boardCategory.boardCategoryId)), "boardCount"
                    )
                )
            ).where(
                super.eq(
                    boardCategory.boardAdmin.boardAdminId, boardAdminId
                )
            ).orderBy(boardCategory.boardCategorySort.asc())
            .fetch()

        val boardCategoryList = mutableListOf<BoardCategoryDto>()
        for (data in query) {
            val boardCategoryDto = BoardCategoryDto(
                boardCategoryId = data.boardCategoryId,
                boardAdminId = data.boardAdminId,
                boardAdmin = data.boardAdmin,
                boardCategoryName = data.boardCategoryName,
                boardCategorySort = data.boardCategorySort,
                boardCount = data.boardCount
            )
            boardCategoryList.add(boardCategoryDto)
        }
        return boardCategoryList.toList()
    }
}
