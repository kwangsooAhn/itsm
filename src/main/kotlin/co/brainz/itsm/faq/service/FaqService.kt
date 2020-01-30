package co.brainz.itsm.faq.service

import co.brainz.framework.fileTransaction.dto.FileDto
import co.brainz.framework.fileTransaction.service.FileService
import co.brainz.itsm.faq.dto.FaqDto
import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.repository.FaqRepository
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
class FaqService(private val faqRepository: FaqRepository, private val fileService: FileService) {

    /**
     * FAQ 전체 데이터 조회
     */
    fun findAll(): List<FaqEntity> {
        return faqRepository.findAll()
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
        fileService.upload(FileDto(savedFaqEntity.faqId, faqDto.fileSeq))
    }

    /**
     * FAQ 삭제
     */
    @Transactional
    fun delete(faqId: String) {
        faqRepository.deleteById(faqId)
        fileService.delete(faqId)
    }
}
