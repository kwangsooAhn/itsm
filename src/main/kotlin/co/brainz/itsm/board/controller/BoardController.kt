/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.controller

import co.brainz.itsm.board.dto.BoardArticleSearchCondition
import co.brainz.itsm.board.dto.BoardArticleViewDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardSearchCondition
import co.brainz.itsm.board.service.BoardArticleService
import co.brainz.itsm.board.service.BoardService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/boards")
class BoardController(
    private val boardService: BoardService,
    private val boardArticleService: BoardArticleService
) {

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
     *  [BoardSearchCondition]를 받아서 게시판 관리 리스트 화면에[String]으로 반환한다.
     *
     */
    @GetMapping("")
    fun getBoardList(boardSearchCondition: BoardSearchCondition, model: Model): String {
        val result = boardService.getBoardList(boardSearchCondition)
        model.addAttribute("boardAdminList", result.data)
        model.addAttribute("paging", result.paging)
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
        model.addAttribute("boardAdmin", boardService.getBoardDetail(boardAdminId))
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
        model.addAttribute("boardAdmin", boardService.getBoardDetail(boardAdminId))
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
        model.addAttribute("boardAdminList", boardService.getSelectBoard())
        return boardArticlesSearchPage
    }

    /**
     * 게시판 조회조건 포함 리스트 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/articles/search/param")
    fun getBoardArticleSearchParam(boardArticleSearchCondition: BoardArticleSearchCondition, model: Model): String {
        model.addAttribute("boardAdminList", boardService.getSelectBoard())
        model.addAttribute("boardAdminId", boardArticleSearchCondition.boardAdminId)
        return boardArticlesSearchPage
    }

    /**
     * 게시판 리스트 화면.
     *
     * @param boardArticleSearchCondition
     * @param model
     * @return String
     */
    @GetMapping("/articles")
    fun getBoardArticleList(boardArticleSearchCondition: BoardArticleSearchCondition, model: Model): String {
        val result = boardArticleService.getBoardArticleList(boardArticleSearchCondition)
        model.addAttribute("boardList", result.data)
        model.addAttribute("paging", result.paging)
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
        val boardArticleDtoInfo: BoardArticleViewDto = boardArticleService.getBoardArticleDetail(boardId, "view")
        model.addAttribute("boardInfo", boardArticleDtoInfo)
        model.addAttribute("boardAdminInfo", boardArticleDtoInfo.boardAdmin)
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
        val boardInfo: BoardDto = boardArticleService.getBoardArticleDetail(boardAdminId)
        if (boardInfo.categoryYn) {
            model.addAttribute(
                "boardCategoryInfo",
                boardArticleService.getBoardArticleCategoryList(boardInfo.boardAdminId)
            )
        }
        model.addAttribute("boardAdminInfo", boardInfo)
        model.addAttribute("boardAdminList", boardService.getSelectBoard())
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
        val boardArticleDtoInfo: BoardArticleViewDto = boardArticleService.getBoardArticleDetail(boardId, "edit")
        if (boardArticleDtoInfo.boardAdmin.categoryYn) {
            model.addAttribute("boardCategoryInfo", boardArticleDtoInfo.boardAdmin.category)
        }
        model.addAttribute("boardAdminInfo", boardArticleDtoInfo.boardAdmin)
        model.addAttribute("boardAdminList", boardService.getSelectBoard())
        model.addAttribute("boardInfo", boardArticleDtoInfo)
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
        model.addAttribute("boardCommentList", boardArticleService.getBoardArticleCommentList(boardId))
        return boardArticlesCommentListPage
    }

    /**
     * 게시판 답글 조회 화면.
     *
     * @param boardId
     * @param model
     * @return String
     */
    @GetMapping("/articles/{boardId}/reply/edit")
    fun getBoardArticleReplyEdit(@PathVariable boardId: String, model: Model): String {
        val boardArticleDtoInfo: BoardArticleViewDto = boardArticleService.getBoardArticleDetail(boardId, "reply")
        if (boardArticleDtoInfo.boardAdmin.categoryYn) {
            model.addAttribute(
                "boardCategoryInfo",
                boardArticleService.getBoardArticleCategoryList(boardArticleDtoInfo.boardAdmin.boardAdminId)
            )
        }
        model.addAttribute("boardAdminList", boardService.getSelectBoard())
        model.addAttribute("boardAdminInfo", boardArticleDtoInfo.boardAdmin)
        model.addAttribute("boardInfo", boardArticleDtoInfo)
        model.addAttribute("replyYn", true)
        return boardArticlesEditPage
    }
}
