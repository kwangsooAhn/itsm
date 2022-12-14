/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */
package co.brainz.workflow.process.repository

import co.brainz.workflow.process.entity.WfProcessEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WfProcessRepository : JpaRepository<WfProcessEntity, String>,
    WfProcessRepositoryCustom {

    fun findByProcessId(processId: String): WfProcessEntity?
    fun countByProcessName(processName: String): Long
    fun existsByProcessName(processName: String): Boolean
}
