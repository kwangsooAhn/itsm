/*
 * Copyright 2022 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.instance.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.itsm.instance.entity.WfInstanceViewerEntity
import com.querydsl.core.QueryResults

interface ViewerRepositoryCustom : AliceRepositoryCustom {
    fun findByInstanceViewerList(instanceId: String): List<WfInstanceViewerEntity>?

    fun getReviewYnByViewKey(instanceId: String, userKey: String): WfInstanceViewerEntity?

    fun findViewerByInstanceId(instanceId: String): MutableList<WfInstanceViewerEntity>

    fun updateDisplayYn(instanceId: String, viewerKey: String)

    fun findByInstanceIdAndViewerKey(instanceId: String, viewerKey: String): WfInstanceViewerEntity?

    fun updateReviewYn(instanceId: String, viewerKey: String): Int

}
