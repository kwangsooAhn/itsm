package co.brainz.itsm.faq.repository

import co.brainz.itsm.faq.entity.FaqEntity
import co.brainz.itsm.faq.entity.QFaqEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class FaqRepositoryImpl : QuerydslRepositorySupport(FaqEntity::class.java), FaqRepositoryCustom {

    override fun findFaqTopList(limit: Long): List<FaqEntity> {
        val faq = QFaqEntity.faqEntity

        return from(faq).distinct()
            .orderBy(faq.createDt.desc())
            .limit(limit)
            .fetch()
    }
}
