package co.brainz.itsm.portal.service

import co.brainz.itsm.notice.repository.NoticeRepository
import co.brainz.itsm.portal.dto.PortalDto
import co.brainz.itsm.portal.dto.PortalSearchDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PortalService(private val noticeRepository: NoticeRepository) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * 포탈 리스트 , 포탈 검색 리스트
     */
    fun findPortalListOrSearchList(portalSearchDto: PortalSearchDto): MutableList<PortalDto> {
        return noticeRepository.findPortalListOrSearchList(portalSearchDto.searchValue)
    }
}
