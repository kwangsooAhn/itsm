/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.statistic.customReport.repository

import co.brainz.itsm.statistic.customReport.entity.CustomReportDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomReportDataRepository : JpaRepository<CustomReportDataEntity, String>, CustomReportDataRepositoryCustom
