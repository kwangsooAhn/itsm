package co.brainz.itsm.certification.serivce

import co.brainz.itsm.certification.OAuthDto
import org.springframework.util.MultiValueMap

interface OAuthServiceIF {
    fun serviceUrl(): String
    fun setParameters(code: String): MultiValueMap<String, String>
    fun callback(parameters: MultiValueMap<String, String>, service: String): OAuthDto
}