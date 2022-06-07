/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.faq.service

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqDto
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqListReturnDto
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.repository.FaqRepository
import co.brainz.itsm.user.service.UserService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
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
    private val codeService: CodeService,
    private val userService: UserService
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
    private val auth = AuthConstants.AuthType.PORTAL_MANAGE.value

    /**
     * FAQ 목록을 조회한다.
     */
    fun getFaqs(faqSearchCondition: FaqSearchCondition): FaqListReturnDto {
        val pagingResult = faqRepository.findFaqs(faqSearchCondition)
        val currentSessionUserCodeList = codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE)
        val pagingList: List<FaqListDto> = mapper.convertValue(pagingResult.dataList, object : TypeReference<List<FaqListDto>>() {})
        val faqList: MutableList<FaqListDto> = mutableListOf()

        for (faq in pagingList) {
            for (code in currentSessionUserCodeList) {
                if (faq.faqGroup == code.code) {
                    faq.faqGroupName = code.codeName.toString()
                    faqList.add(faq)
                }
            }
        }

        return FaqListReturnDto(
            data = faqList,
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = faqRepository.count(),
                currentPageNum = faqSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / faqSearchCondition.contentNumPerPage.toDouble()).toLong(),
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
    fun createFaq(faqDto: FaqDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (faqRepository.getCountDuplicateFaqTitleAndCategory(faqDto.faqTitle, faqDto.faqGroup) > 0) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        } else {
            faqRepository.save(
                FaqEntity(
                    faqGroup = faqDto.faqGroup,
                    faqTitle = faqDto.faqTitle,
                    faqContent = faqDto.faqContent
                )
            )
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * FAQ 변경
     */
    @Transactional
    fun updateFaq(faqId: String, faqDto: FaqDto): ZResponse {
        val faqEntity = faqRepository.getOne(faqId)
        faqEntity.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }

        var status = ZResponseConstants.STATUS.SUCCESS
        val count = faqRepository.getCountDuplicateFaqTitleAndCategory(faqDto.faqTitle, faqDto.faqGroup)
        if (count == 0 || (faqDto.faqTitle == faqEntity.faqTitle && faqDto.faqGroup == faqEntity.faqGroup)) {
            faqEntity.faqGroup = faqDto.faqGroup
            faqEntity.faqTitle = faqDto.faqTitle
            faqEntity.faqContent = faqDto.faqContent
            faqRepository.save(faqEntity)
        } else {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * FAQ 삭제
     */
    @Transactional
    fun deleteFaq(faqId: String): ZResponse {
        val faq = faqRepository.findById(faqId).get()
        faq.createUser?.let { userService.userAccessAuthCheck(it.userKey, auth) }
        faqRepository.deleteById(faqId)
        return ZResponse()
    }
}
