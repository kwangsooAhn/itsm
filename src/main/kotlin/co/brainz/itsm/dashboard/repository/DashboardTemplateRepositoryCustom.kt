/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.document.entity.WfDocumentEntity

interface DashboardTemplateRepositoryCustom : AliceRepositoryCustom {
    fun countByOrganizationRunningDocument(document: WfDocumentEntity, organization: OrganizationEntity): Long
    fun countByUserRunningDocument(document: WfDocumentEntity, userKey: String): Long
}
