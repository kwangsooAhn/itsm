/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.portal.dto

import java.io.Serializable

data class PortalTopListDto(
    val notice: List<PortalTopDto>,
    val faq: List<PortalTopDto>,
    val archive: List<PortalTopDto>
): Serializable
