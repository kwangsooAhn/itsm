package co.brainz.itsm.faq.repository

import co.brainz.itsm.faq.entity.FaqEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

/**
 * ### FAQ용 Repository 클래스.
 *
 * @author Jung heechan
 */
@Repository
interface FaqRepository : JpaRepository<FaqEntity, String>, FaqRepositoryCustom {

    @Query("select distinct(f.faqGroup) as faqGroup from FaqEntity f order by f.faqGroup desc")
    fun getAllFaqGroupList(): List<String>

    @Query(
        "select count(f.faqId) as count from FaqEntity f " +
                "where f.faqTitle = :faqTitle and  f.faqGroup = :faqGroup"
    )
    fun getCountDuplicateFaqTitleAndCategory(faqTitle: String, faqGroup: String): Int
}
