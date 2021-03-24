/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.workflow.engine.manager.impl

import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIDataDto
import co.brainz.cmdb.provider.dto.CIDto
import co.brainz.cmdb.provider.dto.CIRelationDto
import co.brainz.cmdb.provider.dto.CITagDto
import co.brainz.framework.fileTransaction.entity.AliceFileLocEntity
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.entity.WfComponentEntity
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.element.entity.WfElementEntity
import co.brainz.workflow.engine.manager.WfTokenManager
import co.brainz.workflow.engine.manager.WfTokenManagerFactory
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.engine.manager.service.WfTokenManagerService
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class WfScriptTask(
    wfTokenManagerService: WfTokenManagerService,
    override var isAutoComplete: Boolean = true
) : WfTokenManager(wfTokenManagerService) {

    val mapper: ObjectMapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    private val listLinkedMapType: CollectionType =
        TypeFactory.defaultInstance().constructCollectionType(
            List::class.java,
            TypeFactory.defaultInstance().constructMapType(
                LinkedHashMap::class.java,
                String::class.java,
                Any::class.java
            )
        )

    override fun createElementToken(createTokenDto: WfTokenDto): WfTokenDto {
        var scriptType = ""
        val element = wfTokenManagerService.getElement(createTokenDto.elementId)
        run loop@{
            element.elementDataEntities.forEach { data ->
                if (data.attributeId == WfElementConstants.AttributeId.SCRIPT_TYPE.value) {
                    scriptType = data.attributeValue
                    return@loop
                }
            }
        }

        when (scriptType) {
            WfElementConstants.ScriptType.DOCUMENT_ATTACH_FILE.value ->
                this.setDocumentAttachFile(createTokenDto, element)
            WfElementConstants.ScriptType.DOCUMENT_CMDB.value -> {
                this.callCmdbAction(createTokenDto, element)
            }
        }

        return createTokenDto
    }

    override fun createNextElementToken(createNextTokenDto: WfTokenDto): WfTokenDto {
        super.setNextTokenDto(createNextTokenDto)
        return WfTokenManagerFactory(wfTokenManagerService).createTokenManager(createNextTokenDto.elementType)
            .createToken(createNextTokenDto)
    }

    override fun completeElementToken(completedToken: WfTokenDto): WfTokenDto {
        return completedToken
    }

    /**
     * [createTokenDto.data] 에서 데이터 추출 (TokenData에 저장된 CI 데이터).
     */
    private fun getCiList(createTokenDto: WfTokenDto, componentEntity: WfComponentEntity): List<Map<String, Any>> {
        val tokenData =
            createTokenDto.data?.filter { it.componentId == componentEntity.componentId }?.get(0)
        return mapper.readValue(tokenData?.value, listLinkedMapType)
    }

    /**
     * [ciAttributes] 를 CiDto 에 저장하기 위한 List 형태로 추출.
     */
    private fun getCiDataList(ciId: String, ciComponentDataValue: Map<String, Any>): MutableList<CIDataDto> {
        val ciDataList = mutableListOf<CIDataDto>()
        val ciAttributes: List<Map<String, Any>> =
            mapper.convertValue(ciComponentDataValue["ciAttributes"], listLinkedMapType)
        ciAttributes.forEach { attribute ->
            if (attribute["id"] != null && attribute["value"] != null) {
                ciDataList.add(
                    CIDataDto(
                        ciId = ciId,
                        attributeId = attribute["id"] as String,
                        attributeData = attribute["value"] as String
                    )
                )
            }
        }
        return ciDataList
    }

    /**
     * [ciTags] 를 CIDto 에 저장하기 위한 List 형태로 추출.
     */
    private fun getCiTags(ciId: String, ciComponentDataValue: Map<String, Any>): MutableList<CITagDto> {
        val tagDataList = mutableListOf<CITagDto>()
        val ciTags: List<Map<String, Any>> =
            mapper.convertValue(ciComponentDataValue["ciTags"], listLinkedMapType)
        ciTags.forEach { tag ->
            if (tag["id"] != null && tag["value"] != null) {
                tagDataList.add(
                    CITagDto(
                        ciId = ciId,
                        tagId = tag["id"] as String,
                        tagName = tag["value"] as String
                    )
                )
            }
        }
        return tagDataList
    }

    /**
     * 연관된 CI 를 CIDto 에 저장하기 위한 List 형태로 추출.
     */
    private fun getCiRelations(ciId: String, ciComponentDataValue: Map<String, Any>): MutableList<CIRelationDto> {
        val relationList = mutableListOf<CIRelationDto>()
        // TODO : 연관된 CI 정보
        return relationList
    }

    /**
     * CMDB CI Rest Api 호출시 전달할 구조로 CI 데이터 셋팅.
     */
    private fun getCiDtoList(
        ciList: List<Map<String, Any>>,
        componentId: String,
        instanceId: String,
        actionType: String
    ): MutableList<CIDto> {
        val ciDtoList = mutableListOf<CIDto>()
        ciList.forEach { ci ->
            if (ci["actionType"] as String == actionType) {
                // wf_component_ci_data 에서 데이터 조회
                val ciId = ci["ciId"] as String
                val ciComponentData =
                    wfTokenManagerService.getComponentCIData(componentId, ciId, instanceId)
                if (ciComponentData !== null) {
                    val ciComponentDataValue: Map<String, Any> =
                        mapper.readValue(ciComponentData.values, object : TypeReference<Map<String, Any>>() {})

                    // CI에 속한 세부 정보 추출
                    val ciDataList = this.getCiDataList(ciId, ciComponentDataValue)
                    val ciTags = this.getCiTags(ciId, ciComponentDataValue)
                    val ciRelations = this.getCiRelations(ciId, ciComponentDataValue)

                    // Dto 생성후 List에 담기
                    ciDtoList.add(
                        CIDto(
                            ciId = ciId,
                            ciNo = ci["ciNo"] as String,
                            ciName = ci["ciName"] as String,
                            ciDesc = ci["ciDesc"] as String,
                            ciIcon = ci["ciIcon"] as String,
                            classId = ci["classId"] as String,
                            typeId = ci["typeId"] as String,
                            ciStatus = ci["ciStatus"] as String,
                            ciDataList = ciDataList,
                            ciTags = ciTags,
                            ciRelations = ciRelations
                        )
                    )
                } else {
                    ciDtoList.add(
                        CIDto(
                            ciId = ciId,
                            typeId = ""
                        )
                    )
                }
            }
        }
        return ciDtoList
    }

    /**
     * 1. ScriptTask Element 의 MappingId 찾기
     * 2. MappingId 로 연결된 Component 찾은 후 Component Type 이 CI 인지 검증
     * 3. 검증 완료 시, token Data 에서 CI 목록 조회
     * 4. CI 목록 Loop 실행하면서 생성, 변경, 삭제 List<CIDto> 생성 (CMDB Rest Api 호출용 데이터 작성)
     * 4-1. attributes, tags 와 같은 세부 데이터 내용을 임시테이블에서 조회하여 추가
     * 5. 생성된 List<CIDto> 를 각각의 URL로 CMDB 호출
     */
    private fun callCmdbAction(createTokenDto: WfTokenDto, element: WfElementEntity) {
        // 1. ScripTask MappingId 조회
        val targetMappingId = this.getScriptTaskMappingId(element)

        // 2. tokenData 에서 mappingId와 동일한 컴포넌트 찾기
        val componentEntity = this.getMappingComponent(createTokenDto, targetMappingId)

        // 3. 컴포넌트가 CI 컴포넌트인 경우 진행
        if (componentEntity != null && componentEntity.componentType == WfComponentConstants.ComponentType.CI.code) {
            // 3-1. componentId, instanceId, ci_id(ciList) 찾기
            val componentId = componentEntity.componentId
            val instanceId = createTokenDto.instanceId
            // 3-2. 데이터에서 ci 목록 추출
            val ciList = this.getCiList(createTokenDto, componentEntity)
            // 3-3. ci 목록을 전송할 형태로 데이터 변경
            val createCiList =
                this.getCiDtoList(ciList, componentId, instanceId, RestTemplateConstants.ActionType.REGISTER.code)
            val modifyCiList =
                this.getCiDtoList(ciList, componentId, instanceId, RestTemplateConstants.ActionType.MODIFY.code)
            val deleteCiList =
                this.getCiDtoList(ciList, componentId, instanceId, RestTemplateConstants.ActionType.DELETE.code)

            // 4. 전송
            createCiList.forEach { ci ->
                wfTokenManagerService.createCI(ci)
            }
            modifyCiList.forEach { ci ->
                wfTokenManagerService.updateCI(ci)
            }
            deleteCiList.forEach { ci ->
                wfTokenManagerService.deleteCI(ci.ciId)
            }
        }
    }

    /**
     * [targetMappingId] 로 연결된 [componentEntity] 조회.
     */
    private fun getMappingComponent(createTokenDto: WfTokenDto, targetMappingId: String): WfComponentEntity? {
        var componentEntity: WfComponentEntity? = null
        if (targetMappingId.isNotEmpty()) {
            val componentIds: MutableList<String> = mutableListOf()
            createTokenDto.data?.forEach { componentIds.add(it.componentId) }
            componentEntity =
                wfTokenManagerService.getComponentIdInAndMappingId(componentIds, targetMappingId)
        }
        return componentEntity
    }

    /**
     * CI Component 의 매핑아이디 조회.
     */
    private fun getScriptTaskMappingId(element: WfElementEntity): String {
        var targetMappingId = ""
        for (scriptData in element.elementScriptDataEntities) {
            if (!scriptData.scriptValue.isNullOrEmpty()) {
                val scriptMap: Map<String, Any> =
                    mapper.readValue(scriptData.scriptValue, object : TypeReference<Map<String, Any>>() {})
                targetMappingId = scriptMap[WfElementConstants.AttributeId.TARGET_MAPPING_ID.value].toString()
            }
        }
        return targetMappingId
    }

    /**
     * 1. [targetMappingId] 값으로 조건이 되는 컴포넌트를 찾고
     * 2. 값을 비교하여 찾은 파일을
     * 3. [sourceMappingId] 값으로 매핑된 컴포넌트에 추가한다.
     */
    private fun setDocumentAttachFile(createTokenDto: WfTokenDto, element: WfElementEntity) {
        var targetMappingId = ""
        var sourceMappingId = ""
        val scriptActionList: MutableList<Map<String, Any>> = mutableListOf()
        for (scriptData in element.elementScriptDataEntities) {
            if (!scriptData.scriptValue.isNullOrEmpty()) {
                val scriptMap: Map<String, Any> =
                    mapper.readValue(scriptData.scriptValue, object : TypeReference<Map<String, Any>>() {})
                targetMappingId = scriptMap[WfElementConstants.AttributeId.TARGET_MAPPING_ID.value].toString()
                sourceMappingId = scriptMap[WfElementConstants.AttributeId.SOURCE_MAPPING_ID.value].toString()
                if (scriptMap[WfElementConstants.AttributeId.ACTION.value] != null) {
                    val actionList: MutableList<LinkedHashMap<String, Any>> = mapper.convertValue(
                        scriptMap[WfElementConstants.AttributeId.ACTION.value],
                        TypeFactory.defaultInstance()
                            .constructCollectionType(MutableList::class.java, LinkedHashMap::class.java)
                    )
                    for (action in actionList) {
                        val map = HashMap<String, Any>()
                        val condition = action[WfElementConstants.AttributeId.CONDITION.value].toString()
                        val conditionArray =
                            condition.replace("\\s+".toRegex(), "").split("(?=[a-zA-Z0-9])".toRegex(), 2)
                        if (conditionArray.size == 2) {
                            map["operator"] = conditionArray[0]
                            map["value"] = conditionArray[1]
                        }
                        map["file"] = action[WfElementConstants.AttributeId.FILE.value].toString()
                        scriptActionList.add(map)
                    }
                }
            }
        }

        val componentIds: MutableList<String> = mutableListOf()
        createTokenDto.data?.forEach { componentIds.add(it.componentId) }
        val sourceComponentId =
            wfTokenManagerService.getComponentIdInAndMappingId(componentIds, sourceMappingId).componentId
        val componentValue = wfTokenManagerService.getComponentValue(
            createTokenDto.tokenId,
            sourceComponentId,
            WfComponentConstants.ComponentValueType.STRING_SEPARATOR.code
        )
        val attachFileNames: MutableList<String> = mutableListOf()
        scriptActionList.forEach { action ->
            when (action["operator"]) {
                "=" -> {
                    if (componentValue == action["value"]) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                "<=" -> {
                    if (componentValue.toInt() <= (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                "<" -> {
                    if (componentValue.toInt() < (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                ">=" -> {
                    if (componentValue.toInt() >= (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
                ">" -> {
                    if (componentValue.toInt() > (action["value"] ?: 0) as Int) {
                        attachFileNames.add(action["file"].toString())
                        return@forEach
                    }
                }
            }
        }

        if (attachFileNames.isNotEmpty()) {
            val fileSeqList: MutableList<Long> = mutableListOf()
            for (attachFileName in attachFileNames) {
                val processFilePath = wfTokenManagerService.getProcessFilePath(attachFileName)
                val fileName = wfTokenManagerService.getRandomFilename()
                val uploadFilePath = wfTokenManagerService.getUploadFilePath(fileName)
                Files.copy(processFilePath, uploadFilePath, StandardCopyOption.REPLACE_EXISTING)
                if (Files.exists(uploadFilePath)) {
                    val aliceFileLocEntity = AliceFileLocEntity(
                        fileSeq = 0,
                        uploadedLocation = uploadFilePath.parent.toString(),
                        sort = 0,
                        randomName = fileName,
                        originName = attachFileName,
                        fileSize = uploadFilePath.toFile().length(),
                        uploaded = true,
                        fileOwner = super.assigneeId
                    )
                    fileSeqList.add(wfTokenManagerService.uploadProcessFile(aliceFileLocEntity).fileSeq)
                }
            }

            if (fileSeqList.isNotEmpty()) {
                var fileSeqValue = ""
                fileSeqList.forEachIndexed { index, fileSeq ->
                    if (index != 0) {
                        fileSeqValue += ","
                    }
                    fileSeqValue += fileSeq
                }

                // 토큰 데이터에 파일 seq 값을 저장
                val targetComponentId =
                    wfTokenManagerService.getComponentIdInAndMappingId(componentIds, targetMappingId).componentId
                run loop@{
                    createTokenDto.data?.forEach { data ->
                        if (data.componentId == targetComponentId) {
                            data.value = fileSeqValue
                            return@loop
                        }
                    }
                }
                super.tokenEntity.tokenDataEntities =
                    wfTokenManagerService.saveAllTokenData(super.setTokenData(createTokenDto))
            }
        }
    }
}
