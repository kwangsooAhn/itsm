package co.brainz.workflow.process.repository

import co.brainz.framework.querydsl.dto.PagingReturnDto
import co.brainz.itsm.process.dto.ProcessSearchCondition

interface WfProcessRepositoryCustom {

    fun findProcessEntityList(processSearchCondition: ProcessSearchCondition): PagingReturnDto

    fun findProcessDocumentExist(processId: String): Boolean
}
