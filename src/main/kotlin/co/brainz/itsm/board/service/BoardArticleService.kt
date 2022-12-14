/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.service

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.resourceManager.dto.AliceFileDto
import co.brainz.framework.resourceManager.provider.AliceResourceProvider
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.board.dto.BoardArticleCommentDto
import co.brainz.itsm.board.dto.BoardArticleListReturnDto
import co.brainz.itsm.board.dto.BoardArticleSaveDto
import co.brainz.itsm.board.dto.BoardArticleSearchCondition
import co.brainz.itsm.board.dto.BoardArticleViewDto
import co.brainz.itsm.board.dto.BoardCategoryDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.entity.PortalBoardCommentEntity
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.PortalBoardReadEntity
import co.brainz.itsm.board.repository.BoardAdminRepository
import co.brainz.itsm.board.repository.BoardCategoryRepository
import co.brainz.itsm.board.repository.BoardCommentRepository
import co.brainz.itsm.board.repository.BoardReadRepository
import co.brainz.itsm.board.repository.BoardRepository
import co.brainz.itsm.user.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import javax.transaction.Transactional
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class BoardArticleService(
    private val boardRepository: BoardRepository,
    private val boardReadRepository: BoardReadRepository,
    private val boardCommentRepository: BoardCommentRepository,
    private val boardAdminRepository: BoardAdminRepository,
    private val boardCategoryRepository: BoardCategoryRepository,
    private val aliceResourceProvider: AliceResourceProvider,
    private val userDetailsService: AliceUserDetailsService,
    private val userService: UserService
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val auth = AuthConstants.AuthType.PORTAL_MANAGE.value

    /**
     * [boardArticleSearchCondition]??? ????????? ????????? ????????? [List<BoardRestDto>]?????? ?????? ??????.
     */
    fun getBoardArticleList(boardArticleSearchCondition: BoardArticleSearchCondition): BoardArticleListReturnDto {
        val pagingResult = boardRepository.findByBoardList(boardArticleSearchCondition)
        val boardArticleList = BoardArticleListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = boardRepository.count(),
                currentPageNum = boardArticleSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / boardArticleSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            ),
            categoryUseYn = false
        )

        val boardAdmin = boardAdminRepository.findByBoardAdminId(boardArticleSearchCondition.boardAdminId)
        if (boardAdmin != null) {
            boardArticleList.categoryUseYn = boardAdmin.categoryYn
        }
        return boardArticleList
    }

    /**
     * ????????? ??????.
     *
     */
    @Transactional
    fun saveBoardArticle(boardArticleSaveDto: BoardArticleSaveDto): ZResponse {
        val updatePortalBoardEntity = boardRepository.findById(boardArticleSaveDto.boardId).orElse(null)
        if (boardArticleSaveDto.boardId.isNotEmpty()) {
            updatePortalBoardEntity.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        }

        var status = ZResponseConstants.STATUS.SUCCESS
        val boardAdminId = boardArticleSaveDto.boardAdminId
        val portalBoardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        try {
            val boardCount = boardRepository.countByBoardAdminId(boardAdminId)
            var boardSeq = 0L
            if (boardCount > 0) {
                boardSeq = boardRepository.findMaxBoardSeq(boardAdminId)
            }
            val portalBoardEntity = PortalBoardEntity(
                boardId = boardArticleSaveDto.boardId,
                boardAdmin = portalBoardAdminEntity,
                boardCategoryId = boardArticleSaveDto.boardCategoryId,
                boardSeq = boardSeq + 1,
                boardGroupId = boardSeq + 1,
                boardLevelId = updatePortalBoardEntity?.boardLevelId ?: 0,
                boardOrderSeq = updatePortalBoardEntity?.boardOrderSeq ?: 0,
                boardTitle = boardArticleSaveDto.boardTitle,
                boardContents = boardArticleSaveDto.boardContents
            )
            val savedPortalBoardEntity = boardRepository.save(portalBoardEntity)
            aliceResourceProvider.setUploadFileLoc(
                AliceFileDto(
                    ownId = savedPortalBoardEntity.boardId,
                    fileSeq = boardArticleSaveDto.fileSeqList,
                    delFileSeq = boardArticleSaveDto.delFileSeqList
                )
            )
        } catch (e: Exception) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
            e.printStackTrace()
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ????????? ?????? ??????.
     *
     * @param boardArticleCommentDto
     */
    @Transactional
    fun saveBoardArticleComment(boardArticleCommentDto: BoardArticleCommentDto): ZResponse {
        if (boardArticleCommentDto.boardCommentId.isNotEmpty()) {
            val commentDto = boardCommentRepository.findById(boardArticleCommentDto.boardCommentId).get()
            commentDto.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        }
        val boardPortalBoardEntity = boardRepository.findById(boardArticleCommentDto.boardId).orElse(null)
        val portalBoardCommentEntity = PortalBoardCommentEntity(
            boardCommentId = boardArticleCommentDto.boardCommentId,
            commentBoard = boardPortalBoardEntity,
            boardCommentContents = boardArticleCommentDto.boardCommentContents
        )
        boardCommentRepository.save(portalBoardCommentEntity)
        return ZResponse()
    }

    /**
     * ????????? ?????? ??????.
     *
     * @param boardId
     * @param type
     * @return BoardDto
     */
    @Transactional
    fun getBoardArticleDetail(boardId: String, type: String): BoardArticleViewDto {
        val boardDto = boardRepository.findByBoardId(boardId)
        if (type == "edit") {
            boardDto.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        }
        val boardReadEntity = boardReadRepository.findById(boardId).orElse(PortalBoardReadEntity())
        if (type == "view") {
            boardReadEntity.boardId = boardId
            boardReadEntity.boardReadCount = boardReadEntity.boardReadCount?.plus(1)
            boardReadRepository.save(boardReadEntity)
        }

        if (!boardDto.boardCategoryId.isNullOrEmpty()) {
            boardDto.boardCategoryName =
                boardDto.boardCategoryId?.let { boardCategoryRepository.findById(it).get().boardCategoryName }
                    .toString()
        }
        if (type == "reply") {
            boardDto.boardTitle = "RE : " + boardDto.boardTitle
            boardDto.boardContents = ""
            boardDto.createDt = null
            boardDto.createUser = null
        }
        return boardDto
    }

    /**
     * ????????? ?????? ?????? ??????.
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    fun getBoardArticleDetail(boardAdminId: String): BoardDto {
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        return BoardDto(
            boardAdminId = boardAdminEntity.boardAdminId,
            boardAdminTitle = boardAdminEntity.boardAdminTitle,
            boardAdminDesc = boardAdminEntity.boardAdminDesc,
            boardAdminSort = boardAdminEntity.boardAdminSort,
            boardUseYn = boardAdminEntity.boardUseYn,
            replyYn = boardAdminEntity.replyYn,
            commentYn = boardAdminEntity.commentYn,
            categoryYn = boardAdminEntity.categoryYn,
            attachYn = boardAdminEntity.attachYn,
            attachFileSize = boardAdminEntity.attachFileSize
        )
    }

    /**
     * ????????? ??????.
     *
     * @param boardId
     */
    @Transactional
    fun deleteBoardArticle(boardId: String): ZResponse {
        val boardDto = boardRepository.findById(boardId).get()
        boardDto.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        val boardReadCount = boardReadRepository.findById(boardId)
        if (!boardReadCount.isEmpty) {
            boardReadRepository.deleteById(boardId)
        }

        val boardFile = aliceResourceProvider.getFiles(boardId, "")
        if (boardFile.isNotEmpty()) {
            aliceResourceProvider.deleteByOwnId(boardId)
        }
        boardRepository.deleteById(boardId)
        return ZResponse()
    }

    /**
     * ?????? ??????.
     *
     * @param boardCommentId
     */
    @Transactional
    fun deleteBoardArticleComment(boardCommentId: String): ZResponse {
        val boardEntity = boardCommentRepository.findById(boardCommentId).get()
        boardEntity.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        boardCommentRepository.deleteById(boardCommentId)
        return ZResponse()
    }

    /**
     * ????????? ?????? ??????.
     *
     * @param boardId
     * @return List<BoardCommentDto>
     */
    fun getBoardArticleCommentList(boardId: String): List<BoardArticleCommentDto> {
        val boardCommentDtoList = mutableListOf<BoardArticleCommentDto>()
        boardCommentRepository.findByBoardIdOrderByCreateDtDesc(boardId).forEach { PortalBoardCommentEntity ->
            val avatarPath = PortalBoardCommentEntity.createUser?.let { userDetailsService.makeAvatarPath(it) }
            boardCommentDtoList.add(
                BoardArticleCommentDto(
                    boardCommentId = PortalBoardCommentEntity.boardCommentId,
                    boardId = PortalBoardCommentEntity.commentBoard.boardId,
                    commentBoard = PortalBoardCommentEntity.commentBoard,
                    boardCommentContents = PortalBoardCommentEntity.boardCommentContents,
                    createDt = PortalBoardCommentEntity.createDt,
                    createUser = PortalBoardCommentEntity.createUser,
                    updateDt = PortalBoardCommentEntity.updateDt,
                    updateUser = PortalBoardCommentEntity.updateUser,
                    avatarPath = avatarPath
                )
            )
        }
        return boardCommentDtoList
    }

    /**
     * ????????? ?????? ??????
     */
    @Transactional
    fun saveBoardArticleReply(boardArticleSaveDto: BoardArticleSaveDto): ZResponse {
        val oldBoardEntity = boardRepository.findById(boardArticleSaveDto.boardId).orElse(null)
        val portalBoardEntity = PortalBoardEntity(
            boardId = "",
            boardAdmin = oldBoardEntity.boardAdmin,
            boardCategoryId = boardArticleSaveDto.boardCategoryId,
            boardSeq = boardRepository.findMaxBoardSeq(oldBoardEntity.boardAdmin.boardAdminId) + 1,
            boardGroupId = oldBoardEntity.boardGroupId,
            boardLevelId = oldBoardEntity.boardLevelId + 1,
            boardOrderSeq = oldBoardEntity.boardOrderSeq + 1,
            boardTitle = boardArticleSaveDto.boardTitle,
            boardContents = boardArticleSaveDto.boardContents
        )
        val savedPortalBoardEntity = boardRepository.save(portalBoardEntity)
        boardRepository.updateBoardOrderSeq(
            savedPortalBoardEntity.boardAdmin.boardAdminId, savedPortalBoardEntity.boardGroupId,
            savedPortalBoardEntity.boardOrderSeq, savedPortalBoardEntity.boardSeq
        )
        aliceResourceProvider.setUploadFileLoc(
            AliceFileDto(
                ownId = savedPortalBoardEntity.boardId,
                fileSeq = boardArticleSaveDto.fileSeqList,
                delFileSeq = boardArticleSaveDto.delFileSeqList
            )
        )
        return ZResponse()
    }

    /**
     * ????????? ???????????? ??????.
     *
     * @param boardAdminId
     * @return List<BoardCommentDto>
     */
    fun getBoardArticleCategoryList(boardAdminId: String): List<BoardCategoryDto> {
        val boardCategoryDtoList = mutableListOf<BoardCategoryDto>()
        boardCategoryRepository.findByCategoryList(boardAdminId)
            .forEach { PortalBoardCategoryEntity ->
                boardCategoryDtoList.add(
                    BoardCategoryDto(
                        boardCategoryId = PortalBoardCategoryEntity.boardCategoryId,
                        boardAdmin = PortalBoardCategoryEntity.boardAdmin,
                        boardCategoryName = PortalBoardCategoryEntity.boardCategoryName,
                        boardCategorySort = PortalBoardCategoryEntity.boardCategorySort
                    )
                )
            }
        return boardCategoryDtoList
    }
}
