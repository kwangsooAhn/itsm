/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.controller

import co.brainz.itsm.board.dto.BoardCommentDto
import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.dto.BoardSaveDto
import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.service.BoardService
import co.brainz.itsm.boardAdmin.dto.BoardAdminDetailDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminListDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminSearchDto
import co.brainz.itsm.boardAdmin.service.BoardAdminService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/boards")
class BoardRestController(private val boardService: BoardService, private val boardAdminService: BoardAdminService) {

    /**
     * [BoardAdminSearchDto]를 받아서 게시판 관리 리스트에 [List<BoardAdminListDto>]로 반환한다.
     *
     */
    @GetMapping("")
    fun getBoardList(boardAdminSearchDto: BoardAdminSearchDto): List<BoardAdminListDto> {
        return boardAdminService.getBoardAdminList(boardAdminSearchDto)
    }

    /**
     * [boardAdminId]를 받아서 게시판 관리 정보를 [BoardAdminDetailDto]로 반환한다.
     *
     */
    @GetMapping("/{boardAdminId}/view")
    fun getBoardView(@PathVariable boardAdminId: String): BoardAdminDetailDto {
        val boardAdminDetail = boardAdminService.getBoardAdmin(boardAdminId)
        return BoardAdminDetailDto(
            boardAdminId = boardAdminDetail.boardAdminId,
            boardAdminTitle = boardAdminDetail.boardAdminTitle,
            boardAdminDesc = boardAdminDetail.boardAdminDesc,
            boardAdminSort = boardAdminDetail.boardAdminSort,
            boardUseYn = boardAdminDetail.boardUseYn,
            replyYn = boardAdminDetail.replyYn,
            commentYn = boardAdminDetail.commentYn,
            categoryYn = boardAdminDetail.categoryYn,
            attachYn = boardAdminDetail.attachYn,
            attachFileSize = boardAdminDetail.attachFileSize,
            boardBoardCount = boardAdminDetail.boardBoardCount,
            categoryInfo = boardAdminService.getBoardCategoryDetailList(boardAdminId),
            createDt = boardAdminDetail.createDt,
            createUserName = boardAdminDetail.createUser?.userName
        )
    }

    /**
     * 게시판 관리 신규 등록.
     *
     * @param boardAdminDto
     */
    @PostMapping("")
    fun createBoard(@RequestBody boardAdminDto: BoardAdminDto) {
        boardAdminService.saveBoardAdmin(boardAdminDto)
    }

    /**
     * 게시판 관리 수정.
     *
     * @param boardAdminDto
     */
    @PutMapping("")
    fun updateBoard(@RequestBody boardAdminDto: BoardAdminDto) {
        boardAdminService.saveBoardAdmin(boardAdminDto)
    }

    /**
     * 게시판 관리 삭제.
     *
     * @param boardAdminId
     */
    @DeleteMapping("/{boardAdminId}")
    fun deleteBoard(@PathVariable boardAdminId: String) {
        boardAdminService.deleteBoardAdmin(boardAdminId)
    }

    /**
     * [BoardSearchDto]를 받아서 게시판 추가할 데이터를 데이터 반환 [List<BoardRestDto>]
     */
    @GetMapping("/articles")
    fun getBoardArticleList(boardSearchDto: BoardSearchDto): List<BoardListDto> {
        return boardService.getBoardList(boardSearchDto)
    }

    /**
     * 게시판 신규 등록.
     *
     * @param boardSaveDto
     */
    @PostMapping("/articles")
    fun createBoardArticle(@RequestBody boardSaveDto: BoardSaveDto) {
        boardService.saveBoard(boardSaveDto)
    }

    /**
     * 게시판 수정.
     *
     * @param boardSaveDto
     */
    @PutMapping("/articles")
    fun updateBoardArticle(@RequestBody boardSaveDto: BoardSaveDto) {
        boardService.saveBoard(boardSaveDto)
    }

    /**
     * 게시판 삭제.
     *
     * @param boardId
     */
    @DeleteMapping("/articles/{boardId}")
    fun deleteBoardArticle(@PathVariable boardId: String) {
        boardService.deleteBoard(boardId)
    }

    /**
     * 게시판 댓글 등록.
     *
     * @param boardCommentDto
     */
    @PostMapping("/articles/comments")
    fun createBoardArticleComment(@RequestBody boardCommentDto: BoardCommentDto) {
        boardService.saveBoardComment(boardCommentDto)
    }

    /**
     * 게시판 댓글 수정.
     *
     * @param boardCommentDto
     */
    @PutMapping("/articles/comments")
    fun updateBoardArticle(@RequestBody boardCommentDto: BoardCommentDto) {
        boardService.saveBoardComment(boardCommentDto)
    }

    /**
     * 게시판 댓글 삭제.
     *
     * @param commentId
     */
    @DeleteMapping("/articles/comments/{commentId}")
    fun deleteBoardArticleComment(@PathVariable commentId: String) {
        boardService.deleteBoardComment(commentId)
    }

    /**
     * 게시판 답글 등록.
     *
     * @param boardSaveDto
     */
    @PostMapping("/articles/reply")
    fun createBoardArticleReply(@RequestBody boardSaveDto: BoardSaveDto) {
        boardService.saveBoardReply(boardSaveDto)
    }
}
