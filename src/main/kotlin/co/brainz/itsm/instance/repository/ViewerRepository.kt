/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.repository

import co.brainz.itsm.instance.entity.WfInstanceViewerEntity
import co.brainz.workflow.instance.entity.WfInstanceEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ViewerRepository : JpaRepository<WfInstanceViewerEntity, String>, ViewerRepositoryCustom {
    fun deleteWfInstanceViewerEntityByInstanceAndAndReviewYn(instance: WfInstanceEntity, ReviewYn: Boolean)
}
