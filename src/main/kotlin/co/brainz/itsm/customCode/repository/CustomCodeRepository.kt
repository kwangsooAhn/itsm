/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.customCode.repository

import co.brainz.itsm.customCode.entity.CustomCodeEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomCodeRepository : JpaRepository<CustomCodeEntity, String>, CustomCodeRepositoryCustom {
    fun existsByCustomCodeName(customCodeName: String): Boolean
    fun existsByPCode(pCode: String): Boolean
}
