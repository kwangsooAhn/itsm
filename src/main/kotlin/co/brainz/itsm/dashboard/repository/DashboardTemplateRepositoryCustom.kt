/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.instance.dto.ViewerListDto
import co.brainz.workflow.document.entity.WfDocumentEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity

interface DashboardTemplateRepositoryCustom : AliceRepositoryCustom {
    fun countByOrganizationRunningDocument(document: WfDocumentEntity, organization: OrganizationEntity): Long
    fun countByUserRunningDocument(document: WfDocumentEntity, userKey: String): Long
    fun organizationRunningDocument(documentIds: Set<String>, organizationId: String): List<WfInstanceEntity>
    fun test(): List<ViewerListDto>
}
