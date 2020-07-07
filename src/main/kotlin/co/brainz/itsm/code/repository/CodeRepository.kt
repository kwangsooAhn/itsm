package co.brainz.itsm.code.repository

import co.brainz.itsm.code.dto.CodeDetailDto
import co.brainz.itsm.code.entity.CodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CodeRepository : JpaRepository<CodeEntity, String> {
    fun findByPCodeOrderByCode(pCode: CodeEntity): MutableList<CodeEntity>

    /**
     * 코드 데이터 상세 정보 조회
     */
    @Query(
        "SELECT NEW co.brainz.itsm.code.dto.CodeDetailDto(c.code, c.pCode.code, c.codeValue, c.editable, c.createDt, " +
                "c.createUser.userKey, c.updateDt, c.updateUser.userKey, true) FROM CodeEntity c " +
                "WHERE c.code = :code "
    )
    fun findCodeDetail(code: String): CodeDetailDto

    fun existsByCodeAndEditableTrue(code: String) : Boolean

    fun existsByPCodeAndEditableTrue(pCode: CodeEntity): Boolean

    fun existsByCodeAndPCodeAndEditableTrue(code: String, pCode: CodeEntity = CodeEntity()): Boolean

    fun findByPCodeAndEditableTrueOrderByCodeValue(pCode: CodeEntity): MutableList<CodeEntity>

}
