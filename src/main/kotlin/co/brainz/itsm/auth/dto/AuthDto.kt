/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.auth.dto

import co.brainz.framework.validator.CheckUnacceptableCharInUrl

/**
 * 권한 조회시 사용한다.
 */
data class AuthDto(
    @CheckUnacceptableCharInUrl
    var authId: String?,
    var authName: String?,
    var authDesc: String?,
    var arrMenuId: List<String>?,
    var arrMenuList: MutableList<AuthMenuDto>?,
    var arrUrl: List<String>?,
    var arrUrlList: MutableList<AuthUrlDto>?,
    var roleAuthMapCount: Int
)
