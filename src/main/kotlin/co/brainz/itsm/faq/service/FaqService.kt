package co.brainz.itsm.faq.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import co.brainz.itsm.faq.repository.FaqRepository
import co.brainz.itsm.faq.entity.FaqEntity

@Service
public open class FaqService {
    
    @Autowired
    lateinit var faqRepository : FaqRepository
    
    public fun findAll() : List<FaqEntity> {
        return faqRepository.findAll()
    }
    
    public fun findOne(faqId:String) : FaqEntity {
        return faqRepository.findById(faqId)
                            .orElse(null)
    }

    public fun save(faq:FaqEntity) {
        faqRepository.save(faq)
    }

    public fun delete(faqId:String) {
        faqRepository.deleteById(faqId)
    }

}