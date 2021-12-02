package co.brainz.workflow.token.repository

import co.brainz.itsm.chart.dto.average.ChartTokenData
import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto

interface WfTokenDataRepositoryCustom {
    fun findTokenDataByTokenIds(tokenIds: Set<String>): List<WfInstanceListTokenDataDto>

    fun getTokenDataList(componentIds: Set<String>, tokenIds: Set<String>, componentTypeSet: Set<String>): List<ChartTokenData>
}
