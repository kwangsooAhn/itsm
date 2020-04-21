package co.brainz.itsm.boardAdmin.controller

import co.brainz.itsm.boardAdmin.service.BoardAdminService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/board-admin")
class BoardAdminController(private val boardAdminService: BoardAdminService) {

    private val boardAdminSearchPage: String = "boardAdmin/boardAdminSearch"
    private val boardAdminListPage: String = "boardAdmin/boardAdminList"
    private val boardAdminEditPage: String = "boardAdmin/boardAdminEdit"
    private val boardAdminViewPage: String = "boardAdmin/boardAdminView"
    private val boardCategoryListPage: String = "boardAdmin/boardCategoryList"
    private val boardCategoryEditPage: String = "boardAdmin/boardCategoryEdit"

    /**
     * 게시판 관리 호출 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/search")
    fun getBoardAdminSearch(model: Model): String {
        return boardAdminSearchPage
    }

    /**
     * 게시판 관리 리스트 화면.
     *
     * @param search
     * @param model
     * @return String
     */
    @GetMapping("/list")
    fun getBoardAdminList(search: String, model: Model): String {
        model.addAttribute("boardAdminList", boardAdminService.getBoardAdminList(search))
        return boardAdminListPage
    }

    /**
     * 게시판 관리 신규 등록 화면.
     *
     * @param model
     * @return String
     */
    @GetMapping("/new")
    fun getBoardAdminNew(model: Model): String {
        return boardAdminEditPage
    }

    /**
     * 게시판 관리 상세 조회 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("/{boardAdminId}/view")
    fun getBoardAdminView(@PathVariable boardAdminId: String, model: Model): String {
        model.addAttribute("boardAdmin", boardAdminService.getBoardAdmin(boardAdminId))
        return boardAdminViewPage
    }

    /**
     * 게시판 관리 편집 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("/{boardAdminId}/edit")
    fun getBoardAdminEdit(@PathVariable boardAdminId: String, model: Model): String {
        model.addAttribute("boardAdmin", boardAdminService.getBoardAdmin(boardAdminId))
        return boardAdminEditPage
    }

    /**
     * 게시판 관리  카테고리 호출 화면.
     *
     * @param boardAdminId
     * @return String
     */
    @GetMapping("/category/{boardAdminId}/edit")
    fun getBoardCategoryEdit(@PathVariable boardAdminId: String, model: Model): String {
        model.addAttribute("boardAdmin", boardAdminService.getBoardAdmin(boardAdminId))
        return boardCategoryEditPage
    }

    /**
     * 게시판 관리 리스트 화면.
     *
     * @param boardAdminId
     * @param model
     * @return String
     */
    @GetMapping("/category/list")
    fun getBoardCategoryList(boardAdminId: String, model: Model): String {
        model.addAttribute("boardCategoryList", boardAdminService.getBoardCategoryList(boardAdminId))
        return boardCategoryListPage
    }

}