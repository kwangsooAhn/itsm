package co.brainz.itsm.numbering.repository

import co.brainz.itsm.numbering.entity.NumberingPatternEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NumberingPatternRepository: JpaRepository<NumberingPatternEntity, String> {
    
}
