/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.repository

import co.brainz.itsm.plugin.dto.PluginListDto
import co.brainz.itsm.plugin.entity.PluginEntity
import co.brainz.itsm.plugin.entity.QPluginEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class PluginRepositoryImpl : QuerydslRepositorySupport(PluginEntity::class.java), PluginRepositoryCustom {
    override fun getPlugins(): List<PluginListDto> {
        val plugin = QPluginEntity.pluginEntity
        return from(plugin)
            .select(
                Projections.constructor(
                    PluginListDto::class.java,
                    plugin.pluginId,
                    plugin.pluginName
                )
            )
            .orderBy(plugin.pluginName.asc())
            .fetch()
    }
}
