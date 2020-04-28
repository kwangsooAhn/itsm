package co.brainz.framework.numbering.repository

import co.brainz.framework.numbering.entity.AliceNumberingRuleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceNumberingRuleRepository : JpaRepository<AliceNumberingRuleEntity, String> {

}
