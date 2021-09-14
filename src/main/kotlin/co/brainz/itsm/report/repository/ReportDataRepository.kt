/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.entity.ReportDataEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReportDataRepository : JpaRepository<ReportDataEntity, String>, ReportDataRepositoryCustom
