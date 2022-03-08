/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.repository

import co.brainz.itsm.plugin.entity.PluginHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository

@Repository
interface PluginHistoryRepository : JpaRepository<PluginHistoryEntity, String>,
    JpaSpecificationExecutor<PluginHistoryEntity>, PluginHistoryRepositoryCustom
