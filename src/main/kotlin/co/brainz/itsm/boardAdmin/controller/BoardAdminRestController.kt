/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.controller

import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminListDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminSearchDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminDetailDto
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDto
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
@RequestMapping("/rest/board-admin")
class BoardAdminRestController(private val boardAdminService: BoardAdminService) {

    /**
     * [BoardAdminSearchDto]를 받아서 게시판 관리 리스트에 [List<BoardAdminListDto>]로 반환한다.
     *
     */
    @GetMapping("")
    fun getBoardAdminList(boardAdminSearchDto: BoardAdminSearchDto): List<BoardAdminListDto> {
        return boardAdminService.getBoardAdminList(boardAdminSearchDto)
    }

    /**
     * [boardAdminId]를 받아서 게시판 관리 정보를 [BoardAdminDetailDto]로 반환한다.
     *
     */
    @GetMapping("/{boardAdminId}/view")
    fun getBoardAdminView(@PathVariable boardAdminId: String): BoardAdminDetailDto {
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
            createUserName = boardAdminDetail.createUser?.userName,
            updateDt = boardAdminDetail.updateDt,
            updateUserName = boardAdminDetail.updateUser?.userName
        )
    }

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
