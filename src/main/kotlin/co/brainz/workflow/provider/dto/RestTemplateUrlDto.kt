package co.brainz.workflow.provider.dto

import java.io.Serializable
import java.net.InetAddress
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

data class RestTemplateUrlDto(
    val callUrl: String = "",
    val port: String = "",
    val protocol: String = "",
    val serverUrl: String = InetAddress.getLocalHost().hostAddress,
    val parameters: MultiValueMap<String, String> = LinkedMultiValueMap()
) : Serializable
