/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.service

import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import org.springframework.stereotype.Service

@Service
class DashboardService(
    private val wfInstanceService: WfInstanceService
) {

    /**
     * 신청한 문서 현황 count 조회
     *
     * @return
     */
    fun getStatusCountList(params: LinkedHashMap<String, Any>): List<RestTemplateInstanceCountDto> {
        return wfInstanceService.instancesStatusCount(params)
    }
}
