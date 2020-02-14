package co.brainz.itsm.provider

import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ProviderWorkflow(private val restTemplate: RestTemplate): ProviderUtilities() {

    fun getServiceRequests() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getServiceRequest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getProcessInstances() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getProcessInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun postProcessInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun putProcessInstance() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun taskComplete() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun taskCompleteToGateway() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
