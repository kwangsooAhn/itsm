package co.brainz.itsm.faq.service

import co.brainz.itsm.faq.dto.FaqDto
import co.brainz.itsm.faq.dto.FaqListDto
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.mapper.FaqMapper
import co.brainz.itsm.faq.repository.FaqRepository
import org.mapstruct.factory.Mappers
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
class FaqService(private val faqRepository: FaqRepository) {

    val faqMapper: FaqMapper = Mappers.getMapper(FaqMapper::class.java)

    /**
     * FAQ 전체 데이터 조회
     */
    fun findAll(): List<FaqListDto> {
        val faqEntities = faqRepository.getFaqList()
        val faqList: MutableList<FaqListDto> = mutableListOf()
        faqEntities.forEach {
            faqList.add(faqMapper.toFaqListDto(it))
        }
        return faqList
    }

    /**
     * FAQ 전체 카테고리 조회
     */
    fun findAllFaqGroups(): List<String> {
        return faqRepository.getAllFaqGroupList()
    }

    /**
     * FAQ 카테고리 조회
     */
    fun findFaqGroups(faqGroup: String): List<String> {
        return faqRepository.getFaqGroupList(faqGroup)
    }

    /**
     * FAQ 데이터 상세 조회
     */
    fun findOne(faqId: String): FaqEntity {
        return faqRepository.findById(faqId).orElse(null)
    }

    /**
     * FAQ 등록/변경
     */
    @Transactional
    fun save(faqDto: FaqDto) {
        val faqEntity = if (faqDto.faqId.isNotBlank()) {
            faqRepository.getOne(faqDto.faqId)
        } else {
            FaqEntity(
                faqDto.faqId,
                faqDto.faqGroup,
                faqDto.faqTitle,
                faqDto.faqContent
            )
        }
        faqEntity.faqGroup = faqDto.faqGroup
        faqEntity.faqTitle = faqDto.faqTitle
        faqEntity.faqContent = faqDto.faqContent

        val savedFaqEntity = faqRepository.save(faqEntity)
    }

    /**
     * FAQ 삭제
     */
    @Transactional
    fun delete(faqId: String) {
        faqRepository.deleteById(faqId)
    }
}
