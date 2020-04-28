package co.brainz.framework.numbering.repository

import co.brainz.framework.numbering.entity.AliceNumberingPatternEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceNumberingPatternRepository : JpaRepository<AliceNumberingPatternEntity, String> {

}
