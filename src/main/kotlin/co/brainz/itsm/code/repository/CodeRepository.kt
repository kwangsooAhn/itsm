package co.brainz.itsm.code.repository

import co.brainz.itsm.code.entity.CodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CodeRepository : JpaRepository<CodeEntity, String> {
    fun findByPCode(pCode: String): MutableList<CodeEntity>
}
