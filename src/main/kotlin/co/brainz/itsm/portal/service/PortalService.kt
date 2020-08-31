package co.brainz.itsm.portal.service

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
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    fun findPortalListOrSearchList(portalSearchDto: PortalSearchDto, pageableValue: Pageable): MutableList<PortalDto> {

        var pageable = pageableValue
        val page = if (pageable.pageNumber == 0) 0 else pageable.pageNumber
        pageable = PageRequest.of(page, 10)

        return noticeRepository.findPortalListOrSearchList(portalSearchDto.searchValue, pageable)
    }

    fun findTotalCount(portalSearchDto: PortalSearchDto): Int {
        return noticeRepository.findPortalListOrSearchList(portalSearchDto.searchValue, null).size
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
            // noticeTopList.add(noticeMapper.toNoticeListDto(it))
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

    fun getFaqList(faqSearchRequestDto: FaqSearchRequestDto): LinkedHashMap<String, Any> {
        val faqInfo = LinkedHashMap<String, Any>()
        faqInfo["faqList"] = faqRepository.findFaqs(faqSearchRequestDto)
        faqInfo["faqGroupList"] = faqRepository.getAllFaqGroupList()
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
