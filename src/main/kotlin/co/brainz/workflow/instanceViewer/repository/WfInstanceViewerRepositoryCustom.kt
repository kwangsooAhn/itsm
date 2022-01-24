/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instanceViewer.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import com.querydsl.core.QueryResults

interface WfInstanceViewerRepositoryCustom : AliceRepositoryCustom {

    fun findByInstanceViewerList(instanceId: String): QueryResults<WfInstanceViewerEntity>?

    fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerEntity?

    fun findByInstanceIdAndViewerKey(instanceId: String, viewerKey: String): WfInstanceViewerEntity?

}
