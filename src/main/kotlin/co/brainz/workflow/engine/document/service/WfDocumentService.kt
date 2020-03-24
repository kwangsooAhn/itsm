package co.brainz.workflow.engine.document.service

import co.brainz.workflow.engine.document.dto.WfDocumentDto
import co.brainz.workflow.engine.document.entity.WfDocumentEntity
import co.brainz.workflow.engine.document.repository.WfDocumentRepository
import co.brainz.workflow.engine.form.dto.WfFormComponentViewDto
import co.brainz.workflow.engine.form.entity.WfFormEntity
import co.brainz.workflow.engine.form.mapper.WfFormMapper
import co.brainz.workflow.engine.form.repository.WfFormRepository
import co.brainz.workflow.engine.process.entity.WfProcessEntity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.JsonParser
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfDocumentService(private val wfDocumentRepository: WfDocumentRepository,
                        private val wfFormRepository: WfFormRepository) {

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(WfFormMapper::class.java)

    /**
     * Search Documents.
     *
     * @return List<DocumentDto>
     */
    fun documents(): List<WfDocumentDto> {

        val documents = mutableListOf<WfDocumentDto>()
        val documentEntities = wfDocumentRepository.findAll()
        for (document in documentEntities) {
            val documentDto = WfDocumentDto(
                    documentId = document.documentId,
                    documentName = document.documentName,
                    documentDesc = document.documentDesc,
                    procId = document.process.processId,
                    formId = document.form.formId,
                    createDt = document.createDt,
                    createUserKey = document.createUserKey,
                    updateDt = document.updateDt,
                    updateUserKey = document.updateUserKey
            )
            documents.add(documentDto)
        }

        return documents
    }

    /**
     * Search Document.
     *
     * @param documentId
     * @return WfFormComponentViewDto?
     */
    fun document(documentId: String): WfFormComponentViewDto? {
        val documentEntity = wfDocumentRepository.findDocumentEntityByDocumentId(documentId)
        val formEntity = wfFormRepository.findWfFormEntityByFormId(documentEntity.form.formId)
        val formViewDto = wfFormMapper.toFormViewDto(formEntity.get())
        val components: MutableList<LinkedHashMap<String, Any>> = mutableListOf()
        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        for (component in formEntity.get().components!!) {
            val attributes = LinkedHashMap<String, Any>()
            attributes["type"] = component.componentType

            //make common
            val common = LinkedHashMap<String, Any>()
            common["mapping-id"] = component.mappingId
            attributes["common"] = common

            //attribute
            for (attribute in component.attributes!!) {
                val jsonElement = JsonParser().parse(attribute.attributeValue)
                when (jsonElement.isJsonArray) {
                    true -> attributes[attribute.attributeId] = mapper.readValue(attribute.attributeValue, mapper.typeFactory.constructCollectionType(List::class.java, LinkedHashMap::class.java))
                    false -> attributes[attribute.attributeId] = mapper.readValue(attribute.attributeValue, LinkedHashMap::class.java)
                }
            }

            //values
            val values: MutableList<LinkedHashMap<String, Any>> = mutableListOf()

            val map = LinkedHashMap<String, Any>()
            map["componentId"] = component.componentId
            map["attributes"] = attributes
            map["values"] = values
            components.add(map)
        }

        return WfFormComponentViewDto(
                form = formViewDto,
                components = components
        )
    }

    /**
     * Create Document.
     *
     * @param documentDto
     * @return WfDocumentDto
     */
    fun createDocument(documentDto: WfDocumentDto): WfDocumentDto {
        val form = WfFormEntity(formId = documentDto.formId)
        val process = WfProcessEntity(processId = documentDto.procId)
        val documentEntity = WfDocumentEntity(
                documentId = documentDto.documentId,
                documentName = documentDto.documentName,
                documentDesc = documentDto.documentDesc,
                form = form,
                process = process,
                createDt = documentDto.createDt,
                createUserKey = documentDto.createUserKey
        )
        val dataEntity = wfDocumentRepository.save(documentEntity)

        return WfDocumentDto(
                documentId = dataEntity.documentId,
                documentName = dataEntity.documentName,
                documentDesc = dataEntity.documentDesc,
                formId = dataEntity.form.formId,
                procId = dataEntity.process.processId,
                createDt = dataEntity.createDt,
                createUserKey = dataEntity.createUserKey
        )
    }
}
