/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.controller

import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.dto.BoardViewDto
import co.brainz.itsm.board.service.BoardService
import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminSearchDto
import co.brainz.itsm.boardAdmin.service.BoardAdminService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/boards")
class BoardController(private val boardService: BoardService, private val boardAdminService: BoardAdminService) {

    private val boardSearchPage: String = "board/boardSearch"
    private val boardListPage: String = "board/boardList"
    private val boardEditPage: String = "board/boardEdit"
    private val boardViewPage: String = "board/boardView"
    private val boardArticlesSearchPage: String = "board/boardArticlesSearch"
    private val boardArticlesListPage: String = "board/boardArticlesList"
    private val boardArticlesEditPage: String = "board/boardArticlesEdit"
    private val boardArticlesViewPage: String = "board/boardArticlesView"
    private val boardArticlesCommentListPage: String = "board/boardArticlesCommentList"

    /**
     * 게시판 관리 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/search")
    fun getBoardSearch(model: Model): String {
        return boardSearchPage
    }

    /**
     *  [BoardAdminSearchDto]를 받아서 게시판 관리 리스트 화면에[String]으로 반환한다.
     *
     */
    @GetMapping("")
    fun getBoardList(boardAdminSearchDto: BoardAdminSearchDto, model: Model): String {
        val result = boardAdminService.getBoardAdminList(boardAdminSearchDto)
        model.addAttribute("boardAdminList", result)
        model.addAttribute("boardAdminCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return boardListPage
    }

    /**
     * 게시판 관리 신규 등록 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getBoardNew(model: Model): String {
        return boardEditPage
    }

    /**
     * 게시판 관리 상세 조회 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("/{boardAdminId}/view")
    fun getBoardView(@PathVariable boardAdminId: String, model: Model): String {
        model.addAttribute("boardAdmin", boardAdminService.getBoardAdmin(boardAdminId))
        return boardViewPage
    }

    /**
     * 게시판 관리 편집 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("/{boardAdminId}/edit")
    fun getBoardEdit(@PathVariable boardAdminId: String, model: Model): String {
        model.addAttribute("boardAdmin", boardAdminService.getBoardAdmin(boardAdminId))
        return boardEditPage
    }

    /**
     * 게시판 리스트 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/articles/search")
    fun getBoardArticleSearch(model: Model): String {
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        return boardArticlesSearchPage
    }

    /**
     * 게시판 조회조건 포함 리스트 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/articles/search/param")
    fun getBoardArticleSearchParam(boardSearchDto: BoardSearchDto, model: Model): String {
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        model.addAttribute("boardAdminId", boardSearchDto.boardAdminId)
        return boardArticlesSearchPage
    }

    /**
     * 게시판 리스트 화면.
     *
     * @param boardSearchDto
     * @param model
     * @return String
     */
    @GetMapping("/articles")
    fun getBoardArticleList(boardSearchDto: BoardSearchDto, model: Model): String {
        val result = boardService.getBoardList(boardSearchDto)
        model.addAttribute("boardList", result)
        model.addAttribute("boardCount", if (result.isNotEmpty()) result[0].totalCount else 0)
        return boardArticlesListPage
    }

    /**
     * 게시판 상세 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/articles/{boardId}/view")
    fun getBoardArticleView(@PathVariable boardId: String, model: Model): String {
        val boardDtoInfo: BoardViewDto = boardService.getBoard(boardId, "view")
        model.addAttribute("boardInfo", boardDtoInfo)
        model.addAttribute("boardAdminInfo", boardDtoInfo.boardAdmin)
        return boardArticlesViewPage
    }

    /**
     * 게시판 신규 등록 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("/articles/{boardAdminId}/new")
    fun getBoardArticleNew(@PathVariable boardAdminId: String, model: Model): String {
        val boardAdminInfo: BoardAdminDto = boardService.getBoardAdmin(boardAdminId)
        if (boardAdminInfo.categoryYn) {
            model.addAttribute("boardCategoryInfo", boardService.getBoardCategoryList(boardAdminInfo.boardAdminId))
        }
        model.addAttribute("boardAdminInfo", boardAdminInfo)
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        model.addAttribute("replyYn", false)
        return boardArticlesEditPage
    }

    /**
     * 게시판 편집 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/articles/{boardId}/edit")
    fun getBoardArticleEdit(@PathVariable boardId: String, model: Model): String {
        val boardDtoInfo: BoardViewDto = boardService.getBoard(boardId, "edit")
        if (boardDtoInfo.boardAdmin.categoryYn) {
            model.addAttribute("boardCategoryInfo", boardDtoInfo.boardAdmin.category)
        }
        model.addAttribute("boardAdminInfo", boardDtoInfo.boardAdmin)
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        model.addAttribute("boardInfo", boardDtoInfo)
        model.addAttribute("replyYn", false)
        return boardArticlesEditPage
    }

    /**
     * 게시판 댓글 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/articles/{boardId}/comments")
    fun getBoardArticleCommentList(@PathVariable boardId: String, model: Model): String {
        model.addAttribute("boardCommentList", boardService.getBoardCommentList(boardId))
        return boardArticlesCommentListPage
    }

    /**
     * 게시판 답글 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/articles/{boardId}/replay/edit")
    fun getBoardArticleReplayEdit(@PathVariable boardId: String, model: Model): String {
        val boardDtoInfo: BoardViewDto = boardService.getBoard(boardId, "reply")
        if (boardDtoInfo.boardAdmin.categoryYn) {
            model.addAttribute(
                "boardCategoryInfo",
                boardService.getBoardCategoryList(boardDtoInfo.boardAdmin.boardAdminId)
            )
        }
        model.addAttribute("boardAdminList", boardService.getBoardAdminList())
        model.addAttribute("boardAdminInfo", boardDtoInfo.boardAdmin)
        model.addAttribute("boardInfo", boardDtoInfo)
        model.addAttribute("replyYn", true)
        return boardArticlesEditPage
    }
}
