/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.report.repository

import co.brainz.itsm.report.entity.ReportDataEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class ReportDataRepositoryImpl : QuerydslRepositorySupport(ReportDataEntity::class.java), ReportDataRepositoryCustom
