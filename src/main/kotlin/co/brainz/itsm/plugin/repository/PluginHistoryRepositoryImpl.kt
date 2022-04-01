/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.repository

import co.brainz.itsm.plugin.entity.PluginHistoryEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class PluginHistoryRepositoryImpl : QuerydslRepositorySupport(PluginHistoryEntity::class.java), PluginHistoryRepositoryCustom
