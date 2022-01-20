package co.brainz.workflow.instanceViewer.repository

import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity
import co.brainz.framework.querydsl.AliceRepositoryCustom

interface WfInstanceViewerRepositoryCustom : AliceRepositoryCustom {

    fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerEntity?
}