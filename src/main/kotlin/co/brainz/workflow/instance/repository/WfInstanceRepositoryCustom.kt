/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instance.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.statistic.customChart.dto.ChartRange
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto

interface WfInstanceRepositoryCustom {

    fun findTodoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): PagingReturnDto

    fun findRequestedInstances(tokenSearchCondition: TokenSearchCondition): PagingReturnDto

    fun findRelationInstances(
        status: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): PagingReturnDto

    fun findStoredInstances(
        tokenSearchCondition: TokenSearchCondition
    ): PagingReturnDto

    fun findInstanceHistory(instanceId: String): MutableList<RestTemplateInstanceHistoryDto>

    fun deleteInstances(instances: MutableList<WfInstanceEntity>)

    fun findAllInstanceListByRelatedCheck(
        instanceId: String,
        searchValue: String
    ): MutableList<RestTemplateInstanceListDto>

    fun getInstanceListInTag(tagValue: String, range: ChartRange, documentStatus: String?): List<WfInstanceEntity>

    fun findTodoInstanceCount(
        status: List<String>?,
        tokenStatus: List<String>?,
        tokenSearchCondition: TokenSearchCondition
    ): Long
}
