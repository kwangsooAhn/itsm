package co.brainz.itsm.code.repository

import co.brainz.itsm.code.entity.CodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CodeRepository : JpaRepository<CodeEntity, String>, CodeRepositoryCustom {
    fun existsByCode(pCode: String): Boolean

    fun existsByCodeAndEditableTrue(code: String): Boolean

    fun existsByPCodeAndEditableTrue(pCode: CodeEntity): Boolean

    fun existsByPCode(pCode: CodeEntity = CodeEntity()): Boolean

    fun findByPCodeAndEditableTrueOrderByCodeValue(pCode: CodeEntity): MutableList<CodeEntity>
}
