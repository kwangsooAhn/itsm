/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.repository

import co.brainz.itsm.plugin.dto.PluginListDto
import co.brainz.itsm.plugin.dto.PluginSearchCondition
import co.brainz.itsm.plugin.entity.PluginEntity
import co.brainz.itsm.plugin.entity.QPluginEntity
import com.querydsl.core.QueryResults
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class PluginRepositoryImpl : QuerydslRepositorySupport(PluginEntity::class.java), PluginRepositoryCustom {
    override fun getPlugins(pluginSearchCondition: PluginSearchCondition): QueryResults<PluginListDto> {
        val plugin = QPluginEntity.pluginEntity
        val query = from(plugin)
            .select(
                Projections.constructor(
                    PluginListDto::class.java,
                    plugin.pluginId,
                    plugin.pluginName,
                    plugin.pluginType,
                    plugin.pluginLocation,
                    plugin.pluginCommand
                )
            )
            .where(super.likeIgnoreCase(plugin.pluginName, pluginSearchCondition.searchValue))
            .orderBy(plugin.pluginName.asc())
        if (pluginSearchCondition.isPaging) {
            query.limit(pluginSearchCondition.contentNumPerPage)
            query.offset((pluginSearchCondition.pageNum - 1) * pluginSearchCondition.contentNumPerPage)
        }
        return query.fetchResults()
    }
}
