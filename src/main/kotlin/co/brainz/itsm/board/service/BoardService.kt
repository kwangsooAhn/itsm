package co.brainz.itsm.board.service

import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.board.dto.BoardCommentDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardSaveDto
import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.dto.BoardViewDto
import co.brainz.itsm.board.entity.PortalBoardCommentEntity
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.PortalBoardReadEntity
import co.brainz.itsm.board.repository.BoardCommentRepository
import co.brainz.itsm.board.repository.BoardReadRepository
import co.brainz.itsm.board.repository.BoardRepository
import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDto
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.boardAdmin.repository.BoardAdminRepository
import co.brainz.itsm.boardAdmin.repository.BoardCategoryRepository
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
    private val aliceFileService: AliceFileService
) {

    /**
     * 게시판 관리 목록
     *
     * @return MutableList<PortalBoardAdminEntity>
     */
    fun getBoardAdminList(): MutableList<PortalBoardAdminEntity>? {
        return if (boardAdminRepository.count() > 0) {
            boardAdminRepository.findAllByBoardUseYnTrueOrderByBoardAdminSortAsc()
        } else null
    }

    /**
     * 게시판 목록 조회.
     *
     * @param boardSearchDto
     * @return List<boardDto>
     */
    fun getBoardList(boardSearchDto: BoardSearchDto): List<BoardDto> {
        val boardDtoList = mutableListOf<BoardDto>()
        val fromDt = LocalDateTime.parse(boardSearchDto.fromDt, DateTimeFormatter.ISO_DATE_TIME)
        val toDt = LocalDateTime.parse(boardSearchDto.toDt, DateTimeFormatter.ISO_DATE_TIME)

        boardRepository.findByBoardList(boardSearchDto.boardAdminId, boardSearchDto.search, fromDt, toDt)
            .forEach { PortalBoardEntity ->
                var categoryName = ""
                var readCount = 0L

                if (boardReadRepository.countByBoardId(PortalBoardEntity.boardId) > 0) {
                    readCount = boardReadRepository.findByBoardId(PortalBoardEntity.boardId)[0].boardReadCount!!
                }

                if (PortalBoardEntity.boardAdmin.categoryYn) {
                    if (PortalBoardEntity.boardCategoryId != "") {
                        categoryName = PortalBoardEntity.boardCategoryId?.let {
                            boardCategoryRepository.findById(it).get().boardCategoryName
                        }.toString()
                    }
                }
                boardDtoList.add(
                    BoardDto(
                        boardId = PortalBoardEntity.boardId,
                        boardAdminId = PortalBoardEntity.boardAdmin.boardAdminId,
                        boardCategoryName = categoryName,
                        boardSeq = PortalBoardEntity.boardSeq,
                        boardTitle = PortalBoardEntity.boardTitle,
                        boardContents = PortalBoardEntity.boardContents,
                        boardGroupId = PortalBoardEntity.boardGroupId,
                        boardLevelId = PortalBoardEntity.boardLevelId,
                        boardOrderSeq = PortalBoardEntity.boardOrderSeq,
                        replyCount = PortalBoardEntity.commentBoard?.count()?.toLong(),
                        readCount = readCount,
                        createDt = PortalBoardEntity.createDt,
                        createUser = PortalBoardEntity.createUser
                    )
                )
            }
        return boardDtoList
    }

    /**
     * 게시판 저장.
     *
     */
    @Transactional
    fun saveBoard(boardSaveDto: BoardSaveDto) {
        val boardAdminId = boardSaveDto.boardAdminId
        val boardCount = boardRepository.countByBoardAdminId(boardAdminId)
        var boardSeq = 0L
        if (boardCount > 0) boardSeq = boardRepository.findMaxBoardSeq(boardAdminId)

        val updatePortalBoardEntity = boardRepository.findById(boardSaveDto.boardId).orElse(null)
        val portalBoardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        val portalBoardEntity = PortalBoardEntity(
            boardId = boardSaveDto.boardId,
            boardAdmin = portalBoardAdminEntity,
            boardCategoryId = boardSaveDto.boardCategoryId,
            boardSeq = boardSeq + 1,
            boardGroupId = boardSeq + 1,
            boardLevelId = updatePortalBoardEntity?.boardLevelId ?: 0,
            boardOrderSeq = updatePortalBoardEntity?.boardOrderSeq ?: 0,
            boardTitle = boardSaveDto.boardTitle,
            boardContents = boardSaveDto.boardContents
        )
        val savedPortalBoardEntity = boardRepository.save(portalBoardEntity)
        aliceFileService.upload(AliceFileDto(savedPortalBoardEntity.boardId, boardSaveDto.fileSeqList))
    }

    /**
     * 게시판 댓글 저장.
     *
     * @param boardCommentDto
     */
    @Transactional
    fun saveBoardComment(boardCommentDto: BoardCommentDto) {
        val boardPortalBoardEntity = boardRepository.findById(boardCommentDto.boardId).orElse(null)
        val portalBoardCommentEntity = PortalBoardCommentEntity(
            boardCommentId = boardCommentDto.boardCommentId,
            commentBoard = boardPortalBoardEntity,
            boardCommentContents = boardCommentDto.boardCommentContents
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
    fun getBoard(boardId: String, type: String): BoardViewDto {
        val boardReadEntity = boardReadRepository.findById(boardId).orElse(PortalBoardReadEntity())
        if (type == "view") {
            boardReadEntity.boardId = boardId
            boardReadEntity.boardReadCount = boardReadEntity.boardReadCount?.plus(1)
            boardReadRepository.save(boardReadEntity)
        }

        val boardEntity = boardRepository.findById(boardId).orElse(null)
        var categoryName = ""
        if (boardEntity.boardCategoryId != "") {
            categoryName =
                boardEntity.boardCategoryId?.let { boardCategoryRepository.findById(it).get().boardCategoryName }
                    .toString()
        }

        return BoardViewDto(
            boardId = boardEntity.boardId,
            boardAdmin = boardEntity.boardAdmin,
            boardCategoryId = boardEntity.boardCategoryId,
            boardCategoryName = categoryName,
            boardTitle = if (type == "reply") "RE : " + boardEntity.boardTitle else boardEntity.boardTitle,
            boardContents = if (type == "reply") "" else boardEntity.boardContents,
            createDt = if (type == "reply") null else boardEntity.createDt,
            createUser = if (type == "reply") null else boardEntity.createUser,
            updateDt = if (type == "reply") null else boardEntity.updateDt,
            updateUser = if (type == "reply") null else boardEntity.updateUser
        )
    }

    /**
     * 게시판 관리 상세 조회.
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    fun getBoardAdmin(boardAdminId: String): BoardAdminDto {
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        return BoardAdminDto(
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
    fun deleteBoard(boardId: String) {
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
    fun deleteBoardComment(boardCommentId: String) {
        boardCommentRepository.deleteById(boardCommentId)
    }

    /**
     * 게시판 댓글 조회.
     *
     * @param boardId
     * @return List<BoardCommentDto>
     */
    fun getBoardCommentList(boardId: String): List<BoardCommentDto> {
        val boardCommentDtoList = mutableListOf<BoardCommentDto>()
        boardCommentRepository.findByBoardIdOrderByCreateDtDesc(boardId).forEach { PortalBoardCommentEntity ->
            boardCommentDtoList.add(
                BoardCommentDto(
                    boardCommentId = PortalBoardCommentEntity.boardCommentId,
                    boardId = PortalBoardCommentEntity.commentBoard.boardId,
                    commentBoard = PortalBoardCommentEntity.commentBoard,
                    boardCommentContents = PortalBoardCommentEntity.boardCommentContents,
                    createDt = PortalBoardCommentEntity.createDt,
                    createUser = PortalBoardCommentEntity.createUser,
                    updateDt = PortalBoardCommentEntity.updateDt,
                    updateUser = PortalBoardCommentEntity.updateUser
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
    fun getBoardCategoryList(boardAdminId: String): List<BoardCategoryDto> {
        val boardCategoryDtoList = mutableListOf<BoardCategoryDto>()
        boardCategoryRepository.findByBoardAdminOrderByBoardCategorySortAsc(boardAdminId)
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
    fun saveBoardReply(boardSaveDto: BoardSaveDto) {
        val oldBoardEntity = boardRepository.findById(boardSaveDto.boardId).orElse(null)
        val portalBoardEntity = PortalBoardEntity(
            boardId = "",
            boardAdmin = oldBoardEntity.boardAdmin,
            boardCategoryId = boardSaveDto.boardCategoryId,
            boardSeq = boardRepository.findMaxBoardSeq(oldBoardEntity.boardAdmin.boardAdminId) + 1,
            boardGroupId = oldBoardEntity.boardGroupId,
            boardLevelId = oldBoardEntity.boardLevelId + 1,
            boardOrderSeq = oldBoardEntity.boardOrderSeq + 1,
            boardTitle = boardSaveDto.boardTitle,
            boardContents = boardSaveDto.boardContents
        )
        val savedPortalBoardEntity = boardRepository.save(portalBoardEntity)
        boardRepository.updateBoardOrderSeq(
            savedPortalBoardEntity.boardAdmin.boardAdminId, savedPortalBoardEntity.boardGroupId,
            savedPortalBoardEntity.boardOrderSeq, savedPortalBoardEntity.boardSeq
        )
        aliceFileService.upload(AliceFileDto(savedPortalBoardEntity.boardId, boardSaveDto.fileSeqList))
    }
}
