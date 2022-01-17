/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instance.repository

import co.brainz.workflow.instance.entity.WfInstanceViewerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface WfInstanceViewerRepository : JpaRepository<WfInstanceViewerEntity, String>, WfInstanceViewerRepositoryCustom {
}
