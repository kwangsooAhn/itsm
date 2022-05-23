/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.documentStorage.repository

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.documentStorage.entity.DocumentStorageEntity
import co.brainz.itsm.documentStorage.entity.DocumentStoragePk
import co.brainz.workflow.instance.entity.WfInstanceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DocumentStorageRepository : JpaRepository<DocumentStorageEntity, DocumentStoragePk> {
    fun existsByInstanceAndUser(instance: WfInstanceEntity, user: AliceUserEntity): Boolean
}
