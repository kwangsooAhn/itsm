/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.service

import co.brainz.itsm.constants.ItsmConstants
import co.brainz.itsm.download.dto.DownloadDto
import co.brainz.itsm.download.mapper.DownloadMapper
import co.brainz.itsm.download.repository.DownloadRepository
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.dto.FaqSearchRequestDto
import co.brainz.itsm.faq.mapper.FaqMapper
import co.brainz.itsm.faq.repository.FaqRepository
import co.brainz.itsm.notice.dto.NoticeListDto
import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class PortalService(
    private val noticeRepository: NoticeRepository,
    private val faqRepository: FaqRepository,
    private val downloadRepository: DownloadRepository
) {
    private val faqMapper: FaqMapper = Mappers.getMapper(FaqMapper::class.java)
    private val downloadMapper: DownloadMapper = Mappers.getMapper(DownloadMapper::class.java)

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 포탈 리스트 , 포탈 검색 리스트
     */
    fun findPortalListOrSearchList(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        return noticeRepository.findPortalListOrSearchList(
            portalSearchDto.searchValue, ItsmConstants
                .SEARCH_DATA_COUNT, portalSearchDto.offset
        )
    }

    /**
     * 포탈 검색 리스트 글 전체 개수 조회
     */
    fun findPortalListOrSearchCount(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        return noticeRepository.findPortalListOrSearchCount(portalSearchDto.searchValue)
    }

    fun getTopList(limit: Long): LinkedHashMap<String, Any> {
        val noticeTopList = mutableListOf<NoticeListDto>()
        val noticeEntities = noticeRepository.findNoticeTopList(limit)
        noticeEntities.forEach {
            noticeTopList.add(
                NoticeListDto(
                    noticeNo = it.noticeNo,
                    noticeTitle = it.noticeTitle
                )
            )
        }

        val faqTopList = mutableListOf<FaqListDto>()
        val faqEntities = faqRepository.findFaqTopList(limit)
        faqEntities.forEach {
            faqTopList.add(faqMapper.toFaqListDto(it))
        }

        val downloadTopList = mutableListOf<DownloadDto>()
        val downloadEntities = downloadRepository.findDownloadTopList(limit)
        downloadEntities.forEach {
            downloadTopList.add(downloadMapper.toDownloadDto(it))
        }

        val top = LinkedHashMap<String, Any>()
        top["notice"] = noticeTopList
        top["faq"] = faqTopList
        top["download"] = downloadTopList

        return top
    }

    fun getFaqCategories(): List<String> {
        return faqRepository.getAllFaqGroupList()
    }

    fun getFaqList(faqSearchRequestDto: FaqSearchRequestDto): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        val category = faqSearchRequestDto.groupCodes
        faqInfo["faqGroupList"] =
            when (category.isNullOrEmpty()) {
                true -> faqRepository.getAllFaqGroupList()
                false -> faqRepository.getFaqGroupList(category?.get(0).toString())
            }
        faqInfo["faqList"] = faqRepository.findAll()
        return faqInfo
    }

    fun getFaq(faqId: String): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        // faq content
        val faqContent = mutableListOf<FaqListDto>()
        val selectedFaq = faqRepository.findByIdOrNull(faqId)
        selectedFaq?.let { faqContent.add(faqMapper.toFaqListDto(it)) }
        faqInfo["faqContent"] = faqContent

        return faqInfo
    }
}
