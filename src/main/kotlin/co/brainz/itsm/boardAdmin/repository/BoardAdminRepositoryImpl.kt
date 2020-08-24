/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.repository

import co.brainz.itsm.board.entity.QPortalBoardEntity
import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminListDto
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.boardAdmin.entity.QPortalBoardAdminEntity
import co.brainz.itsm.constants.ItsmConstants
import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Projections
import com.querydsl.jpa.JPAExpressions
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardAdminRepositoryImpl : QuerydslRepositorySupport(PortalBoardAdminEntity::class.java),
    BoardAdminRepositoryCustom {

    override fun findByBoardAdminList(
        search: String,
        offset: Long
    ): List<BoardAdminListDto> {
        val boardAdmin = QPortalBoardAdminEntity.portalBoardAdminEntity
        val board = QPortalBoardEntity("board")
        val query = from(boardAdmin)
            .select(
                Projections.constructor(
                    BoardAdminDto::class.java,
                    boardAdmin.boardAdminId,
                    boardAdmin.boardAdminTitle,
                    boardAdmin.boardAdminDesc,
                    boardAdmin.boardAdminSort,
                    boardAdmin.boardUseYn,
                    boardAdmin.replyYn,
                    boardAdmin.commentYn,
                    boardAdmin.categoryYn,
                    boardAdmin.attachYn,
                    boardAdmin.attachFileSize,
                    ExpressionUtils.`as`(
                        JPAExpressions.select(board.boardId.count()).from(board)
                            .where(board.boardAdmin.boardAdminId.eq(boardAdmin.boardAdminId)), "boardBoardCount"
                    ),
                    ExpressionUtils.`as`(
                        JPAExpressions.select(
                            board.boardId.count().`when`(0).then(true).otherwise(false)
                        ).from(board)
                            .where(board.boardAdmin.boardAdminId.eq(boardAdmin.boardAdminId)), "enabled"
                    ),
                    boardAdmin.createDt,
                    boardAdmin.createUser,
                    boardAdmin.updateDt,
                    boardAdmin.updateUser
                )
            ).where(
                super.likeIgnoreCase(
                    boardAdmin.boardAdminTitle, search
                )?.or(super.likeIgnoreCase(boardAdmin.createUser.userName, search))
            ).orderBy(boardAdmin.createDt.desc())
            .limit(ItsmConstants.SEARCH_DATA_COUNT)
            .offset(offset)
            .fetchResults()

        val boardAdminList = mutableListOf<BoardAdminListDto>()
        for (data in query.results) {
            val boardAdminListDto = BoardAdminListDto(
                boardAdminId = data.boardAdminId,
                boardAdminTitle = data.boardAdminTitle,
                categoryYn = data.categoryYn,
                boardBoardCount = data.boardBoardCount,
                totalCount = query.total,
                createDt = data.createDt,
                createUserName = data.createUser?.userName,
                updateDt = data.updateDt,
                updateUserName = data.updateUser?.userName
            )
            boardAdminList.add(boardAdminListDto)
        }
        return boardAdminList.toList()
    }
}
