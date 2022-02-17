/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom

interface DashboardTemplateRepositoryCustom : AliceRepositoryCustom {
    fun countRunningDocument(document: String, organizationId: String, status: String): Long
    fun countRunningDocumentByUserKey(document: String, userKey: String, status: String): Long
}
