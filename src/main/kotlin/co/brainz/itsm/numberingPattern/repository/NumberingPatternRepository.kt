package co.brainz.itsm.numberingPattern.repository

import co.brainz.itsm.numberingPattern.entity.NumberingPatternEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NumberingPatternRepository : JpaRepository<NumberingPatternEntity, String>, NumberingPatternRepositoryCustom {
    fun existsByPatternName(patternName: String): Boolean
}
