/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.instance.entity.WfInstanceViewerEntity

interface ViewerRepositoryCustom : AliceRepositoryCustom {
    fun findByInstanceViewerList(instanceId: String): List<WfInstanceViewerEntity>?

    fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerEntity?

    fun findViewerByInstanceId(instanceId: String): MutableList<WfInstanceViewerEntity>

    fun findByInstanceIdAndViewerKey(instanceId: String, viewerKey: String): WfInstanceViewerEntity?
}
