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
interface FaqRepository : FaqRepositoryCustom, JpaRepository<FaqEntity, String> {

    @Query("select f from FaqEntity f join fetch f.createUser left outer join fetch f.updateUser order by f.faqGroup desc  ")
    fun getFaqList(): MutableList<FaqEntity>

    @Query("select distinct(f.faqGroup) as faqGroup from FaqEntity f order by f.faqGroup desc")
    fun getAllFaqGroupList(): List<String>

    @Query("select distinct(f.faqGroup) as faqGroup from FaqEntity f where f.faqGroup = :faqGroup")
    fun getFaqGroupList(faqGroup: String): List<String>
}
