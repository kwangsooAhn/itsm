/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.board.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.board.dto.BoardCategoryDetailDto
import co.brainz.itsm.board.dto.BoardCategoryDto
import co.brainz.itsm.board.dto.BoardDto
import co.brainz.itsm.board.dto.BoardListDto
import co.brainz.itsm.board.dto.BoardListReturnDto
import co.brainz.itsm.board.dto.BoardSearchCondition
import co.brainz.itsm.board.entity.PortalBoardAdminEntity
import co.brainz.itsm.board.entity.PortalBoardCategoryEntity
import co.brainz.itsm.board.repository.BoardAdminRepository
import co.brainz.itsm.board.repository.BoardCategoryRepository
import co.brainz.itsm.board.repository.BoardRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.convertValue
import javax.transaction.Transactional
import kotlin.math.ceil
import org.springframework.stereotype.Service

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val boardAdminRepository: BoardAdminRepository,
    private val boardCategoryRepository: BoardCategoryRepository
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * [boardSearchCondition]로 받아서 게시판 관리 목록 조회를 [BoardListReturnDto]으로 반환.
     *
     */
    fun getBoardList(boardSearchCondition: BoardSearchCondition): BoardListReturnDto {
        val pagingResult = boardAdminRepository.findByBoardAdminList(boardSearchCondition)
        return BoardListReturnDto(
            data = mapper.convertValue(pagingResult.dataList),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = boardAdminRepository.count(),
                currentPageNum = boardSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / boardSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 게시판 관리 저장.
     *
     * @param boardDto
     */
    @Transactional
    fun saveBoard(boardDto: BoardDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
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
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
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

                    // 카테고리 삭제
                    deleteCategoryList.forEach { category ->
                        boardCategoryRepository.deleteById(category.boardCategoryId)
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
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 게시판 관리 상세 조회
     *
     * @param boardAdminId
     * @return BoardAdminDto
     */
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
    fun deleteBoard(boardAdminId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (boardRepository.countByBoardAdminId(boardAdminId) > 0) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        } else {
            boardAdminRepository.deleteById(boardAdminId)
        }
        return ZResponse(
            status = status.code
        )
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
     * 게시판 관리 목록 -> 게시판에서 selectbox에서 사용한다.
     *
     * @return MutableList<PortalBoardAdminEntity>
     */
    fun getSelectBoard(): List<BoardListDto>? {
        return boardAdminRepository.findPortalBoardAdmin()
    }
}
