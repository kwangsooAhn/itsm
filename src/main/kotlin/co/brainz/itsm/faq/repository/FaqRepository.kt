package co.brainz.itsm.faq.repository

import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import java.time.LocalDateTime
import co.brainz.itsm.faq.entity.FaqEntity

@Repository
public interface FaqRepository : JpaRepository<FaqEntity, String> {
 
    //override fun findAll(): List<FaqEntity>
    //fun findOne(faqId: String): FaqEntity
}