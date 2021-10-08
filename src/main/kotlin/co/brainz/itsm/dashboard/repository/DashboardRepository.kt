/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.dashboard.repository

import co.brainz.workflow.instance.entity.WfInstanceEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DashboardRepository : JpaRepository<WfInstanceEntity, String>, DashboardRepositoryCustom
