package co.brainz.itsm.portal.service

import co.brainz.itsm.faq.repository.FaqRepository
import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import co.brainz.itsm.portal.mapper.PortalMapper
import org.mapstruct.factory.Mappers
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PortalService(private val noticeRepository: NoticeRepository,
                    private val faqRepository: FaqRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)
    private val portalMapper: PortalMapper = Mappers.getMapper(PortalMapper::class.java)

    /**
     * 포탈 리스트 , 포탈 검색 리스트
     */
    fun findPortalListOrSearchList(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        val portalDto = mutableListOf<PortalDto>()

        when (portalSearchDto.isSearch) {
            true -> {
                noticeRepository.findPortalNoticeSearchList(portalSearchDto.searchValue).forEach {
                    portalDto.add(portalMapper.toPortalNoticeListDto(it))
                }
                faqRepository.getFaqSearchList(portalSearchDto.searchValue).forEach {
                    portalDto.add(portalMapper.toPortalFaqListDto(it))
                }
            }
            false -> {
                noticeRepository.findAllByOrderByCreateDtDesc().forEach {
                    portalDto.add(portalMapper.toPortalNoticeListDto(it))
                }
                faqRepository.getFaqList().forEach {
                    portalDto.add(portalMapper.toPortalFaqListDto(it))
                }
            }
        }
        return portalDto
    }
}