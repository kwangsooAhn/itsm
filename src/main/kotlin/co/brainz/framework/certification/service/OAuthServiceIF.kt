package co.brainz.framework.certification.service

import co.brainz.framework.certification.dto.OAuthDto
import org.springframework.util.MultiValueMap

interface OAuthServiceIF {
    fun platformUrl(): String
    fun setParameters(code: String): MultiValueMap<String, String>
    fun callback(parameters: MultiValueMap<String, String>, platformValue: String): OAuthDto
}
