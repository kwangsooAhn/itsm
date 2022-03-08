/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.plugin.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.plugin.dto.PluginListDto

interface PluginRepositoryCustom : AliceRepositoryCustom {
    fun getPlugins(): List<PluginListDto>
}
