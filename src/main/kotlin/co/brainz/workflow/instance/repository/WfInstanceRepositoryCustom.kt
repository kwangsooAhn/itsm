package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import com.querydsl.core.QueryResults

interface WfInstanceRepositoryCustom {

    fun findTodoInstances(
        status: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        fromDt: String,
        toDt: String,
        dateFormat: String,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findRequestedInstances(
        userKey: String,
        documentId: String,
        searchValue: String,
        fromDt: String,
        toDt: String,
        dateFormat: String,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findRelationInstances(
        status: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        fromDt: String,
        toDt: String,
        dateFormat: String,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>
}
