/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.repository

import co.brainz.itsm.documentStorage.entity.DocumentStorageEntity
import co.brainz.itsm.documentStorage.entity.DocumentStoragePk
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentStorageRepository : JpaRepository<DocumentStorageEntity, DocumentStoragePk> {
}
