package co.brainz.workflow.instance.repository

import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface WfInstanceRepository : JpaRepository<WfInstanceEntity, String>, WfInstanceRepositoryCustom {

    fun findByInstanceId(instanceId: String): WfInstanceEntity?
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
        "SELECT NEW co.brainz.workflow.provider.dto.RestTemplateInstanceListDto(" +
                "i.instanceId, i.document.documentName, i.documentNo, i.instanceStartDt , i.instanceEndDt, i.instanceCreateUser.userKey, i.instanceCreateUser.userName) from WfInstanceEntity i left join i.instanceCreateUser inner join i.document " +
                "WHERE i.instanceId != :instanceId " +
                "AND (lower(i.document.documentName) like lower(concat('%', :searchValue, '%')) " +
                "or lower(i.instanceCreateUser.userName) like lower(concat('%', :searchValue, '%')) " +
                "or :searchValue = '') " +
                "ORDER BY i.instanceStartDt"
    )
    fun findAllInstanceListAndSearch(instanceId: String, searchValue: String): MutableList<RestTemplateInstanceListDto>
}