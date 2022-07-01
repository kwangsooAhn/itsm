/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.cmdb.ciIcon.repository

import co.brainz.itsm.cmdb.ciIcon.entity.CIIconEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CIIconRepository : JpaRepository<CIIconEntity, Long> {
    /**
     * 아이콘 파일명 조회
     */
    fun findByFileNameAndFileNameExtension(fileName: String, fileNameExtension: String): CIIconEntity
}
