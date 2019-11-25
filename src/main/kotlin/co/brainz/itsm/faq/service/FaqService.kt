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
}