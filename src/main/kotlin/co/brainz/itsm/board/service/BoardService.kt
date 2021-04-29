/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.service

import co.brainz.itsm.board.dto.BoardCategoryDetailDto
import co.brainz.itsm.board.dto.BoardCategoryDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.dto.BoardListReturnDto
import co.brainz.itsm.board.dto.BoardSearchDto
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.board.entity.PortalBoardCategoryEntity
import co.brainz.itsm.board.repository.BoardAdminRepository
import co.brainz.itsm.board.repository.BoardCategoryRepository
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardAdminRepository: BoardAdminRepository,
    private val boardCategoryRepository: BoardCategoryRepository
) {

    /**
     * [boardSearchDto]로 받아서 게시판 관리 목록 조회를 [BoardListReturnDto]으로 반환.
     *
     */
    fun getBoardList(boardSearchDto: BoardSearchDto): BoardListReturnDto {
        return boardAdminRepository.findByBoardAdminList(
            boardSearchDto.search,
            boardSearchDto.offset
        )
    }

    /**
     * 게시판 관리 저장.
     *
     * @param boardDto
     */
    @Transactional
    fun saveBoard(boardDto: BoardDto): Boolean {
        val portalBoardAdminEntity = PortalBoardAdminEntity(
            boardAdminId = boardDto.boardAdminId,
            boardAdminTitle = boardDto.boardAdminTitle,
            boardAdminDesc = boardDto.boardAdminDesc,
            boardAdminSort = boardDto.boardAdminSort,
            boardUseYn = boardDto.boardUseYn,
            replyYn = boardDto.replyYn,
            commentYn = boardDto.commentYn,
            categoryYn = boardDto.categoryYn,
            attachYn = boardDto.attachYn,
            attachFileSize = boardDto.attachFileSize
        )
        val preBoardEntity: PortalBoardAdminEntity? =
            boardAdminRepository.findByBoardAdminId(portalBoardAdminEntity.boardAdminId)
        val duplicateCount = boardAdminRepository.countByBoardAdminTitle(boardDto.boardAdminTitle!!)
        if (duplicateCount > 0 && !boardDto.boardAdminTitle.equals(preBoardEntity?.boardAdminTitle)) {
            return false
        }
        val boardAdmin = boardAdminRepository.save(portalBoardAdminEntity)
        if (boardDto.categoryYn) {
            val categoryList = mutableListOf<PortalBoardCategoryEntity>()
            if (boardDto.boardAdminId.isNotEmpty()) {
                val boardCategoryList = boardCategoryRepository.findByCategoryList(boardDto.boardAdminId)
                val newCategoryList = mutableListOf<BoardCategoryDetailDto>()
                val existsCategoryList = mutableListOf<BoardCategoryDto>()
                for (boardCategoryDetailDto in boardDto.categoryList) {
                    if (boardCategoryDetailDto.boardCategoryId.isEmpty()) { // new
                        newCategoryList.add(boardCategoryDetailDto)
                    } else {
                        for (boardCategoryDto in boardCategoryList) {
                            if (boardCategoryDto.boardCategoryId == boardCategoryDetailDto.boardCategoryId) {
                                existsCategoryList.add(boardCategoryDto)
                            }
                        }
                    }
                }

                val deleteCategoryList = mutableListOf<BoardCategoryDto>()
                for (boardCategoryDto in boardCategoryList) {
                    if (!existsCategoryList.contains(boardCategoryDto)) {
                        deleteCategoryList.add(boardCategoryDto)
                    }
                }

                deleteCategoryList.forEach { category ->
                    this.deleteBoardCategory(category.boardCategoryId)
                }

                newCategoryList.forEach { category ->
                    categoryList.add(
                        PortalBoardCategoryEntity(
                            boardCategoryId = "",
                            boardAdmin = boardAdminRepository.getOne(boardDto.boardAdminId),
                            boardCategoryName = category.boardCategoryName,
                            boardCategorySort = category.boardCategorySort
                        )
                    )
                }
            } else {
                boardDto.categoryList.forEach { category ->
                    categoryList.add(
                        PortalBoardCategoryEntity(
                            boardCategoryId = "",
                            boardAdmin = boardAdmin,
                            boardCategoryName = category.boardCategoryName,
                            boardCategorySort = category.boardCategorySort
                        )
                    )
                }
            }
            if (categoryList.isNotEmpty()) {
                boardCategoryRepository.saveAll(categoryList)
            }
        }
        return true
    }

    /**
     * 게시판 관리 상세 조회
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    @Transactional
    fun getBoardDetail(boardAdminId: String): BoardDto {
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        var enabled = true
        if (boardAdminEntity.board?.count() ?: 0 > 0) {
            enabled = false
        }
        return BoardDto(
            boardAdminId = boardAdminEntity.boardAdminId,
            boardAdminTitle = boardAdminEntity.boardAdminTitle,
            boardAdminDesc = boardAdminEntity.boardAdminDesc,
            boardAdminSort = boardAdminEntity.boardAdminSort,
            boardUseYn = boardAdminEntity.boardUseYn,
            replyYn = boardAdminEntity.replyYn,
            commentYn = boardAdminEntity.commentYn,
            categoryYn = boardAdminEntity.categoryYn,
            attachYn = boardAdminEntity.attachYn,
            attachFileSize = boardAdminEntity.attachFileSize,
            enabled = enabled,
            createDt = boardAdminEntity.createDt,
            createUser = boardAdminEntity.createUser,
            updateDt = boardAdminEntity.updateDt,
            updateUser = boardAdminEntity.updateUser,
            categoryList = getBoardCategoryDetailList(boardAdminEntity.boardAdminId)
        )
    }

    /**
     * 게시판 관리 삭제.
     *
     * @param boardAdminId
     */
    @Transactional
    fun deleteBoard(boardAdminId: String) {
        boardAdminRepository.deleteById(boardAdminId)
    }

    /**
     * 카테고리 리스트
     *
     * @param boardAdminId
     * @return List<BoardCategoryDetailDto>
     */
    fun getBoardCategoryDetailList(boardAdminId: String): List<BoardCategoryDetailDto> {
        val boardCategoryList = boardCategoryRepository.findByCategoryList(boardAdminId)
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        val boardCategoryDetailDtoList = mutableListOf<BoardCategoryDetailDto>()
        for (boardCategory in boardCategoryList) {
            boardCategoryDetailDtoList.add(
                BoardCategoryDetailDto(
                    boardCategoryId = boardCategory.boardCategoryId,
                    boardAdminId = boardAdminEntity.boardAdminId,
                    boardCategoryName = boardCategory.boardCategoryName,
                    boardCategorySort = boardCategory.boardCategorySort,
                    boardCount = boardCategory.boardCount
                )
            )
        }
        return boardCategoryDetailDtoList
    }

    /**
     * 카테고리 삭제
     *
     * @param boardCategoryId
     */
    @Transactional
    fun deleteBoardCategory(boardCategoryId: String) {
        boardCategoryRepository.deleteById(boardCategoryId)
    }

    /**
     * 게시판 관리 목록 -> 게시판에서 selectbox에서 사용한다.
     *
     * @return MutableList<PortalBoardAdminEntity>
     */
    fun getSelectBoard(): List<BoardListDto>? {
        return boardAdminRepository.findPortalBoardAdmin()
    }
}
