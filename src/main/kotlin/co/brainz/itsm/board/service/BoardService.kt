/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.service

import co.brainz.framework.auth.service.AliceUserDetailsService
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.board.dto.BoardArticleCommentDto
import co.brainz.itsm.board.dto.BoardArticleListDto
import co.brainz.itsm.board.dto.BoardArticleSaveDto
import co.brainz.itsm.board.dto.BoardArticleSearchDto
import co.brainz.itsm.board.dto.BoardArticleViewDto
import co.brainz.itsm.board.dto.BoardCategoryDetailDto
import co.brainz.itsm.board.dto.BoardCategoryDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.board.entity.PortalBoardCategoryEntity
import co.brainz.itsm.board.entity.PortalBoardCommentEntity
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.PortalBoardReadEntity
import co.brainz.itsm.board.repository.BoardAdminRepository
import co.brainz.itsm.board.repository.BoardCategoryRepository
import co.brainz.itsm.board.repository.BoardCommentRepository
import co.brainz.itsm.board.repository.BoardReadRepository
import co.brainz.itsm.board.repository.BoardRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val boardReadRepository: BoardReadRepository,
    private val boardCommentRepository: BoardCommentRepository,
    private val boardAdminRepository: BoardAdminRepository,
    private val boardCategoryRepository: BoardCategoryRepository,
    private val aliceFileService: AliceFileService,
    private val userDetailsService: AliceUserDetailsService
) {

    /**
     * [boardSearchDto]로 받아서 게시판 관리 목록 조회를 [List<BoardAdminListDto>]으로 반환.
     *
     */
    fun getBoardList(boardSearchDto: BoardSearchDto): List<BoardListDto> {
        return boardAdminRepository.findByBoardAdminList(
            boardSearchDto.search,
            boardSearchDto.offset
        )
    }

    /**
     * 게시판 관리 저장.
     *
     * @param boardDto
     */
    @Transactional
    fun saveBoard(boardDto: BoardDto): Boolean {
        val portalBoardAdminEntity = PortalBoardAdminEntity(
                boardAdminId = boardDto.boardAdminId,
                boardAdminTitle = boardDto.boardAdminTitle,
                boardAdminDesc = boardDto.boardAdminDesc,
                boardAdminSort = boardDto.boardAdminSort,
                boardUseYn = boardDto.boardUseYn,
                replyYn = boardDto.replyYn,
                commentYn = boardDto.commentYn,
                categoryYn = boardDto.categoryYn,
                attachYn = boardDto.attachYn,
                attachFileSize = boardDto.attachFileSize
        )
        var preBoardEntity: PortalBoardAdminEntity? = boardAdminRepository.findByBoardAdminId(portalBoardAdminEntity.boardAdminId)
                ?: null
        val duplicateData = boardAdminRepository.countByBoardAdminTitle(boardDto.boardAdminTitle!!)
        if (duplicateData > 0 && !boardDto.boardAdminTitle.equals(preBoardEntity?.boardAdminTitle)) {
            return false
        }
        val boardAdmin = boardAdminRepository.save(portalBoardAdminEntity)
        if (boardDto.categoryYn) {
            val categoryList = mutableListOf<PortalBoardCategoryEntity>()
            if (boardDto.boardAdminId.isNotEmpty()) {
                val boardCategoryList = boardCategoryRepository.findByCategoryList(boardDto.boardAdminId)
                val newCategoryList = mutableListOf<BoardCategoryDetailDto>()
                val existsCategoryList = mutableListOf<BoardCategoryDto>()
                for (boardCategoryDetailDto in boardDto.categoryList) {
                    if (boardCategoryDetailDto.boardCategoryId.isEmpty()) { // new
                        newCategoryList.add(boardCategoryDetailDto)
                    } else {
                        for (boardCategoryDto in boardCategoryList) {
                            if (boardCategoryDto.boardCategoryId == boardCategoryDetailDto.boardCategoryId) {
                                existsCategoryList.add(boardCategoryDto)
                            }
                        }
                    }
                }

                val deleteCategoryList = mutableListOf<BoardCategoryDto>()
                for (boardCategoryDto in boardCategoryList) {
                    if (!existsCategoryList.contains(boardCategoryDto)) {
                        deleteCategoryList.add(boardCategoryDto)
                    }
                }

                deleteCategoryList.forEach { category ->
                    this.deleteBoardCategory(category.boardCategoryId)
                }

                newCategoryList.forEach { category ->
                    categoryList.add(
                            PortalBoardCategoryEntity(
                                    boardCategoryId = "",
                                    boardAdmin = boardAdminRepository.getOne(boardDto.boardAdminId),
                                    boardCategoryName = category.boardCategoryName,
                                    boardCategorySort = category.boardCategorySort
                            )
                    )
                }
            } else {
                boardDto.categoryList.forEach { category ->
                    categoryList.add(
                            PortalBoardCategoryEntity(
                                    boardCategoryId = "",
                                    boardAdmin = boardAdmin,
                                    boardCategoryName = category.boardCategoryName,
                                    boardCategorySort = category.boardCategorySort
                            )
                    )
                }
            }
            if (categoryList.isNotEmpty()) {
                boardCategoryRepository.saveAll(categoryList)
            }
        }
        return true
    }

    /**
     * 게시판 관리 상세 조회
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    @Transactional
    fun getBoardDetail(boardAdminId: String): BoardDto {
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        var enabled = true
        if (boardAdminEntity.board?.count() ?: 0 > 0) {
            enabled = false
        }
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
            attachFileSize = boardAdminEntity.attachFileSize,
            enabled = enabled,
            createDt = boardAdminEntity.createDt,
            createUser = boardAdminEntity.createUser,
            updateDt = boardAdminEntity.updateDt,
            updateUser = boardAdminEntity.updateUser,
            categoryList = getBoardCategoryDetailList(boardAdminEntity.boardAdminId)
        )
    }

    /**
     * 게시판 관리 삭제.
     *
     * @param boardAdminId
     */
    @Transactional
    fun deleteBoard(boardAdminId: String) {
        boardAdminRepository.deleteById(boardAdminId)
    }

    /**
     * 카테고리 리스트
     *
     * @param boardAdminId
     * @return List<BoardCategoryDto>
     */
    fun getBoardCategoryList(boardAdminId: String): List<BoardCategoryDto>? {
        val boardCategoryList = boardCategoryRepository.findByCategoryList(boardAdminId)
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        val boardCategoryDtoList = mutableListOf<BoardCategoryDto>()
        for (boardCategory in boardCategoryList) {
            boardCategoryDtoList.add(
                BoardCategoryDto(
                    boardCategoryId = boardCategory.boardCategoryId,
                    boardAdmin = boardAdminEntity,
                    boardAdminId = boardAdminEntity.boardAdminId,
                    boardCategoryName = boardCategory.boardCategoryName,
                    boardCategorySort = boardCategory.boardCategorySort,
                    boardCount = boardCategory.boardCount
                )
            )
        }
        return boardCategoryDtoList
    }

    /**
     * 카테고리 리스트
     *
     * @param boardAdminId
     * @return List<BoardCategoryDetailDto>
     */
    fun getBoardCategoryDetailList(boardAdminId: String): List<BoardCategoryDetailDto> {
        val boardCategoryList = boardCategoryRepository.findByCategoryList(boardAdminId)
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        val boardCategoryDetailDtoList = mutableListOf<BoardCategoryDetailDto>()
        for (boardCategory in boardCategoryList) {
            boardCategoryDetailDtoList.add(
                BoardCategoryDetailDto(
                    boardCategoryId = boardCategory.boardCategoryId,
                    boardAdminId = boardAdminEntity.boardAdminId,
                    boardCategoryName = boardCategory.boardCategoryName,
                    boardCategorySort = boardCategory.boardCategorySort,
                    boardCount = boardCategory.boardCount
                )
            )
        }
        return boardCategoryDetailDtoList
    }

    /**
     * 카테고리 삭제
     *
     * @param boardCategoryId
     */
    @Transactional
    fun deleteBoardCategory(boardCategoryId: String) {
        boardCategoryRepository.deleteById(boardCategoryId)
    }

    /**
     * 게시판 관리 목록 -> 게시판에서 selectbox에서 사용한다.
     *
     * @return MutableList<PortalBoardAdminEntity>
     */
    fun getSelectBoard(): List<BoardListDto>? {
        return boardAdminRepository.findPortalBoardAdmin()
    }

    /**
     * [boardArticleSearchDto]을 받아서 게시판 목록을 [List<BoardRestDto>]으로 반환 한다.
     */
    fun getBoardArticleList(boardArticleSearchDto: BoardArticleSearchDto): List<BoardArticleListDto> {
        val fromDt = LocalDateTime.parse(boardArticleSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(boardArticleSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)
        val offset = boardArticleSearchDto.offset

        return boardRepository.findByBoardList(
            boardArticleSearchDto.boardAdminId,
            boardArticleSearchDto.search,
            fromDt,
            toDt,
            offset
        )
    }

    /**
     * 게시판 저장.
     *
     */
    @Transactional
    fun saveBoardArticle(boardArticleSaveDto: BoardArticleSaveDto) {
        val boardAdminId = boardArticleSaveDto.boardAdminId
        val boardCount = boardRepository.countByBoardAdminId(boardAdminId)
        var boardSeq = 0L
        if (boardCount > 0) boardSeq = boardRepository.findMaxBoardSeq(boardAdminId)

        val updatePortalBoardEntity = boardRepository.findById(boardArticleSaveDto.boardId).orElse(null)
        val portalBoardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
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
        aliceFileService.upload(
            AliceFileDto(
                ownId = savedPortalBoardEntity.boardId,
                fileSeq = boardArticleSaveDto.fileSeqList,
                delFileSeq = boardArticleSaveDto.delFileSeqList
            )
        )
    }

    /**
     * 게시판 댓글 저장.
     *
     * @param boardArticleCommentDto
     */
    @Transactional
    fun saveBoardArticleComment(boardArticleCommentDto: BoardArticleCommentDto) {
        val boardPortalBoardEntity = boardRepository.findById(boardArticleCommentDto.boardId).orElse(null)
        val portalBoardCommentEntity = PortalBoardCommentEntity(
            boardCommentId = boardArticleCommentDto.boardCommentId,
            commentBoard = boardPortalBoardEntity,
            boardCommentContents = boardArticleCommentDto.boardCommentContents
        )
        boardCommentRepository.save(portalBoardCommentEntity)
    }

    /**
     * 게시판 상세 조회.
     *
     * @param boardId
     * @param type
     * @return BoardDto
     */
    @Transactional
    fun getBoardArticle(boardId: String, type: String): BoardArticleViewDto {
        val boardReadEntity = boardReadRepository.findById(boardId).orElse(PortalBoardReadEntity())
        if (type == "view") {
            boardReadEntity.boardId = boardId
            boardReadEntity.boardReadCount = boardReadEntity.boardReadCount?.plus(1)
            boardReadRepository.save(boardReadEntity)
        }

        val boardDto = boardRepository.findByBoardId(boardId)
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
     * 게시판 관리 상세 조회.
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    fun getBoardInfo(boardAdminId: String): BoardDto {
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
     * 게시판 삭제.
     *
     * @param boardId
     */
    @Transactional
    fun deleteBoardArticle(boardId: String) {
        val boardReadCount = boardReadRepository.findById(boardId)
        if (!boardReadCount.isEmpty) {
            boardReadRepository.deleteById(boardId)
        }

        val boardFile = aliceFileService.getList(boardId, "")
        if (boardFile.isEmpty()) {
            aliceFileService.delete(boardId)
        }

        boardRepository.deleteById(boardId)
    }

    /**
     * 댓글 삭제.
     *
     * @param boardCommentId
     */
    @Transactional
    fun deleteBoardArticleComment(boardCommentId: String) {
        boardCommentRepository.deleteById(boardCommentId)
    }

    /**
     * 게시판 댓글 조회.
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
     * 게시판 카테고리 조회.
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

    /**
     * 게시판 답글 저장
     */
    @Transactional
    fun saveBoardArticleReply(boardArticleSaveDto: BoardArticleSaveDto) {
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
        aliceFileService.upload(
            AliceFileDto(
                ownId = savedPortalBoardEntity.boardId,
                fileSeq = boardArticleSaveDto.fileSeqList,
                delFileSeq = boardArticleSaveDto.delFileSeqList
            )
        )
    }
}
