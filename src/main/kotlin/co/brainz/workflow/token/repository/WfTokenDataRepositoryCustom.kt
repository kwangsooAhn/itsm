/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.token.repository

import co.brainz.itsm.report.chart.dto.average.ChartTokenData
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto

interface WfTokenDataRepositoryCustom {
    fun findTokenDataByTokenIds(tokenIds: Set<String>): List<WfInstanceListTokenDataDto>

    fun getTokenDataList(componentIds: Set<String>, tokenIds: Set<String>, componentTypeSet: Set<String>): List<co.brainz.itsm.report.chart.dto.average.ChartTokenData>
}
