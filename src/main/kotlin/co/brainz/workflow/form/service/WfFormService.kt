/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.form.service

import co.brainz.framework.auth.repository.AliceUserRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.tag.constants.AliceTagConstants
import co.brainz.framework.tag.entity.AliceTagEntity
import co.brainz.framework.tag.repository.AliceTagRepository
import co.brainz.framework.tag.service.AliceTagService
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.form.dto.FormSearchCondition
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.component.entity.WfComponentPropertyEntity
import co.brainz.workflow.component.repository.WfComponentPropertyRepository
import co.brainz.workflow.component.repository.WfComponentRepository
import co.brainz.workflow.document.repository.WfDocumentRepository
import co.brainz.workflow.form.constants.WfFormConstants
import co.brainz.workflow.form.entity.WfFormEntity
import co.brainz.workflow.form.mapper.WfFormMapper
import co.brainz.workflow.form.repository.WfFormRepository
import co.brainz.workflow.formGroup.entity.WfFormGroupEntity
import co.brainz.workflow.formGroup.entity.WfFormGroupPropertyEntity
import co.brainz.workflow.formGroup.repository.WfFormGroupPropertyRepository
import co.brainz.workflow.formGroup.repository.WfFormGroupRepository
import co.brainz.workflow.formRow.entity.WfFormRowEntity
import co.brainz.workflow.formRow.repository.WfFormRowRepository
import co.brainz.workflow.provider.dto.FormComponentDto
import co.brainz.workflow.provider.dto.FormGroupDto
import co.brainz.workflow.provider.dto.FormRowDto
import co.brainz.workflow.provider.dto.RestTemplateFormDataDto
import co.brainz.workflow.provider.dto.RestTemplateFormDto
import co.brainz.workflow.provider.dto.RestTemplateFormListReturnDto
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.util.UUID
import javax.transaction.Transactional
import kotlin.math.ceil
import org.mapstruct.factory.Mappers
import org.springframework.stereotype.Service

@Service
class WfFormService(
    private val wfFormRepository: WfFormRepository,
    private val wfComponentRepository: WfComponentRepository,
    private val wfDocumentRepository: WfDocumentRepository,
    private val aliceUserRepository: AliceUserRepository,
    private val aliceTagRepository: AliceTagRepository,
    private val wfFormGroupRepository: WfFormGroupRepository,
    private val wfFormRowRepository: WfFormRowRepository,
    private val wfComponentPropertyRepository: WfComponentPropertyRepository,
    private val wfFormGroupPropertyRepository: WfFormGroupPropertyRepository,
    private val aliceTagService: AliceTagService
) {

    private val wfFormMapper: WfFormMapper = Mappers.getMapper(
        WfFormMapper::class.java
    )

    private val objMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 폼 목록을 출력하기 위한 문서양식 리스트 조회
     *
     * @param formSearchCondition
     * @return List<RestTemplateFormDto>
     */
    fun getFormList(formSearchCondition: FormSearchCondition): RestTemplateFormListReturnDto {
        val queryResult = wfFormRepository.findFormEntityList(formSearchCondition)
        val formList = mutableListOf<RestTemplateFormDto>()
        for (form in queryResult.results) {
            val restTemplateDto = wfFormMapper.toFormViewDto(form)
            when (restTemplateDto.status) {
                WfFormConstants.FormStatus.EDIT.value,
                WfFormConstants.FormStatus.PUBLISH.value -> restTemplateDto.editable = true
                WfFormConstants.FormStatus.USE.value -> {
                    restTemplateDto.editable = !wfFormRepository.findFormDocumentExist(restTemplateDto.id)
                }
            }
            formList.add(restTemplateDto)
        }

        return RestTemplateFormListReturnDto(
            data = formList,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = wfFormRepository.count(),
                currentPageNum = formSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * Form Info.
     *
     * [formId]를 받아서 문서양식 자체에 대한 세부 데이터 조회.
     * 하위의 그룹 정보등은 포함되지 않는다.
     *
     * @param formId
     * @return RestTemplateFormDto
     */
    fun getFormInfo(formId: String): RestTemplateFormDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId).get()
        val restTemplateFormDto = wfFormMapper.toFormViewDto(formEntity)
        when (restTemplateFormDto.status) {
            WfFormConstants.FormStatus.EDIT.value,
            WfFormConstants.FormStatus.PUBLISH.value -> restTemplateFormDto.editable = true
            WfFormConstants.FormStatus.USE.value -> {
                restTemplateFormDto.editable = !wfFormRepository.findFormDocumentExist(formId)
            }
        }
        return restTemplateFormDto
    }

    /**
     * 폼 전체 데이터를 조회
     *
     * 폼과 폼에 포함된 Group, Row, Component 등 실제 문서를 출력하기 위한 전체 정보를 조회.
     * 폼 편집 및 신청서, 처리할 문서 등에서 모두 사용.
     *
     * @param formId
     * @return RestTemplateFormDataDto
     */
    fun getFormData(formId: String): RestTemplateFormDataDto {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId)
        val linkedMapType = TypeFactory.defaultInstance()
            .constructMapType(LinkedHashMap::class.java, String::class.java, Any::class.java)

        val formGroupList: MutableList<FormGroupDto> = mutableListOf()
        val formGroupEntityList = wfFormGroupRepository.findByFormId(formId)
        for (formGroup in formGroupEntityList) {

            val rowListInGroup: MutableList<FormRowDto> = mutableListOf()
            val formRowEntityList = wfFormRowRepository.findByGroupId(formGroup.formGroupId)
            for (formRow in formRowEntityList) {

                // 컴포넌트 구성
                val componentDtoList: MutableList<FormComponentDto> = mutableListOf()
                val componentEntityList = wfComponentRepository.findByRowId(formRow.formRowId)
                for (componentEntity in componentEntityList) {
                    // 기본 component dto
                    val componentDto = FormComponentDto(
                        id = componentEntity.componentId,
                        type = componentEntity.componentType,
                        mapId = componentEntity.mappingId,
                        isTopic = componentEntity.isTopic,
                        tags = aliceTagService.getTagsByTargetId(
                            AliceTagConstants.TagType.COMPONENT.code,
                            componentEntity.componentId
                        )
                    )

                    // component 속성 채우기
                    val componentPropertyEntityList =
                        wfComponentPropertyRepository.findByComponentId(componentEntity.componentId)
                    for (componentPropertyEntity in componentPropertyEntityList) {
                        val optionValue =
                            objMapper.readValue(componentPropertyEntity.propertyOptions, LinkedHashMap::class.java)
                        when (componentPropertyEntity.propertyType) {
                            WfFormConstants.PropertyType.DISPLAY.value -> componentDto.display =
                                objMapper.convertValue(optionValue, linkedMapType)
                            WfFormConstants.PropertyType.LABEL.value -> componentDto.label =
                                objMapper.convertValue(optionValue, linkedMapType)
                            WfFormConstants.PropertyType.VALIDATION.value -> componentDto.validation =
                                objMapper.convertValue(optionValue, linkedMapType)
                            WfFormConstants.PropertyType.ELEMENT.value -> componentDto.element =
                                objMapper.convertValue(optionValue, linkedMapType)
                        }
                    }
                    componentDtoList.add(componentDto)
                }

                val optionValue = objMapper.readValue(formRow.rowDisplayOption, LinkedHashMap::class.java)
                rowListInGroup.add(
                    FormRowDto(
                        id = formRow.formRowId,
                        display = objMapper.convertValue(optionValue, linkedMapType),
                        component = componentDtoList
                    )
                )
            }

            // 그룹 구성
            val groupDto = FormGroupDto(
                id = formGroup.formGroupId,
                name = formGroup.formGroupName,
                row = rowListInGroup
            )

            // 그룹 속성 채우기
            val groupPropertyEntityList = wfFormGroupPropertyRepository.findByFormGroupId(formGroup.formGroupId)
            for (groupPropertyEntity in groupPropertyEntityList) {
                val optionValue = objMapper.readValue(groupPropertyEntity.propertyOptions, LinkedHashMap::class.java)
                when (groupPropertyEntity.propertyType) {
                    WfFormConstants.PropertyType.DISPLAY.value -> groupDto.display =
                        objMapper.convertValue(optionValue, linkedMapType)
                    WfFormConstants.PropertyType.LABEL.value -> groupDto.label =
                        objMapper.convertValue(optionValue, linkedMapType)
                }
            }
            formGroupList.add(groupDto)
        }

        var displayOption: LinkedHashMap<String, Any> = LinkedHashMap()
        formEntity.get().formDisplayOption?.let {
            val displayOptionMap = objMapper.readValue(it, LinkedHashMap::class.java)
            displayOption = objMapper.convertValue(displayOptionMap, linkedMapType)
        }

        return RestTemplateFormDataDto(
            id = formId,
            name = formEntity.get().formName,
            status = formEntity.get().formStatus,
            desc = formEntity.get().formDesc,
            category = formEntity.get().formCategory ?: "",
            updateDt = formEntity.get().updateDt,
            updateUserKey = formEntity.get().updateUser?.userKey,
            createDt = formEntity.get().createDt,
            createUserKey = formEntity.get().createUser?.userKey,
            group = formGroupList,
            display = displayOption
        )
    }

    /**
     * Get component data.
     */
    private fun getComponentData(
        resultComponentEntity: WfComponentEntity,
        component: FormComponentDto
    ): MutableList<WfComponentPropertyEntity> {
        val wfComponentPropertyEntities: MutableList<WfComponentPropertyEntity> = mutableListOf()

        // wf_comp_data 저장
        var componentPropertyEntity = WfComponentPropertyEntity(
            componentId = resultComponentEntity.componentId,
            propertyType = WfFormConstants.PropertyType.DISPLAY.value,
            propertyOptions = objMapper.writeValueAsString(component.display),
            properties = resultComponentEntity
        )
        wfComponentPropertyEntities.add(componentPropertyEntity)

        component.validation?.let {
            if (it.size > 0) {
                componentPropertyEntity = WfComponentPropertyEntity(
                    componentId = resultComponentEntity.componentId,
                    propertyType = WfFormConstants.PropertyType.VALIDATION.value,
                    propertyOptions = objMapper.writeValueAsString(it),
                    properties = resultComponentEntity
                )
                wfComponentPropertyEntities.add(componentPropertyEntity)
            }
        }

        component.element?.let {
            if (it.size > 0) {
                componentPropertyEntity = WfComponentPropertyEntity(
                    componentId = resultComponentEntity.componentId,
                    propertyType = WfFormConstants.PropertyType.ELEMENT.value,
                    propertyOptions = objMapper.writeValueAsString(it),
                    properties = resultComponentEntity
                )
                wfComponentPropertyEntities.add(componentPropertyEntity)
            }
        }

        if (component.label.size > 0) {
            componentPropertyEntity = WfComponentPropertyEntity(
                componentId = resultComponentEntity.componentId,
                propertyType = WfFormConstants.PropertyType.LABEL.value,
                propertyOptions = objMapper.writeValueAsString(component.label),
                properties = resultComponentEntity
            )
            wfComponentPropertyEntities.add(componentPropertyEntity)
        }

        return wfComponentPropertyEntities
    }

    /**
     * Create Form.
     *
     * @param restTemplateFormDto
     * @return RestTemplateFormDto
     */
    @Transactional
    fun createForm(restTemplateFormDto: RestTemplateFormDto): RestTemplateFormDto {
        val formEntity = WfFormEntity(
            formId = restTemplateFormDto.id,
            formName = restTemplateFormDto.name,
            formDesc = restTemplateFormDto.desc,
            formStatus = restTemplateFormDto.status,
            createDt = restTemplateFormDto.createDt,
            createUser = restTemplateFormDto.createUserKey?.let { aliceUserRepository.findAliceUserEntityByUserKey(it) }
        )
        val dataEntity = wfFormRepository.save(formEntity)

        return RestTemplateFormDto(
            id = dataEntity.formId,
            name = dataEntity.formName,
            status = dataEntity.formStatus,
            desc = dataEntity.formDesc,
            editable = true,
            createUserKey = dataEntity.createUser?.userKey,
            createDt = dataEntity.createDt
        )
    }

    /**
     * Delete Form.
     *
     * @param formId
     */
    @Transactional
    fun deleteForm(formId: String): Boolean {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(formId).get()
        val documentEntity = wfDocumentRepository.findByForm(formEntity)
        if (documentEntity == null) {
            wfFormRepository.removeWfFormEntityByFormId(formId)
            return true
        }
        return false
    }

    /**
     * Update Form.
     *
     * @param restTemplateFormDto
     * @return Boolean
     */
    @Transactional
    fun saveForm(restTemplateFormDto: RestTemplateFormDto): String {
        this.updateFormEntity(restTemplateFormDto)
        return WfFormConstants.Status.STATUS_SUCCESS.code
    }

    /**
     * Insert, Update Form Data.
     *
     * @param formData
     */
    @Transactional
    fun saveFormData(formData: RestTemplateFormDataDto): String {

        // Delete
        val groupEntities = formData.id?.let { wfFormGroupRepository.findByFormId(it) }
        groupEntities?.let {
            for (group in groupEntities) {
                val rowEntities = wfFormRowRepository.findByGroupId(group.formGroupId)
                rowEntities.let {
                    for (row in rowEntities) {
                        val componentEntities = wfComponentRepository.findByRowId(row.formRowId)
                        for (component in componentEntities) {
                            wfComponentRepository.deleteComponentEntityByComponentId(component.componentId)
                            aliceTagRepository.deleteByTargetId(
                                AliceTagConstants.TagType.COMPONENT.code,
                                component.componentId
                            )
                        }
                        wfFormRowRepository.delete(row)
                    }
                }
                wfFormGroupRepository.delete(group)
            }
        }

        // Update Form
        val resultFormEntity =
            this.updateFormEntity(wfFormMapper.toRestTemplateFormDto(formData))

        // Insert component, attribute
        for (group in formData.group.orEmpty()) {
            val wfFormGroupPropertyEntities: MutableList<WfFormGroupPropertyEntity> = mutableListOf()
            val currentGroup = wfFormGroupRepository.save(
                WfFormGroupEntity(
                    formGroupId = group.id,
                    formGroupName = group.name!!,
                    form = resultFormEntity
                )
            )

            wfFormGroupPropertyEntities.addAll(this.getFormGroupProperty(currentGroup, group))

            if (wfFormGroupPropertyEntities.isNotEmpty()) {
                wfFormGroupPropertyRepository.saveAll(wfFormGroupPropertyEntities)
            }

            group.row.let {
                for (row in it) {
                    val currentRow = wfFormRowRepository.save(
                        WfFormRowEntity(
                            formRowId = row.id,
                            formGroup = currentGroup,
                            rowDisplayOption = objMapper.writeValueAsString(row.display)
                        )
                    )
                    row.component.let { components ->
                        val wfComponentPropertyEntities: MutableList<WfComponentPropertyEntity> = mutableListOf()
                        for (component in components) {
                            val resultComponentEntity =
                                this.saveComponent(currentRow, resultFormEntity, component)
                            wfComponentPropertyEntities.addAll(
                                this.getComponentData(
                                    resultComponentEntity,
                                    component
                                )
                            )
                        }
                        if (wfComponentPropertyEntities.isNotEmpty()) {
                            wfComponentPropertyRepository.saveAll(wfComponentPropertyEntities)
                        }
                    }
                }
            }
        }

        return WfFormConstants.Status.STATUS_SUCCESS.code
    }

    /**
     * Save as Form.
     *
     * @param restTemplateFormDataDto
     * @return RestTemplateFormDto
     */
    @Transactional
    fun saveAsFormData(restTemplateFormDataDto: RestTemplateFormDataDto): RestTemplateFormDto {
        val formDataDto = RestTemplateFormDto(
            name = restTemplateFormDataDto.name,
            status = restTemplateFormDataDto.status,
            desc = restTemplateFormDataDto.desc,
            editable = true,
            createUserKey = restTemplateFormDataDto.createUserKey,
            createDt = restTemplateFormDataDto.createDt
        )
        val wfFormDto = createForm(formDataDto)
        restTemplateFormDataDto.id = wfFormDto.id
        when (restTemplateFormDataDto.status) {
            WfFormConstants.FormStatus.PUBLISH.value, WfFormConstants.FormStatus.DESTROY.value -> wfFormDto.editable =
                false
        }

        for (group in restTemplateFormDataDto.group.orEmpty()) {
            group.id = UUID.randomUUID().toString().replace("-", "")
            for (row in group.row) {
                row.id = UUID.randomUUID().toString().replace("-", "")
                for (component in row.component) {
                    component.id = UUID.randomUUID().toString().replace("-", "")
                }
            }
        }
        saveFormData(restTemplateFormDataDto)

        return wfFormDto
    }

    /**
     * Update form entity.
     */
    private fun updateFormEntity(restTemplateFormDto: RestTemplateFormDto): WfFormEntity {
        val formEntity = wfFormRepository.findWfFormEntityByFormId(restTemplateFormDto.id)
        formEntity.get().formName = restTemplateFormDto.name
        formEntity.get().formDesc = restTemplateFormDto.desc
        formEntity.get().formStatus = restTemplateFormDto.status
        formEntity.get().formDisplayOption = objMapper.writeValueAsString(restTemplateFormDto.display)
        formEntity.get().formCategory = restTemplateFormDto.category
        formEntity.get().updateDt = restTemplateFormDto.updateDt
        formEntity.get().updateUser = restTemplateFormDto.updateUserKey?.let {
            aliceUserRepository.findAliceUserEntityByUserKey(it)
        }
        return wfFormRepository.save(formEntity.get())
    }

    /**
     * Save component.
     */
    private fun saveComponent(
        currentRowEntity: WfFormRowEntity,
        currentFormEntity: WfFormEntity,
        component: FormComponentDto
    ): WfComponentEntity {
        component.tags?.forEach { tag ->
            aliceTagRepository.save(
                AliceTagEntity(
                    tagType = AliceTagConstants.TagType.COMPONENT.code,
                    tagValue = tag.tagValue,
                    targetId = component.id
                )
            )
        }

        val componentEntity = WfComponentEntity(
            componentId = component.id,
            componentType = component.type,
            mappingId = component.mapId,
            isTopic = component.isTopic,
            form = currentFormEntity, // 리팩토링 후 삭제 대상 필드
            formRow = currentRowEntity
        )

        return wfComponentRepository.save(componentEntity)
    }

    private fun getFormGroupProperty(
        formGroupEntity: WfFormGroupEntity,
        group: FormGroupDto
    ): MutableList<WfFormGroupPropertyEntity> {
        val wfFormGroupPropertyEntities: MutableList<WfFormGroupPropertyEntity> = mutableListOf()

        var formGroupPropertyEntity = WfFormGroupPropertyEntity(
            formGroupId = formGroupEntity.formGroupId,
            propertyType = "display",
            propertyOptions = objMapper.writeValueAsString(group.display),
            properties = formGroupEntity
        )
        wfFormGroupPropertyEntities.add(formGroupPropertyEntity)

        group.label?.let {
            if (it.size > 0) {
                formGroupPropertyEntity = WfFormGroupPropertyEntity(
                    formGroupId = formGroupEntity.formGroupId,
                    propertyType = "label",
                    propertyOptions = objMapper.writeValueAsString(it),
                    properties = formGroupEntity
                )
                wfFormGroupPropertyEntities.add(formGroupPropertyEntity)
            }
        }
        return wfFormGroupPropertyEntities
    }

    /**
     * Check Form Data
     */
    fun checkFormData(formId: String?, formName: String): String {
        var status = WfFormConstants.Status.STATUS_SUCCESS.code

        if (formId == null) {
            if (wfFormRepository.existsByFormName(formName)) {
                status = WfFormConstants.Status.STATUS_ERROR_DUPLICATE_FORM_NAME.code
            }
        } else {
            val existForm = wfFormRepository.getOne(formId)
            val isExistForm = existForm.formName == formName
            if (!isExistForm) {
                if (wfFormRepository.existsByFormName(formName)) {
                    status = WfFormConstants.Status.STATUS_ERROR_DUPLICATE_FORM_NAME.code
                }
            }
        }
        return status
    }
}
