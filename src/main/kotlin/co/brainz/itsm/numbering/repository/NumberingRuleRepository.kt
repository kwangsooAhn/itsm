package co.brainz.itsm.numbering.repository

import co.brainz.itsm.numbering.entity.NumberingRuleEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NumberingRuleRepository: JpaRepository<NumberingRuleEntity, String> {

}
