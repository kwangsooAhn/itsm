/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.repository

import co.brainz.itsm.plugin.entity.PluginEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class PluginRepositoryImpl : QuerydslRepositorySupport(
    PluginEntity::class.java
), PluginRepositoryCustom {
}
