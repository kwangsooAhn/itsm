/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.service

import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqDto
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqListReturnDto
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.repository.FaqRepository
import kotlin.math.ceil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * ### FAQ 관련 서비스 레이어 클래스.
 *
 * FAQ관련 데이터 조회/등록/변경/삭제를 처리한다.
 *
 * @author Jung heechan
 */
@Service
class FaqService(
    private val faqRepository: FaqRepository,
    private val aliceFileService: AliceFileService,
    private val codeService: CodeService
    ) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    /**
     * FAQ 목록을 조회한다.
     */
    fun getFaqs(faqSearchCondition: FaqSearchCondition): FaqListReturnDto {
        val queryResult = faqRepository.findFaqs(faqSearchCondition)
        val currentSessionUserCodeList = codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE)
        val fapList: MutableList<FaqListDto> = mutableListOf()

        for (faq in queryResult.results) {
           for (code in currentSessionUserCodeList) {
               if (faq.faqGroup == code.code) {
                   faq.faqGroupName = code.codeName.toString()
                   fapList.add(faq)
               }
           }
        }

        return FaqListReturnDto(
            data = fapList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = faqRepository.count(),
                currentPageNum = faqSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CATEGORY_ASC.code
            )
        )
    }

    /**
     * FAQ 전체 카테고리 조회
     */
    fun findAllFaqGroups(): List<String> {
        return faqRepository.getAllFaqGroupList()
    }

    /**
     * FAQ 데이터 상세 조회
     */
    fun getFaqDetail(faqId: String): FaqListDto? {
        return faqRepository.findFaq(faqId)
    }

    /**
     * FAQ 등록
     */
    @Transactional
    fun createFaq(faqDto: FaqDto): Boolean {
        var isSuccess = true
        val faqEntity = FaqEntity(
            faqGroup = faqDto.faqGroup,
            faqTitle = faqDto.faqTitle,
            faqContent = faqDto.faqContent
        )
        // Duplicate check
        if (faqRepository.getCountDuplicateFaqTitleAndCategory(faqEntity.faqTitle, faqEntity.faqGroup) > 0) {
            isSuccess = false
        }
        if (isSuccess) {
            faqRepository.save(faqEntity)
        }
        return isSuccess
    }

    /**
     * FAQ 변경
     */
    @Transactional
    fun updateFaq(faqId: String, faqDto: FaqDto): Boolean {
        val faqEntity = faqRepository.getOne(faqId)
        val count = faqRepository.getCountDuplicateFaqTitleAndCategory(faqDto.faqTitle, faqDto.faqGroup)
        if (count == 0 || (faqDto.faqTitle.equals(faqEntity.faqTitle) && faqDto.faqGroup.equals(faqEntity.faqGroup))) {
            faqEntity.faqGroup = faqDto.faqGroup
            faqEntity.faqTitle = faqDto.faqTitle
            faqEntity.faqContent = faqDto.faqContent
            faqRepository.save(faqEntity)
            return true
        }
        return false
    }

    /**
     * FAQ 삭제
     */
    @Transactional
    fun deleteFaq(faqId: String): Boolean {
        faqRepository.deleteById(faqId)
        return true
    }
}
