/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.boardAdmin.service

import co.brainz.itsm.boardAdmin.dto.BoardAdminDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminListDto
import co.brainz.itsm.boardAdmin.dto.BoardAdminSearchDto
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDetailDto
import co.brainz.itsm.boardAdmin.dto.BoardCategoryDto
import co.brainz.itsm.boardAdmin.entity.PortalBoardAdminEntity
import co.brainz.itsm.boardAdmin.entity.PortalBoardCategoryEntity
import co.brainz.itsm.boardAdmin.repository.BoardAdminRepository
import co.brainz.itsm.boardAdmin.repository.BoardCategoryRepository
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class BoardAdminService(
    private val boardAdminRepository: BoardAdminRepository,
    private val boardCategoryRepository: BoardCategoryRepository
) {

    /**
     * [boardAdminSearchDto]로 받아서 게시판 관리 목록 조회를 [List<BoardAdminListDto>]으로 반환.
     *
     */
    fun getBoardAdminList(boardAdminSearchDto: BoardAdminSearchDto): List<BoardAdminListDto> {
        return boardAdminRepository.findByBoardAdminList(
            boardAdminSearchDto.search,
            boardAdminSearchDto.offset
        )
    }

    /**
     * 게시판 관리 저장.
     *
     * @param boardAdminDto
     */
    @Transactional
    fun saveBoardAdmin(boardAdminDto: BoardAdminDto) {
        val portalBoardAdminEntity = PortalBoardAdminEntity(
            boardAdminId = boardAdminDto.boardAdminId,
            boardAdminTitle = boardAdminDto.boardAdminTitle,
            boardAdminDesc = boardAdminDto.boardAdminDesc,
            boardAdminSort = boardAdminDto.boardAdminSort,
            boardUseYn = boardAdminDto.boardUseYn,
            replyYn = boardAdminDto.replyYn,
            commentYn = boardAdminDto.commentYn,
            categoryYn = boardAdminDto.categoryYn,
            attachYn = boardAdminDto.attachYn,
            attachFileSize = boardAdminDto.attachFileSize
        )
        val boardAdmin = boardAdminRepository.save(portalBoardAdminEntity)

        if (boardAdminDto.categoryYn) {
            val categoryList = mutableListOf<PortalBoardCategoryEntity>()
            if (boardAdminDto.boardAdminId.isNotEmpty()) {
                val boardCategoryList = boardCategoryRepository.findByCategoryList(boardAdminDto.boardAdminId)
                val newCategoryList = mutableListOf<BoardCategoryDetailDto>()
                val existsCategoryList = mutableListOf<BoardCategoryDto>()
                for (boardCategoryDetailDto in boardAdminDto.categoryList) {
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
                    this.deleteBoardCategory(category.boardCategoryId);
                }

                newCategoryList.forEach { category ->
                    categoryList.add(
                        PortalBoardCategoryEntity(
                            boardCategoryId = "",
                            boardAdmin = boardAdminRepository.getOne(boardAdminDto.boardAdminId),
                            boardCategoryName = category.boardCategoryName,
                            boardCategorySort = category.boardCategorySort
                        )
                    )
                }
            } else {
                boardAdminDto.categoryList.forEach { category ->
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
    }

    /**
     * 게시판 관리 상세 조회
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
    @Transactional
    fun getBoardAdmin(boardAdminId: String): BoardAdminDto {
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        var enabled = true
        if (boardAdminEntity.board?.count() ?: 0 > 0) {
            enabled = false
        }
        return BoardAdminDto(
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
    fun deleteBoardAdmin(boardAdminId: String) {
        boardAdminRepository.deleteById(boardAdminId)
    }

    /**
     * 카테고리 리스트
     *
     * @param boardAdminId
     * @return List<BoardCategoryDto>
     */
    fun getBoardCategoryList(boardAdminId: String): List<BoardCategoryDto>? {
        val boardCategoryList = boardCategoryRepository.findByCategoryList(boardAdminId)
        val boardAdminEntity = boardAdminRepository.findById(boardAdminId).orElse(null)
        val boardCategoryDtoList = mutableListOf<BoardCategoryDto>()
        for (boardCategory in boardCategoryList) {
            boardCategoryDtoList.add(
                BoardCategoryDto(
                    boardCategoryId = boardCategory.boardCategoryId,
                    boardAdmin = boardAdminEntity,
                    boardAdminId = boardAdminEntity.boardAdminId,
                    boardCategoryName = boardCategory.boardCategoryName,
                    boardCategorySort = boardCategory.boardCategorySort,
                    boardCount = boardCategory.boardCount
                )
            )
        }
        return boardCategoryDtoList
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
}
