package co.brainz.itsm.board.controller

import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.dto.BoardViewDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.board.service.BoardService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import java.time.LocalDateTime

@Controller
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService) {

    private val boardSearchPage: String = "board/boardSearch"
    private val boardListPage: String = "board/boardList"
    private val boardEditPage: String = "board/boardEdit"
    private val boardViewPage: String = "board/boardView"
    private val boardCommentListPage: String = "board/boardCommentList"

    /**
     * 게시판 리스트 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/search")
    fun getBoardSearch(model: Model): String {
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        model.addAttribute("fromDt", LocalDateTime.now().minusMonths(1))
        model.addAttribute("toDt", LocalDateTime.now())
        return boardSearchPage
    }

    /**
     * 게시판 조회조건 포함 리스트 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/search/param")
    fun getBoardSearchParam(boardSearchDto: BoardSearchDto, model: Model): String {
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        model.addAttribute("fromDt", LocalDateTime.now().minusMonths(1))
        model.addAttribute("toDt", LocalDateTime.now())
        model.addAttribute("boardAdminId", boardSearchDto.boardAdminId)
        return boardSearchPage
    }

    /**
     * 게시판 리스트 화면.
     *
     * @param boardSearchDto
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getBoardList(boardSearchDto: BoardSearchDto, model: Model): String {
        model.addAttribute("boardList", boardService.getBoardList(boardSearchDto))
        return boardListPage
    }

    /**
     * 게시판 상세 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/{boardId}/view")
    fun getBoardView(@PathVariable boardId: String, model: Model): String {
        val boardDtoInfo: BoardViewDto = boardService.getBoard(boardId, "view")
        model.addAttribute("boardInfo", boardDtoInfo)
        model.addAttribute("boardAdminInfo", boardDtoInfo.boardAdmin)
        return boardViewPage
    }

    /**
     * 게시판 신규 등록 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("{boardAdminId}/new")
    fun getBoardNew(@PathVariable boardAdminId: String, model: Model): String {
        val boardAdminInfo: BoardAdminDto = boardService.getBoardAdmin(boardAdminId)
        if (boardAdminInfo.categoryYn) {
            model.addAttribute("boardCategoryInfo", boardService.getBoardCategoryList(boardAdminInfo.boardAdminId))
        }
        model.addAttribute("boardAdminInfo", boardAdminInfo)
        model.addAttribute("replyYn", false)
        return boardEditPage
    }

    /**
     * 게시판 편집 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/{boardId}/edit")
    fun getBoardEdit(@PathVariable boardId: String, model: Model): String {
        val boardDtoInfo: BoardViewDto = boardService.getBoard(boardId, "edit")
        if (boardDtoInfo.boardAdmin.categoryYn) {
            model.addAttribute("boardCategoryInfo", boardDtoInfo.boardAdmin.category)
        }
        model.addAttribute("boardAdminInfo", boardDtoInfo.boardAdmin)
        model.addAttribute("boardInfo", boardDtoInfo)
        model.addAttribute("replyYn", false)
        return boardEditPage
    }

    /**
     * 게시판 댓글 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/{boardId}/comments/list")
    fun getBoardCommentList(@PathVariable boardId: String, model: Model): String {
        model.addAttribute("boardCommentList", boardService.getBoardCommentList(boardId))
        return boardCommentListPage
    }

    /**
     * 게시판 답글 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/{boardId}/replay/edit")
    fun getBoardReplayEdit(@PathVariable boardId: String, model: Model): String {
        val boardDtoInfo: BoardViewDto = boardService.getBoard(boardId, "reply")
        if (boardDtoInfo.boardAdmin.categoryYn) {
            model.addAttribute(
                "boardCategoryInfo",
                boardService.getBoardCategoryList(boardDtoInfo.boardAdmin.boardAdminId)
            )
        }
        model.addAttribute("boardAdminInfo", boardDtoInfo.boardAdmin)
        model.addAttribute("boardInfo", boardDtoInfo)
        model.addAttribute("replyYn", true)
        return boardEditPage
    }
}
