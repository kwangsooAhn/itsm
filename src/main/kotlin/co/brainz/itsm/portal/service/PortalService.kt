/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.service

import co.brainz.itsm.download.repository.DownloadRepository
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.repository.FaqRepository
import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.dto.PortalTopDto
import co.brainz.itsm.portal.repository.PortalRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PortalService(
    private val noticeRepository: NoticeRepository,
    private val faqRepository: FaqRepository,
    private val downloadRepository: DownloadRepository,
    private val portalRepository: PortalRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 포탈 리스트 , 포탈 검색 리스트
     */
    fun findPortalListOrSearchList(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        return portalRepository.findPortalSearchList(
            portalSearchDto.searchValue, portalSearchDto.offset
        )
    }

    fun getTopList(limit: Long): LinkedHashMap<String, List<PortalTopDto>> {
        val top = LinkedHashMap<String, List<PortalTopDto>>()
        top["notice"] = noticeRepository.findNoticeTopList(limit)
        top["faq"] = faqRepository.findFaqTopList(limit)
        top["download"] = downloadRepository.findDownloadTopList(limit)
        return top
    }

    fun getFaqCategories(faqId: String?): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        faqInfo["faqCategory"] = faqRepository.getAllFaqGroupList()
        faqInfo["faqId"] = faqId ?: ""
        return faqInfo
    }

    fun getFaqList(category: String, faqId: String): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        faqInfo["faqGroupList"] = when (category.isBlank()) {
            true -> faqRepository.getAllFaqGroupList()
            false -> faqRepository.getFaqGroupList(category)
        }
        val faqAll = faqRepository.getFaqList()
        val faqList = mutableListOf<FaqListDto>()
        faqAll.forEach { faq ->
            faqList.add(
                FaqListDto(
                    faqId = faq.faqId,
                    faqGroup = faq.faqGroup,
                    faqTitle = faq.faqTitle,
                    faqContent = faq.faqContent,
                    createDt = null,
                    createUserName = ""
                )
            )
        }

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
