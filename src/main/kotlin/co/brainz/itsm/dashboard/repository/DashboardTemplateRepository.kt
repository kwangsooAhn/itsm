/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.itsm.dashboard.entity.DashboardTemplateEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DashboardTemplateRepository : JpaRepository<DashboardTemplateEntity, String>, DashboardTemplateRepositoryCustom
