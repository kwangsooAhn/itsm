/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.service

import co.brainz.cmdb.provider.RestTemplateProvider
import co.brainz.cmdb.provider.constants.RestTemplateConstants
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.cmdb.provider.dto.CmdbClassAttributeListDto
import co.brainz.cmdb.provider.dto.CmdbClassDetailDto
import co.brainz.cmdb.provider.dto.CmdbClassDto
import co.brainz.cmdb.provider.dto.CmdbClassListDto
import co.brainz.cmdb.provider.dto.CmdbClassToAttributeDto
import co.brainz.cmdb.provider.dto.RestTemplateUrlDto
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
     * CMDB Class 단일 조회
     */
    fun getCmdbClass(classId: String): CmdbClassDetailDto {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.GET_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                classId
            )
        )
        val responseBody = restTemplate.get(url)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructType(CmdbClassDetailDto::class.java)
        )
    }

    /**
     * CMDB class 멀티 조회
     */
    fun getCmdbClasses(parameters: LinkedMultiValueMap<String, String>): List<CmdbClassListDto> {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.GET_CLASSES.url,
            parameters = parameters
        )
        val responseBody = restTemplate.get(url)
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, CmdbClassListDto::class.java)
        )
    }

    /**
     * CMDB Class 등록
     */
    fun createCmdbClass(cmdbClassDto: CmdbClassDto): String {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        cmdbClassDto.createDt = LocalDateTime.now()
        cmdbClassDto.createUserKey = aliceUserDto.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.POST_CLASS.url
        )
        val responseBody = restTemplate.create(url, cmdbClassDto)
        return when (responseBody.body.toString().isNotEmpty()) {
            true -> CIClassConstants.Status.STATUS_SUCCESS.code
            false -> ""
        }
    }

    /**
     * CMDB Class 수정
     */
    fun updateCmdbClass(cmdbClassDto: CmdbClassDto): String {
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        cmdbClassDto.updateDt = LocalDateTime.now()
        cmdbClassDto.updateUserKey = userDetails.userKey
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Class.PUT_CLASS.url.replace(
                restTemplate.getKeyRegex(),
                cmdbClassDto.classId
            )
        )
        val responseEntity = restTemplate.update(url, cmdbClassDto)
        return when (responseEntity.body.toString().isNotEmpty()) {
            true -> CIClassConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
            false -> ""
        }
    }

    /**
     * CMDB Class 삭제
     */
    fun deleteCmdbClass(classId: String): String {
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
     * CMDB classAttributeList 조회
     */
    fun getClassAttributeList(
        attributeList: List<CIAttributeListDto>,
        addAttributeList: List<CmdbClassToAttributeDto>?,
        extendsAttributeList: List<CmdbClassToAttributeDto>?
    ): MutableList<CmdbClassAttributeListDto> {
        val cmdbClassAttributeList = mutableListOf<CmdbClassAttributeListDto>()

        for (attributes in attributeList) {
            var cmdbClassAttribute = CmdbClassAttributeListDto(
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
                        cmdbClassAttribute.extends = true
                    }
                }
            }
            cmdbClassAttributeList.add(cmdbClassAttribute)
        }

        return cmdbClassAttributeList
    }
}
