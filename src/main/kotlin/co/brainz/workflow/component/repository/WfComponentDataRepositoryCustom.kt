/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.workflow.component.repository

import co.brainz.framework.querydsl.AliceRepositoryCustom
import co.brainz.workflow.provider.dto.RestTemplateFormComponentDataDto

interface WfComponentDataRepositoryCustom : AliceRepositoryCustom {

    /**
     * 커스텀 코드 타입의 display 데이터 정보를 리턴.
     */
    fun findComponentTypeAndAttributeId(
        componentType: String?,
        attributeId: String?
    ): List<RestTemplateFormComponentDataDto>
}
