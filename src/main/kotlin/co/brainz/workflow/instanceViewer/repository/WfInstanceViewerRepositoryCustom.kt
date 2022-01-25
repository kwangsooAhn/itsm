/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instanceViewer.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity

interface WfInstanceViewerRepositoryCustom : AliceRepositoryCustom {

    fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerEntity?

    fun findViewerByInstanceId(instanceId: String): MutableList<WfInstanceViewerEntity>

}
