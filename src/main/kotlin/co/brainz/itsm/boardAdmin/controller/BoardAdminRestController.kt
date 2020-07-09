package co.brainz.itsm.boardAdmin.controller

import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDto
import co.brainz.itsm.boardAdmin.service.BoardAdminService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/board-admin")
class BoardAdminRestController(private val boardAdminService: BoardAdminService) {

    /**
     * 게시판 관리 신규 등록.
     *
     * @param boardAdminDto
     */
    @PostMapping("")
    fun createBoardAdmin(@RequestBody boardAdminDto: BoardAdminDto) {
        boardAdminService.saveBoardAdmin(boardAdminDto)
    }

    /**
     * 게시판 관리 수정.
     *
     * @param boardAdminDto
     */
    @PutMapping("")
    fun updateBoardAdmin(@RequestBody boardAdminDto: BoardAdminDto) {
        boardAdminService.saveBoardAdmin(boardAdminDto)
    }

    /**
     * 게시판 관리 삭제.
     *
     * @param boardAdminId
     */
    @DeleteMapping("/{boardAdminId}")
    fun deleteBoardAdmin(@PathVariable boardAdminId: String) {
        boardAdminService.deleteBoardAdmin(boardAdminId)
    }

    /**
     * 카테고리 신규 등록.
     *
     * @param boardCategoryDto
     */
    @PostMapping("/category")
    fun createBoardCategory(@RequestBody boardCategoryDto: BoardCategoryDto) {
        boardAdminService.saveBoardCategory(boardCategoryDto)
    }

    /**
     * 카테고리 관리 삭제.
     *
     * @param boardCategoryId
     */
    @DeleteMapping("/category/{boardCategoryId}")
    fun deleteBoardCategory(@PathVariable boardCategoryId: String) {
        boardAdminService.deleteBoardCategory(boardCategoryId)
    }
}
