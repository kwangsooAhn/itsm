/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instanceViewer.repository

import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import javax.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface WfInstanceViewerRepository : JpaRepository<WfInstanceViewerEntity, String>,
    WfInstanceViewerRepositoryCustom {
}
