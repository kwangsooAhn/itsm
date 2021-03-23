/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.service

import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.provider.dto.CIAttributeListDto
import co.brainz.cmdb.provider.dto.CIClassAttributeListDto
import co.brainz.cmdb.provider.dto.CIClassDetailDto
import co.brainz.cmdb.provider.dto.CIClassDetailValueDto
import co.brainz.cmdb.provider.dto.CIClassDto
import co.brainz.cmdb.provider.dto.CIClassToAttributeDto
import co.brainz.cmdb.provider.dto.CIClassTreeListDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.cmdb.ciClass.constants.CIClassConstants
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
@Transactional
class CIClassService(
    private val ciClassService: CIClassService
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB CI Class 단일 조회
     */
    fun getCIClass(classId: String): CIClassDetailDto {
        return ciClassService.getCIClassDetail(classId)
    }

    /**
     * CMDB CI class 멀티 조회
     */
    fun getCIClasses(params: LinkedHashMap<String, Any>): List<CIClassTreeListDto> {
        return ciClassService.getCIClassesTreeNode(params)
    }

    /**
     * CMDB CI Class 등록
     */
    fun createCIClass(ciClassDto: CIClassDto): String {
        var returnValue = ""
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        ciClassDto.createDt = LocalDateTime.now()
        ciClassDto.createUserKey = aliceUserDto.userKey
        if (ciClassService.createCIClass(ciClassDto)) {
            returnValue = CIClassConstants.Status.STATUS_SUCCESS.code
        }
        return returnValue
    }

    /**
     * CMDB CI Class 수정
     */
    fun updateCIClass(ciClassDto: CIClassDto): String {
        var returnValue = ""
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        ciClassDto.updateDt = LocalDateTime.now()
        ciClassDto.updateUserKey = userDetails.userKey
        if (ciClassService.updateCIClass(ciClassDto)) {
            returnValue = CIClassConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
        }
        return returnValue
    }

    /**
     * CMDB CI Class 삭제
     */
    fun deleteCIClass(classId: String): String {
        var returnValue = ""
        if (ciClassService.deleteCIClass(classId)) {
            returnValue = CIClassConstants.Status.STATUS_SUCCESS.code
        }
        return returnValue
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
            val ciClassAttribute = CIClassAttributeListDto(
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
    fun getCIClassAttributes(classId: String): List<CIClassDetailValueDto> {
        return ciClassService.getCIClassAttributes(classId)
    }
}
