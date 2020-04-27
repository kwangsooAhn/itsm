package co.brainz.itsm.code.repository

import co.brainz.itsm.code.dto.CodeDtoHeechan
import co.brainz.itsm.code.entity.CodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CodeRepository : JpaRepository<CodeEntity, String> {
    fun findByPCode(pCode: CodeEntity): MutableList<CodeEntity>

    @Query(
        "SELECT NEW co.brainz.itsm.code.dto.CodeDtoHeechan(" +
                "c.code, c.pCode.code, c.codeValue, c.editable, c.createDt, c.createUser.userKey, c.updateDt, c.updateUser.userKey) FROM CodeEntity c " +
                "WHERE c.code = :code "
    )
    fun findTest(code: String): CodeDtoHeechan
}
