package co.brainz.itsm.certification.repository

import co.brainz.itsm.certification.service.OAuthServiceGoogle
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.codec.binary.Base64
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.net.URLDecoder
import kotlin.collections.HashSet

@RunWith(SpringRunner::class)
@SpringBootTest(properties = ["classpath:application.yml"], webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class OAuthTest {

    @Autowired
    lateinit var oAuthServiceGoogle: OAuthServiceGoogle

    @Value("\${spring.oauth.google.client.clientId}")
    lateinit var googleClientId: String

    @Value("\${spring.oauth.google.client.scope}")
    lateinit var googleClientScope: String

    @Value("\${spring.oauth.google.client.redirectUri}")
    lateinit var googleClientRedirectUri: String

    val idToken = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ3NDU2YjgwNjllNDM2NWU1MTdjYTVlMjk3NTdkMWE5ZWZhNTY3YmEiLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIyMTYyMzM5NjQ3NTUtOTc4Y2hlYnI5MzdjOTgzOHVsa2M0c2Y1djVibGQzdmkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIyMTYyMzM5NjQ3NTUtOTc4Y2hlYnI5MzdjOTgzOHVsa2M0c2Y1djVibGQzdmkuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMDcxMzM1NzMwNjc1OTg4NDc2NTUiLCJlbWFpbCI6ImxpemVlbGZAZ21haWwuY29tIiwiZW1haWxfdmVyaWZpZWQiOnRydWUsImF0X2hhc2giOiJTcGp0aktRSHRiZlc3S0tjODdPdGh3IiwibmFtZSI6Iuuwle2YhOyyoCIsInBpY3R1cmUiOiJodHRwczovL2xoNS5nb29nbGV1c2VyY29udGVudC5jb20vLTM2ZlV5OGFSN0tJL0FBQUFBQUFBQUFJL0FBQUFBQUFBQUFBL0FDSGkzcmVqeTE0eFQ5RVEwODQzeExIUDJ3TDAzUmVxN3cvczk2LWMvcGhvdG8uanBnIiwiZ2l2ZW5fbmFtZSI6Iu2YhOyyoCIsImZhbWlseV9uYW1lIjoi67CVIiwibG9jYWxlIjoia28iLCJpYXQiOjE1NzczNDA5NDEsImV4cCI6MTU3NzM0NDU0MX0.l32XRcRjw0yJkT3yiIBGXPEE5ssDgAOoW2qv8O5mnrdMDg8aMRGIE7flCjqYHeRNrFQ0EnxChQ0llQOlKtDrAgeAinXIeInoZSCIpZHFYBZAzFYRpg4IGCBteErskwF2n7OGvywTft8l5BLD1gy3_bft_ZG0GHexqwaA37veyxK4EupcXKp4Lk1u96E6imY3MLxLs34LzxEpn2ZzgJJA29Q9wQnTudv2hCiyi8lr8mwrcHdPjmbG_ylB-L9rqI-LjAL6XluGslQWlsQ6Wjnv6huxHN7Z1jQ4YIqZyN-qW7DlcPqpujkiCzLSONZF0kEp5zKoEP6zxY8D2j2Ds74fxA"

    @Test
    fun requestValid() {
        val serviceUrl = URLDecoder.decode(oAuthServiceGoogle.platformUrl(), charset("UTF-8"))
        val googleLoginApiUrl = "https://accounts.google.com/o/oauth2/auth"
        val serviceUrlPath = serviceUrl.substring(0, serviceUrl.indexOf("?"))
        assertThat(googleLoginApiUrl).isEqualTo(serviceUrlPath)

        val parameter = serviceUrl.substring(serviceUrl.indexOf("?") + 1, serviceUrl.length)
        val params: HashSet<String> = parameter.split("&".toRegex()).toHashSet()
        params.forEach {
            val key = it.split("=")[0]
            val value = it.split("=")[1]
            when (key) {
                "client_id" -> assertThat(value).isEqualTo(googleClientId)
                "scope" -> assertThat(value).isEqualTo(googleClientScope)
                "redirect_uri" -> assertThat(value).isEqualTo(googleClientRedirectUri)
                "response_type" -> assertThat(value).isEqualTo("code")
            }
        }
    }

    @Test
    fun validOAuthDto() {
        val responseMap:  MutableMap<String, Any> = mutableMapOf()
        responseMap["id_token"] = idToken
        val oAuthDto = oAuthServiceGoogle.makeOAuthDto(responseMap, "google")
        assertThat(oAuthDto.email).isNotEmpty()
    }

    @Test
    fun validOAuthDtoNotEmail() {
        val tokens: MutableList<String> = idToken.split("\\.".toRegex()).toMutableList()
        val base64 = Base64(true)
        val body = String(base64.decode(tokens[1]), charset = Charsets.UTF_8)
        val mapper = ObjectMapper()
        val result: MutableMap<*, *>? = mapper.readValue(body, MutableMap::class.java)
        result?.remove("email")
        val notEmailBody: String = mapper.writeValueAsString(result)
        val encodeNotEmailBody: String = String(base64.encode(notEmailBody.toByteArray()))
        val idTokenNotEmail = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ3NDU2YjgwNjllNDM2NWU1MTdjYTVlMjk3NTdkMWE5ZWZhNTY3YmEiLCJ0eXAiOiJKV1QifQ." + encodeNotEmailBody + ".l32XRcRjw0yJkT3yiIBGXPEE5ssDgAOoW2qv8O5mnrdMDg8aMRGIE7flCjqYHeRNrFQ0EnxChQ0llQOlKtDrAgeAinXIeInoZSCIpZHFYBZAzFYRpg4IGCBteErskwF2n7OGvywTft8l5BLD1gy3_bft_ZG0GHexqwaA37veyxK4EupcXKp4Lk1u96E6imY3MLxLs34LzxEpn2ZzgJJA29Q9wQnTudv2hCiyi8lr8mwrcHdPjmbG_ylB"
        val responseMap: MutableMap<String, Any> = mutableMapOf()
        responseMap["id_token"] = idTokenNotEmail
        val oAuthDto = oAuthServiceGoogle.makeOAuthDto(responseMap, "google")
        assertThat(oAuthDto.email).isEmpty()
    }

}