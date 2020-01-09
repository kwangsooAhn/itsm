package co.brainz.itsm.certification.service

import co.brainz.itsm.certification.dto.OAuthDto
import org.springframework.util.MultiValueMap

interface OAuthServiceIF {
    fun platformUrl(): String
    fun setParameters(code: String): MultiValueMap<String, String>
    fun callback(parameters: MultiValueMap<String, String>, platformValue: String): OAuthDto
}
