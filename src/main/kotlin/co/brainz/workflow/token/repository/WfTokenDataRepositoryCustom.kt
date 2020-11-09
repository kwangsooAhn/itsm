package co.brainz.workflow.token.repository

import co.brainz.workflow.instance.dto.WfInstanceListTokenDataDto

interface WfTokenDataRepositoryCustom {
    fun findTokenDataByTokenIds(tokenIds: Set<String>): List<WfInstanceListTokenDataDto>
}
