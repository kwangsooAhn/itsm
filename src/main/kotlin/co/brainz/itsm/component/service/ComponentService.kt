package co.brainz.itsm.component.service

import co.brainz.itsm.provider.ProviderComponent
import co.brainz.itsm.provider.dto.FormComponentDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class ComponentService(private val providerComponent: ProviderComponent) {

    fun findFormComponents(formId: String): List<FormComponentDto> {
        val params = LinkedMultiValueMap<String, String>()
        params.add("formId", formId)
        val responseBody = providerComponent.getComponents(params)
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        val list :List<FormComponentDto> = mapper.readValue(responseBody, mapper.typeFactory.constructCollectionType(List::class.java, FormComponentDto::class.java))

        return list

    }

    fun findComponents() {

    }

    fun findComponent() {

    }


}
