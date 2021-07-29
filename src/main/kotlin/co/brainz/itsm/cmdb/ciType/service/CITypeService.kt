/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.service

import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.dto.CITypeDto
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.cmdb.ciType.constants.CITypeConstants
import co.brainz.itsm.cmdb.ciType.dto.CITypeTreeReturnDto
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CITypeService(
    private val ciTypeService: CITypeService,
    private val currentSessionUser: CurrentSessionUser
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Type 트리 조회
     */
    fun getCITypesTree(params: LinkedHashMap<String, Any>): CITypeTreeReturnDto {
        return ciTypeService.getCITypesTree(params)
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
        ciTypeDto.createDt = LocalDateTime.now()
        ciTypeDto.createUserKey = currentSessionUser.getUserKey()
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
        ciTypeDto.updateDt = LocalDateTime.now()
        ciTypeDto.updateUserKey = currentSessionUser.getUserKey()
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

    fun getCITypesByClassId(classId: String): Boolean {
        return ciTypeService.getCITypesByClassId(classId)
    }
}
