package co.brainz.itsm.provider

import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate

@Service
class ProviderComponent(private val restTemplate: RestTemplate): ProviderUtilities() {

    fun getComponents(params: LinkedMultiValueMap<String, String>): String {
        return ""
    }

    fun getComponent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun postComponent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun putComponent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun deleteComponent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
