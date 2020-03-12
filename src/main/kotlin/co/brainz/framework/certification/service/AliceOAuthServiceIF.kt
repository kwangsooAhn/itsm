package co.brainz.framework.certification.service

import co.brainz.framework.certification.dto.AliceOAuthDto
import org.springframework.util.MultiValueMap

interface AliceOAuthServiceIF {
    fun platformUrl(): String
    fun setParameters(code: String): MultiValueMap<String, String>
    fun callback(parameters: MultiValueMap<String, String>, platformValue: String): AliceOAuthDto
}
