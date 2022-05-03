/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.cmdb.ciType.service

import co.brainz.cmdb.ciType.entity.CITypeEntity
import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.dto.CITypeDto
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
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
    fun createCIType(ciTypeDto: CITypeDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (ciTypeService.checkValidation(ciTypeDto)) {
            CITypeConstants.Status.STATUS_SUCCESS.code -> {
                ciTypeDto.createDt = LocalDateTime.now()
                ciTypeDto.createUserKey = currentSessionUser.getUserKey()
                if (!ciTypeService.createCIType(ciTypeDto)) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
            }
            CITypeConstants.Status.STATUS_FAIL_PTYPE_AND_TYPENAME_DUPLICATION.code -> {
                status = ZResponseConstants.STATUS.ERROR_EXIST
            }
            CITypeConstants.Status.STATUS_FAIL_TYPE_ALIAS_DUPLICATION.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        }
        return ZResponse(
            status = status.code,
            data = status == ZResponseConstants.STATUS.SUCCESS
        )
    }

    /**
     * CI Type 수정
     */
    fun updateCIType(ciTypeDto: CITypeDto, typeId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        when (ciTypeService.checkValidation(ciTypeDto)) {
            CITypeConstants.Status.STATUS_SUCCESS.code -> {
                ciTypeDto.createDt = LocalDateTime.now()
                ciTypeDto.createUserKey = currentSessionUser.getUserKey()
                if (!ciTypeService.updateCIType(typeId, ciTypeDto)) {
                    status = ZResponseConstants.STATUS.ERROR_FAIL
                }
            }
            CITypeConstants.Status.STATUS_FAIL_PTYPE_AND_TYPENAME_DUPLICATION.code -> {
                status = ZResponseConstants.STATUS.ERROR_EXIST
            }
            CITypeConstants.Status.STATUS_FAIL_TYPE_ALIAS_DUPLICATION.code -> {
                status = ZResponseConstants.STATUS.ERROR_DUPLICATE
            }
        }
        return ZResponse(
            status = status.code,
            data = status == ZResponseConstants.STATUS.SUCCESS
        )
    }

    /**
     * CI Type 삭제
     */
    fun deleteCIType(typeId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (!ciTypeService.deleteCIType(typeId)) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    fun getCITypesByClassId(classId: String): Boolean {
        return ciTypeService.getCITypesByClassId(classId)
    }

    fun getCITypeByTypeName(typeName: String): CITypeEntity? {
        return ciTypeService.getCITypeByTypeName(typeName)
    }
}
