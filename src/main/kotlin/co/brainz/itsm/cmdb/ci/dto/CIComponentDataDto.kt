/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ci.dto

import java.io.Serializable

/**
 * CI 컴포넌트 - CI 임시데이터 DTO.
 *
 * @author Woo Da jung
 * @since 2021-02-02
 */
data class CIComponentDataDto(
    val ciId: String = "",
    val componentId: String = "",
    val values: String,
    val instanceId: String? = null
) : Serializable
