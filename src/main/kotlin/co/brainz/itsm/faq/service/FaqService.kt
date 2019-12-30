package co.brainz.itsm.faq.service

import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.repository.FaqRepository
import org.springframework.stereotype.Service

/**
 * ### FAQ 관련 서비스 레이어 클래스.
 *
 * FAQ관련 데이터 조회/등록/변경/삭제를 처리한다.
 *
 * @author Jung heechan
 */
@Service
public open class FaqService(private val faqRepository: FaqRepository) {

    /**
     * FAQ 전체 데이터 조회
     */
    public fun findAll(): List<FaqEntity> {
        return faqRepository.findAll()
    }

    /**
     * FAQ 데이터 상세 조회
     */
    public fun findOne(faqId: String): FaqEntity {
        return faqRepository.findById(faqId)
                            .orElse(null)
    }

    /**
     * FAQ 등록/변경
     */
    public fun save(faq: FaqEntity) {
        faqRepository.save(faq)
    }

    /**
     * FAQ 삭제
     */
    public fun delete(faqId: String) {
        faqRepository.deleteById(faqId)
    }
}
