package co.brainz.itsm.board.controller

import co.brainz.itsm.board.dto.BoardCommentDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.service.BoardService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/boards")
class boardRestController(private val boardService: BoardService) {

    /**
     * 게시판 신규 등록.
     *
     * @param BoardDto
     */
    @PostMapping("")
    fun createBoard(@RequestBody BoardDto: BoardDto) {
        boardService.saveBoard(BoardDto)
    }

    /**
     * 게시판 수정.
     *
     * @param BoardDto
     */
    @PutMapping("")
    fun updateBoard(@RequestBody BoardDto: BoardDto) {
        boardService.saveBoard(BoardDto)
    }

    /**
     * 게시판 삭제.
     *
     * @param boardId
     */
    @DeleteMapping("/{boardId}")
    fun deleteBoard(@PathVariable boardId: String) {
        boardService.deleteBoard(boardId)
    }

    /**
     * 게시판 댓글 등록.
     *
     * @param boardCommentDto
     */
    @PostMapping("/comments")
    fun createBoardComment(@RequestBody boardCommentDto: BoardCommentDto) {
        boardService.saveBoardComment(boardCommentDto)
    }

    /**
     * 게시판 댓글 수정.
     *
     * @param boardCommentDto
     */
    @PutMapping("/comments")
    fun updateBoards(@RequestBody boardCommentDto: BoardCommentDto) {
        boardService.saveBoardComment(boardCommentDto)
    }

    /**
     * 게시판 댓글 삭제.
     *
     * @param commentId
     */
    @DeleteMapping("/comments/{commentId}")
    fun deleteBoardComment(@PathVariable commentId: String) {
        boardService.deleteBoardComment(commentId)
    }
}