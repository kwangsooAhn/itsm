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
                   private val convertParam: ConvertParam
                   ) {

    /**
     * 게시판 관리 목록
     *
     * @return MutableList<PortalBoardAdminEntity>
     */
    fun getBoardAdminList(): MutableList<PortalBoardAdminEntity> {
        return boardAdminRepository.findAll()
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

            val readCount= boardReadRepository.countByBoardId(PortalBoardEntity.boardId)
            if (boardAdminDto.categoryYn) {
                categoryName = PortalBoardEntity.boardCategoryId?.let { boardCategoryRepository.findById(it).get().boardCategoryName }.toString()
            }
            if (boardAdminDto.commentYn) {
                replyCount = boardCommentRepository.countByBoardId(PortalBoardEntity.boardId)
            }
            boardDtoList.add (
                BoardDto(
                    boardId = PortalBoardEntity.boardId,
                    boardAdminId = PortalBoardEntity.boardAdminId,
                    boardCategoryName = categoryName,
                    boardNo = PortalBoardEntity.boardNo,
                    boardTitle = PortalBoardEntity.boardTitle,
                    boardConents = PortalBoardEntity.boardConents,
                    parentBoardId = PortalBoardEntity.parentBoardId,
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
        var boardNo = 0L
        if (boardCount > 0) {
            boardNo = boardRepository.findMaxBoardNo(boardDto.boardAdminId)
        }

        val portalBoardEntity = PortalBoardEntity (
            boardId = boardDto.boardId,
            boardAdminId = boardDto.boardAdminId,
            boardNo = boardNo + 1,
            boardCategoryId = boardDto.boardCategoryId,
            boardTitle = boardDto.boardTitle,
            boardConents = boardDto.boardConents,
            parentBoardId = boardDto.parentBoardId
        )
        boardRepository.save(portalBoardEntity)
        aliceFileService.upload(AliceFileDto(boardDto.boardId, boardDto.fileSeqList))
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
            boardReadEntity.boardReadCount = boardReadEntity.boardReadCount?.plus(1)
            boardReadEntity = boardReadRepository.save(boardReadEntity)
        }

        val boardEntity = boardRepository.findById(boardId).orElse(null)
        val categoryName = boardEntity.boardCategoryId?.let { boardCategoryRepository.findById(it).get().boardCategoryName }.toString()

        return BoardDto(
            boardId = boardEntity.boardId,
            boardAdminId = boardEntity.boardAdminId,
            boardCategoryId = boardEntity.boardCategoryId,
            boardCategoryName = categoryName,
            boardNo = boardEntity.boardNo,
            boardTitle = boardEntity.boardTitle,
            boardConents = boardEntity.boardConents,
            parentBoardId = boardEntity.parentBoardId,
            replyCount = replyCount,
            readCount = boardReadEntity.boardReadCount,
            createDt = boardEntity.createDt,
            createUser = boardEntity.createUser,
            updateDt = boardEntity.updateDt,
            updateUser = boardEntity.updateUser
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
                answerYn = boardAdminEntity.answerYn,
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
        boardCommentRepository.findByBoardId(boardId).forEach { PortalBoardCommentEntity ->
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

}
