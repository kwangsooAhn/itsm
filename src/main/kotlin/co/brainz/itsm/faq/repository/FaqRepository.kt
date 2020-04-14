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
interface FaqRepository : JpaRepository<FaqEntity, String> {

    @Query("select f from FaqEntity f join fetch f.createUser left outer join fetch f.updateUser")
    fun getFaqList(): MutableList<FaqEntity>
    
    @Query("select f from FaqEntity f join fetch f.createUser left outer join fetch f.updateUser where (lower(f.faqTitle)) like lower(concat('%',:searchValue,'%'))")
    fun getFaqSearchList(searchValue: String): MutableList<FaqEntity>
}
