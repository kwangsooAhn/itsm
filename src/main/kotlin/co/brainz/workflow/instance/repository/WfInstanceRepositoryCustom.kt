/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import com.querydsl.core.QueryResults
import java.time.LocalDateTime

interface WfInstanceRepositoryCustom {

    fun findTodoInstances(
        status: List<String>?,
        tokenStatus: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        tags: Set<String>,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findRequestedInstances(
        userKey: String,
        documentId: String,
        searchValue: String,
        tags: Set<String>,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findRelationInstances(
        status: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        tags: Set<String>,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findInstanceHistory(instanceId: String): MutableList<RestTemplateInstanceHistoryDto>

    fun deleteInstances(instances: MutableList<WfInstanceEntity>)

    fun findAllInstanceListAndSearch(instanceId: String, searchValue: String): MutableList<RestTemplateInstanceListDto>
}
