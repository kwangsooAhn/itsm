package co.brainz.framework.auth.dto

import java.io.Serializable
/**
 * 권한 기본 DTO (역할관리에서 상세 정보 사용시 사용함.)
 */
data class AliceAuthSimpleDto(
        var authId: String? = "",
        var authName: String? = "",
        var authDesc: String? = ""
) : Serializable
