/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.*
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.cmdb.ciClass.constants.CIClassConstants
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
@Transactional
class CIClassService(
    private val restTemplate: RestTemplateProvider
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * CMDB CI Class 단일 조회
     */
    fun getCIClass(classId: String): CIClassDetailDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.GET_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                classId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructType(CIClassDetailDto::class.java)
        )
    }

    /**
     * CMDB CI class 멀티 조회
     */
    fun getCIClasses(parameters: LinkedMultiValueMap<String, String>): List<CIClassListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.GET_CLASSES.url,
            parameters = parameters
        )
        val responseBody = restTemplate.get(url)
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CIClassListDto::class.java)
        )
    }

    /**
     * CMDB CI Class 등록
     */
    fun createCIClass(CIClassDto: CIClassDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        CIClassDto.createDt = LocalDateTime.now()
        CIClassDto.createUserKey = aliceUserDto.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.POST_CLASS.url
        )
        val responseBody = restTemplate.create(url, CIClassDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> CIClassConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }

    /**
     * CMDB CI Class 수정
     */
    fun updateCIClass(CIClassDto: CIClassDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        CIClassDto.updateDt = LocalDateTime.now()
        CIClassDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.PUT_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                CIClassDto.classId
            )
        )
        val responseEntity = restTemplate.update(url, CIClassDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> CIClassConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
            false -> ""
        }
    }

    /**
     * CMDB CI Class 삭제
     */
    fun deleteCIClass(classId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.DELETE_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                classId
            )
        )
        return when (restTemplate.delete(url).toString().isNotEmpty()) {
            true -> CIClassConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }

    /**
     * CMDB CI classAttributeList 조회
     */
    fun getClassAttributeList(
        attributeList: List<CIAttributeListDto>,
        addAttributeList: List<CIClassToAttributeDto>?,
        extendsAttributeList: List<CIClassToAttributeDto>?
    ): MutableList<CIClassAttributeListDto> {
        val ciClassAttributeList = mutableListOf<CIClassAttributeListDto>()

        for (attributes in attributeList) {
            var ciClassAttribute = CIClassAttributeListDto(
                attributeId = attributes.attributeId,
                attributeName = attributes.attributeName,
                attributeText = attributes.attributeText,
                attributeType = attributes.attributeType,
                attributeDesc = attributes.attributeDesc,
                extends = false
            )

            if (extendsAttributeList != null) {
                for (extendsAttributes in extendsAttributeList) {
                    if (attributes.attributeId == extendsAttributes.attributeId) {
                        ciClassAttribute.extends = true
                    }
                }
            }
            ciClassAttributeList.add(ciClassAttribute)
        }

        return ciClassAttributeList
    }


    /**
     * CMDB CI 세부 속성 조회
     */
    fun getCIClassAttributes(classId: String): MutableList<CIClassDetailValueDto> {
        val url = RestTemplateUrlDto(
                callUrl = RestTemplateConstants.Class.GET_CLASS_ATTRIBUTE.url.replace(
                        restTemplate.getKeyRegex(),
                        classId
                )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
                responseBody,
                mapper.typeFactory.constructCollectionType(List::class.java, CIClassDetailValueDto::class.java)
        )
    }
}
