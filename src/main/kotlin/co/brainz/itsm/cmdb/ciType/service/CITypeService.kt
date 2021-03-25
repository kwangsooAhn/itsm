/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.service

import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.dto.CITypeDto
import co.brainz.cmdb.dto.CITypeTreeListDto
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.cmdb.ciType.constants.CITypeConstants
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class CITypeService(
    private val ciTypeService: CITypeService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Type 트리 조회
     */
    fun getCITypeList(params: LinkedHashMap<String, Any>): List<CITypeTreeListDto> {
        return ciTypeService.getCITypesTreeNode(params)
    }

    /**
     * CI Type 상세 조회
     */
    fun getCIType(typeId: String): CITypeDto {
        return ciTypeService.getCITypeDetail(typeId)
    }

    /**
     * CI Type 생성
     */
    fun createCIType(ciTypeDto: CITypeDto): String {
        var returnValue = ""
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        ciTypeDto.createDt = LocalDateTime.now()
        ciTypeDto.createUserKey = userDetails.userKey
        if (ciTypeService.createCIType(ciTypeDto)) {
            returnValue = CITypeConstants.Status.STATUS_SUCCESS.code
        }
        return returnValue
    }

    /**
     * CI Type 수정
     */
    fun updateCIType(ciTypeDto: CITypeDto, typeId: String): String {
        var returnValue = ""
        val userDetails = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        ciTypeDto.updateDt = LocalDateTime.now()
        ciTypeDto.updateUserKey = userDetails.userKey
        if (ciTypeService.updateCIType(typeId, ciTypeDto)) {
            returnValue = CITypeConstants.Status.STATUS_SUCCESS_EDIT_CLASS.code
        }
        return returnValue
    }

    /**
     * CI Type 삭제
     */
    fun deleteCIType(typeId: String): String {
        var returnValue = ""
        if (ciTypeService.deleteCIType(typeId)) {
            returnValue = CITypeConstants.Status.STATUS_SUCCESS.code
        }
        return returnValue
    }
}
