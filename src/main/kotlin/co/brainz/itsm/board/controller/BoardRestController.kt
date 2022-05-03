/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.board.dto.BoardArticleCommentDto
import co.brainz.itsm.board.dto.BoardArticleSaveDto
import co.brainz.itsm.board.dto.BoardDetailDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.service.BoardArticleService
import co.brainz.itsm.board.service.BoardService
import org.springframework.http.ResponseEntity
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
class BoardRestController(
    private val boardService: BoardService,
    private val boardArticleService: BoardArticleService
) {

    /**
     * [boardAdminId]를 받아서 게시판 관리 정보를 [BoardDetailDto]로 반환한다.
     *
     */
    @GetMapping("/{boardAdminId}/view")
    fun getBoardView(@PathVariable boardAdminId: String): ResponseEntity<ZResponse> {
        val boardAdminDetail = boardService.getBoardDetail(boardAdminId)
        return ZAliceResponse.response(
            ZResponse(
                data = BoardDetailDto(
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
                    categoryInfo = boardService.getBoardCategoryDetailList(boardAdminId),
                    createDt = boardAdminDetail.createDt,
                    createUserName = boardAdminDetail.createUser?.userName
                )
            )
        )
    }

    /**
     * 게시판 관리 신규 등록.
     *
     * @param boardDto
     */
    @PostMapping("")
    fun createBoard(@RequestBody boardDto: BoardDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardService.saveBoard(boardDto))
    }

    /**
     * 게시판 관리 수정.
     *
     * @param boardDto
     */
    @PutMapping("")
    fun updateBoard(@RequestBody boardDto: BoardDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardService.saveBoard(boardDto))
    }

    /**
     * 게시판 관리 삭제.
     *
     * @param boardAdminId
     */
    @DeleteMapping("/{boardAdminId}")
    fun deleteBoard(@PathVariable boardAdminId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardService.deleteBoard(boardAdminId))
    }

    /**
     * 게시판 신규 등록.
     *
     * @param boardArticleSaveDto
     */
    @PostMapping("/articles")
    fun createBoardArticle(@RequestBody boardArticleSaveDto: BoardArticleSaveDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.saveBoardArticle(boardArticleSaveDto))
    }

    /**
     * 게시판 수정.
     *
     * @param boardArticleSaveDto
     */
    @PutMapping("/articles")
    fun updateBoardArticle(@RequestBody boardArticleSaveDto: BoardArticleSaveDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.saveBoardArticle(boardArticleSaveDto))
    }

    /**
     * 게시판 삭제.
     *
     * @param boardId
     */
    @DeleteMapping("/articles/{boardId}")
    fun deleteBoardArticle(@PathVariable boardId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.deleteBoardArticle(boardId))
    }

    /**
     * 게시판 댓글 등록.
     *
     * @param boardArticleCommentDto
     */
    @PostMapping("/articles/comments")
    fun createBoardArticleComment(@RequestBody boardArticleCommentDto: BoardArticleCommentDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.saveBoardArticleComment(boardArticleCommentDto))
    }

    /**
     * 게시판 댓글 수정.
     *
     * @param boardArticleCommentDto
     */
    @PutMapping("/articles/comments")
    fun updateBoardArticleComment(@RequestBody boardArticleCommentDto: BoardArticleCommentDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.saveBoardArticleComment(boardArticleCommentDto))
    }

    /**
     * 게시판 댓글 삭제.
     *
     * @param commentId
     */
    @DeleteMapping("/articles/comments/{commentId}")
    fun deleteBoardArticleComment(@PathVariable commentId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.deleteBoardArticleComment(commentId))
    }

    /**
     * 게시판 답글 등록.
     *
     * @param boardArticleSaveDto
     */
    @PostMapping("/articles/reply")
    fun createBoardArticleReply(@RequestBody boardArticleSaveDto: BoardArticleSaveDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(boardArticleService.saveBoardArticleReply(boardArticleSaveDto))
    }
}
