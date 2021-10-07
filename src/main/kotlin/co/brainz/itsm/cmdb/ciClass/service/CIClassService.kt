/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciClass.service

import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.dto.CIAttributeListDto
import co.brainz.cmdb.dto.CIClassAttributeListDto
import co.brainz.cmdb.dto.CIClassDetailDto
import co.brainz.cmdb.dto.CIClassDetailValueDto
import co.brainz.cmdb.dto.CIClassDto
import co.brainz.cmdb.dto.CIClassToAttributeDto
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ciClass.constants.CIClassConstants
import co.brainz.itsm.cmdb.ciClass.dto.CIClassTreeReturnDto
import co.brainz.itsm.cmdb.ciType.service.CITypeService
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Transactional
class CIClassService(
    private val ciClassService: CIClassService,
    private val ciTypeService: CITypeService,
    private val currentSessionUser: CurrentSessionUser
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CMDB CI Class 단일 조회
     */
    fun getCIClass(classId: String): CIClassDetailDto {
        return ciClassService.getCIClassDetail(classId)
    }

    /**
     * CMDB CI class Tree 조회
     */
    fun getCIClassesTree(params: LinkedHashMap<String, Any>): CIClassTreeReturnDto {
        return ciClassService.getCIClassesTree(params)
    }

    /**
     * CMDB CI Class 등록
     */
    fun createCIClass(ciClassDto: CIClassDto): String {
        var returnValue = ""
        ciClassDto.createDt = LocalDateTime.now()
        ciClassDto.createUserKey = currentSessionUser.getUserKey()
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
        ciClassDto.updateDt = LocalDateTime.now()
        ciClassDto.updateUserKey = currentSessionUser.getUserKey()
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
        if (!ciTypeService.getCITypesByClassId(classId)) {
            if (ciClassService.deleteCIClass(classId)) {
                returnValue = CIClassConstants.Status.STATUS_SUCCESS.code
            }
        } else {
            returnValue = CIClassConstants.Status.STATUS_FAILE_CLASS_HAVE_TYPE.code
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
    fun getCIClassAttributes(ciId: String, classId: String): List<CIClassDetailValueDto> {
        return ciClassService.getCIClassAttributes(ciId, classId)
    }
}
