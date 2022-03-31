/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.component.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom

interface WfComponentPropertyRepositoryCustom : AliceRepositoryCustom {
    fun findComponentTypeAndProperty(compoType: String, compoProperty: String): List<String>
}
