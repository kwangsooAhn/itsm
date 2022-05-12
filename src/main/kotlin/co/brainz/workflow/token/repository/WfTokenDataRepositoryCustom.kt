/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.token.repository

import co.brainz.itsm.statistic.customChart.dto.average.ChartTokenData
import co.brainz.workflow.engine.manager.dto.WfTokenDataDto
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto

interface WfTokenDataRepositoryCustom {
    fun findTokenDataByTokenIds(tokenIds: Set<String>, componentTypes: ArrayList<String>): List<WfInstanceListTokenDataDto>

    fun getTokenDataList(componentIds: Set<String>, tokenIds: Set<String>, componentTypeSet: Set<String>): List<ChartTokenData>

    fun getTokenDataList(tokenId: String): List<WfTokenDataDto>
}
