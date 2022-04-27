/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.service

import co.brainz.itsm.plugin.entity.PluginHistoryEntity
import co.brainz.itsm.plugin.repository.PluginHistoryRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PluginHistoryService(
    val pluginHistoryRepository: PluginHistoryRepository
) {

    @Value("\${plugins.dir}")
    val pluginsDir: String? = null

    fun insertPluginHistory(pluginHistoryEntity: PluginHistoryEntity): PluginHistoryEntity {
        return pluginHistoryRepository.save(pluginHistoryEntity)
    }

    fun getPluginDir(): String? {
        return this.pluginsDir
    }
}
