package co.brainz.itsm.provider

import co.brainz.itsm.provider.constants.ProviderConstants
import co.brainz.itsm.provider.dto.UrlDto
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class ProviderDocument(private val restTemplate: RestTemplate): ProviderUtilities() {

    /**
     * Search Documents.
     *
     * @return String
     */
    fun getDocuments(): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Workflow.GET_DOCUMENTS.url))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

    /**
     * Search Document.
     *
     * @param documentId
     * @return String
     */
    fun getDocument(documentId: String): String {
        val url = makeUri(UrlDto(callUrl = ProviderConstants.Workflow.GET_DOCUMENT.url.replace(keyRegex, documentId)))
        return restTemplate.getForObject(url, String::class.java)?:""
    }

}
