/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instance.repository

import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import com.querydsl.core.QueryResults
import org.springframework.data.domain.Page

interface WfInstanceRepositoryCustom {

    fun findTodoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): Page<WfInstanceListViewDto>

    fun findRequestedInstances(tokenSearchCondition: TokenSearchCondition): Page<WfInstanceListViewDto>

    fun findRelationInstances(
        status: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): Page<WfInstanceListViewDto>

    fun findInstanceHistory(instanceId: String): MutableList<RestTemplateInstanceHistoryDto>

    fun deleteInstances(instances: MutableList<WfInstanceEntity>)

    fun findAllInstanceListByRelatedCheck(
        instanceId: String,
        searchValue: String
    ): MutableList<RestTemplateInstanceListDto>

    fun getInstanceListInTag(tagValue: String, range: ChartRange, documentStatus: String?): List<WfInstanceEntity>
}
