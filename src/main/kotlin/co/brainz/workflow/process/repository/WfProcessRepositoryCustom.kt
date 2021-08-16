package co.brainz.workflow.process.repository

import co.brainz.itsm.process.dto.ProcessSearchCondition
import co.brainz.workflow.process.entity.WfProcessEntity
import co.brainz.workflow.provider.dto.ProcessListReturnDto
import com.querydsl.core.QueryResults

interface WfProcessRepositoryCustom {

    fun findProcessEntityList(processSearchCondition: ProcessSearchCondition): ProcessListReturnDto

    fun findProcessDocumentExist(processId: String): Boolean
}
