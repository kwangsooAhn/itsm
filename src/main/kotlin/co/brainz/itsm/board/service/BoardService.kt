package co.brainz.itsm.board.service

import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.dto.BoardCommentDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.framework.fileTransaction.dto.AliceFileDto
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.itsm.board.entity.PortalBoardCommentEntity
import co.brainz.itsm.board.entity.PortalBoardEntity
import co.brainz.itsm.board.entity.PortalBoardReadEntity
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.board.repository.BoardCommentRepository
import co.brainz.itsm.board.repository.BoardReadRepository
import co.brainz.itsm.board.repository.BoardRepository
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDto
import co.brainz.itsm.boardAdmin.repository.BoardAdminRepository
import co.brainz.itsm.boardAdmin.repository.BoardCategoryRepository
import co.brainz.itsm.utility.ConvertParam
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class BoardService(private val boardRepository: BoardRepository,
                   private val boardReadRepository : BoardReadRepository,
                   private val boardCommentRepository: BoardCommentRepository,
                   private val boardAdminRepository: BoardAdminRepository,
                   private val boardCategoryRepository: BoardCategoryRepository,
                   private val aliceFileService: AliceFileService,
                   private val convertParam: ConvertParam) {

    /**
     * 게시판 관리 목록
     *
     * @return MutableList<PortalBoardAdminEntity>
     */
    fun getBoardAdminList(): MutableList<PortalBoardAdminEntity> {
        return boardAdminRepository.findAllByBoardUseYnTrueOrderByBoardAdminSortAsc()
    }

    /**
     * 게시판 목록 조회.
     *
     * @param boardSearchDto
     * @return List<boardDto>
     */
    fun getBoardList(boardSearchDto: BoardSearchDto, boardAdminDto : BoardAdminDto): List<BoardDto> {
        val boardDtoList = mutableListOf<BoardDto>()
        val fromDt = convertParam.convertToSearchLocalDateTime(boardSearchDto.fromDt, "fromDt")
        val toDt = convertParam.convertToSearchLocalDateTime(boardSearchDto.toDt, "toDt")

        boardRepository.findByBoardList(boardSearchDto.boardAdminId, boardSearchDto.search, fromDt, toDt).forEach { PortalBoardEntity ->
            var replyCount = 0L
            var categoryName = ""
            var readCount = 0L

            if (boardReadRepository.countByBoardId(PortalBoardEntity.boardId) > 0 ) {
                readCount = boardReadRepository.findByBoardId(PortalBoardEntity.boardId)[0].boardReadCount!!
            }
            if (boardAdminDto.categoryYn) {
                if (PortalBoardEntity.boardCategoryId != "") {
                    categoryName = PortalBoardEntity.boardCategoryId?.let { boardCategoryRepository.findById(it).get().boardCategoryName }.toString()
                }
            }
            if (boardAdminDto.commentYn) {
                replyCount = boardCommentRepository.countByBoardId(PortalBoardEntity.boardId)
            }
            boardDtoList.add (
                BoardDto(
                    boardId = PortalBoardEntity.boardId,
                    boardAdminId = PortalBoardEntity.boardAdminId,
                    boardCategoryName = categoryName,
                    boardSeq = PortalBoardEntity.boardSeq,
                    boardTitle = PortalBoardEntity.boardTitle,
                    boardConents = PortalBoardEntity.boardConents,
                    boardGroupNo =PortalBoardEntity.boardGroupNo,
                    boardLevelNo = PortalBoardEntity.boardLevelNo,
                    boardOrderSeq = PortalBoardEntity.boardOrderSeq,
                    replyCount = replyCount,
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
     * @param boardDto
     */
    @Transactional
    fun saveBoard(boardDto: BoardDto) {
        val boardCount = boardRepository.countByBoardAdminId(boardDto.boardAdminId)
        var boardSeq = 0L
        if (boardCount > 0) {
            boardSeq = boardRepository.findMaxBoardSeq(boardDto.boardAdminId)
        }

        val updatePortalBoardEntity = boardRepository.findById(boardDto.boardId).orElse(null)

        val portalBoardEntity = PortalBoardEntity (
            boardId = boardDto.boardId,
            boardAdminId = boardDto.boardAdminId,
            boardCategoryId = boardDto.boardCategoryId,
            boardSeq = boardSeq + 1,
            boardGroupNo = boardSeq + 1,
            boardLevelNo = updatePortalBoardEntity?.boardLevelNo ?: 0,
            boardOrderSeq = updatePortalBoardEntity?.boardOrderSeq ?: 0,
            boardTitle = boardDto.boardTitle,
            boardConents = boardDto.boardConents
        )
        val savedPortalBoardEntity = boardRepository.save(portalBoardEntity)
        aliceFileService.upload(AliceFileDto(savedPortalBoardEntity.boardId, boardDto.fileSeqList))
    }

    /**
     * 게시판 댓글 저장.
     *
     * @param boardCommentDto
     */
    @Transactional
    fun saveBoardComment(boardCommentDto: BoardCommentDto) {
        val portalBoardCommentEntity = PortalBoardCommentEntity (
            boardCommentId = boardCommentDto.boardCommentId,
            boardId = boardCommentDto.boardId,
            boardCommentConents =  boardCommentDto.boardCommentConents
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
    fun getBoard(boardId: String, type: String): BoardDto {
        val replyCount = boardCommentRepository.countByBoardId(boardId)
        var boardReadEntity = boardReadRepository.findById(boardId).orElse(PortalBoardReadEntity())
        if (type == "view") {
            boardReadEntity.boardId = boardId
            boardReadEntity.boardReadCount = boardReadEntity.boardReadCount?.plus(1)
            boardReadEntity = boardReadRepository.save(boardReadEntity)
        }

        val boardEntity = boardRepository.findById(boardId).orElse(null)
        var categoryName = ""
        if (boardEntity.boardCategoryId != "") {
            categoryName = boardEntity.boardCategoryId?.let { boardCategoryRepository.findById(it).get().boardCategoryName }.toString()
        }

        return BoardDto (
            boardId = boardEntity.boardId,
            boardAdminId = boardEntity.boardAdminId,
            boardCategoryId = boardEntity.boardCategoryId,
            boardCategoryName = categoryName,
            boardSeq = boardEntity.boardSeq,
            boardGroupNo = if (type == "reply") boardEntity.boardSeq else boardEntity.boardGroupNo,
            boardLevelNo = boardEntity.boardLevelNo,
            boardOrderSeq = boardEntity.boardOrderSeq,
            boardTitle = if (type == "reply") "RE : "+boardEntity.boardTitle else boardEntity.boardTitle,
            boardConents = if (type == "reply") "" else boardEntity.boardConents,
            replyCount = replyCount,
            readCount = boardReadEntity.boardReadCount,
            createDt = if (type == "reply") null else boardEntity.createDt,
            createUser = if (type == "reply") null else boardEntity.createUser,
            updateDt =if (type == "reply") null else boardEntity.updateDt,
            updateUser = if (type == "reply") null else boardEntity.updateUser
        )
    }

    /**
     * 게시판 관리 상세 조회.
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    fun getBoardAdmin(boardAdminId: String) : BoardAdminDto {
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        return BoardAdminDto (
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

        val boardComment = boardCommentRepository.findByBoardId(boardId)
        if (boardComment.isNotEmpty()) {
            boardCommentRepository.deleteByBoardId(boardId)
        }

        val boardFile = aliceFileService.getList(boardId,"")
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
            boardCommentDtoList.add (
                BoardCommentDto (
                    boardCommentId = PortalBoardCommentEntity.boardCommentId,
                    boardId = PortalBoardCommentEntity.boardId,
                    boardCommentConents = PortalBoardCommentEntity.boardCommentConents,
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
        boardCategoryRepository.findByBoardAdminIdOrderByBoardCategorySortAsc(boardAdminId).forEach { PortalBoardCategoryEntity ->
            boardCategoryDtoList.add (
                BoardCategoryDto (
                    boardCategoryId = PortalBoardCategoryEntity.boardCategoryId,
                    boardCategoryName = PortalBoardCategoryEntity.boardCategoryName,
                    boardCategorySort = PortalBoardCategoryEntity.boardCategorySort
                )
            )
        }
        return boardCategoryDtoList
    }

    /**
     * 게시판 답글 저장.
     *
     * @param boardDto
     */
    @Transactional
    fun saveBoardReply(boardDto: BoardDto) {
        val oldBoardEntity = boardRepository.findById(boardDto.boardId)
        val portalBoardEntity = PortalBoardEntity (
            boardId = "",
            boardAdminId = boardDto.boardAdminId,
            boardSeq = boardRepository.findMaxBoardSeq(boardDto.boardAdminId) + 1,
            boardGroupNo = oldBoardEntity.get().boardGroupNo,
            boardLevelNo = oldBoardEntity.get().boardLevelNo + 1,
            boardOrderSeq = oldBoardEntity.get().boardOrderSeq + 1,
            boardCategoryId = boardDto.boardCategoryId,
            boardTitle = boardDto.boardTitle,
            boardConents = boardDto.boardConents
        )
        val savedPortalBoardEntity = boardRepository.save(portalBoardEntity)
        boardRepository.updateBoardOrderSeq(savedPortalBoardEntity.boardAdminId, savedPortalBoardEntity.boardGroupNo, savedPortalBoardEntity.boardOrderSeq, savedPortalBoardEntity.boardSeq)
        aliceFileService.upload(AliceFileDto(savedPortalBoardEntity.boardId, boardDto.fileSeqList))
    }
}
