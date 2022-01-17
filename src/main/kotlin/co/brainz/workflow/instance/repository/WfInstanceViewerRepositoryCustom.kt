/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.instance.repository

interface WfInstanceViewerRepositoryCustom {

    fun existsByViewerKey(instanceId: String, userKey: String): Boolean

}
