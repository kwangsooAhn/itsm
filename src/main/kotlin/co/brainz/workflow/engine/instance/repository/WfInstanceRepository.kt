package co.brainz.workflow.engine.instance.repository

import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto
import co.brainz.workflow.engine.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WfInstanceRepository : JpaRepository<WfInstanceEntity, String> {

    fun findByInstanceId(instanceId: String): WfInstanceEntity?

    @Query(
        "SELECT NEW co.brainz.workflow.engine.instance.dto.WfInstanceListViewDto(t, d, i) " +
                "FROM WfTokenEntity t, WfDocumentEntity d, WfInstanceEntity i " +
                "where d.documentId = i.document.documentId " +
                "and i.instanceId = t.instance.instanceId " +
                "and t.tokenStatus = :status " +
                "and t.assigneeId = :userKey"
    )
    fun findInstances(status: String, userKey: String): List<WfInstanceListViewDto>

    fun countByDocument(wfDocumentEntity: WfDocumentEntity): Int

    @Query(
        "select i.instanceStatus as instanceStatus, count(i.instanceStatus) as instanceCount " +
                "from WfInstanceEntity i " +
                "where i.instanceCreateUser.userKey = :userKey " +
                "group by i.instanceStatus " +
                "order by " +
                "case " +
                "   when i.instanceStatus = 'running' then 1  " +
                "   when i.instanceStatus = 'wait' then 2 " +
                "   when i.instanceStatus = 'finish' then 3 " +
                "end"
    )
    fun findInstancesCount(userKey: String): List<Map<String, Any>>

    @Query(
        "SELECT NEW co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto(" +
                "t.tokenStartDt, t.tokenEndDt, e.elementName, e.elementType, t.assigneeId, t.assigneeId) FROM WfTokenEntity t, WfElementEntity e " +
                "WHERE t.instance.instanceId = :instanceId " +
                "AND t.element = e " +
                "ORDER BY t.tokenStartDt"
    )
    fun findInstanceHistory(instanceId: String): List<RestTemplateInstanceHistoryDto>

    @Query(
        "SELECT NEW co.brainz.workflow.provider.dto.RestTemplateInstanceListDto(" +
                "i.instanceId, d.documentName, i.documentNo, i.instanceStartDt , i.instanceEndDt, i.instanceCreateUser.userKey, i.instanceCreateUser.userName) from WfDocumentEntity d, WfInstanceEntity i left join i.instanceCreateUser  " +
                "WHERE d.documentId = i.document.documentId " +
                "AND i.instanceId != :instanceId " +
                "AND (lower(i.document.documentName) like lower(concat('%', :searchValue, '%')) " +
                "or lower(i.instanceCreateUser.userName) like lower(concat('%', :searchValue, '%')) " +
                "or :searchValue is null or :searchValue = '') " +
                "ORDER BY i.instanceStartDt"
    )
    fun findAllInstanceListAndSearch(instanceId: String, searchValue: String): MutableList<RestTemplateInstanceListDto>
}
