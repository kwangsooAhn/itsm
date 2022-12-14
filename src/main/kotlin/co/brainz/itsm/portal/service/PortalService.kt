/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.service

import co.brainz.itsm.archive.repository.ArchiveRepository
import co.brainz.itsm.code.repository.CodeRepository
import co.brainz.itsm.code.service.CodeService
import co.brainz.itsm.faq.constants.FaqConstants
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchCondition
import co.brainz.itsm.faq.repository.FaqRepository
import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.portal.dto.PortalListReturnDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.dto.PortalTopListDto
import co.brainz.itsm.portal.repository.PortalRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PortalService(
    private val noticeRepository: NoticeRepository,
    private val faqRepository: FaqRepository,
    private val archiveRepository: ArchiveRepository,
    private val portalRepository: PortalRepository,
    private val codeRepository: CodeRepository,
    private val codeService: CodeService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 포탈 리스트 , 포탈 검색 리스트
     */
    fun findPortalListOrSearchList(portalSearchDto: PortalSearchDto): PortalListReturnDto {
        return portalRepository.findPortalSearchList(
            portalSearchDto.searchValue, portalSearchDto.offset
        )
    }

    fun getTopList(limit: Long): PortalTopListDto {
        return PortalTopListDto(
            notice = noticeRepository.findNoticeTopList(limit),
            faq = faqRepository.findFaqTopList(limit),
            archive = archiveRepository.findArchiveTopList(limit)
        )
    }

    fun getFaqCategories(faqId: String?): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        faqInfo["faqCategory"] = codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE)
        faqInfo["faqId"] = faqId ?: ""
        return faqInfo
    }

    fun getFaqList(category: String, faqId: String): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        faqInfo["faqGroupList"] = when (category.isBlank()) {
            true -> codeService.selectCodeByParent(FaqConstants.FAQ_CATEGORY_P_CODE)
            false -> codeRepository.getOne(category)
        }
        val faqAll = faqRepository.findFaqs(FaqSearchCondition(null, null, null, 0, 0))
        val faqList = faqAll.dataList as List<FaqListDto>

        var selectedFaq = FaqListDto()
        if (faqId.isNotEmpty()) {
            selectedFaq = faqList.filter {
                it.faqId == faqId
            }[0]
        }
        faqInfo["faqList"] = faqList
        faqInfo["faqSelected"] = selectedFaq
        return faqInfo
    }
}
