package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import com.querydsl.core.QueryResults
import java.time.LocalDateTime

interface WfInstanceRepositoryCustom {

    fun findTodoInstances(
        status: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findRequestedInstances(
        userKey: String,
        documentId: String,
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findRelationInstances(
        status: List<String>?,
        userKey: String,
        documentId: String,
        searchValue: String,
        fromDt: LocalDateTime,
        toDt: LocalDateTime,
        offset: Long
    ): QueryResults<WfInstanceListViewDto>

    fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>
}
