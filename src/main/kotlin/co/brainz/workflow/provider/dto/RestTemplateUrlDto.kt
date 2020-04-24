package co.brainz.workflow.provider.dto

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.io.Serializable
import java.net.InetAddress

data class RestTemplateUrlDto(
    val callUrl: String = "",
    val port: String = "",
    val protocol: String = "",
    val serverUrl: String = InetAddress.getLocalHost().hostAddress,
    val parameters: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()
) : Serializable
