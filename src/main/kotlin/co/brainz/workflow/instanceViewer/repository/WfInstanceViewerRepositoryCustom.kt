/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instanceViewer.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.instanceViewer.dto.WfInstanceViewerReviewDto

interface WfInstanceViewerRepositoryCustom : AliceRepositoryCustom {

    fun existsByViewerKey(instanceId: String, userKey: String): Boolean

    fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerReviewDto?

}
