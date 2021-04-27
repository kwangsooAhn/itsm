package co.brainz.itsm.code.repository

import co.brainz.itsm.code.entity.CodeLangEntity
import co.brainz.itsm.code.entity.CodeLangEntityPk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CodeLangRepository : JpaRepository<CodeLangEntity, CodeLangEntityPk>