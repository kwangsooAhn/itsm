/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ciType.service

import co.brainz.api.constants.ApiConstants
import co.brainz.cmdb.ciType.service.CITypeService
import co.brainz.cmdb.provider.dto.CITypeDto
import co.brainz.cmdb.provider.dto.CITypeListDto
import co.brainz.cmdb.provider.dto.CITypeReturnDto
import co.brainz.itsm.user.service.UserService
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ApiCITypeService(
    private val ciTypeService: CITypeService,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Type 목록 조회
     */
    fun getCITypes(params: LinkedHashMap<String, Any>): CITypeReturnDto {
        return ciTypeService.getCITypes(params)
    }

    /**
     * CI Type 단일 목록 조회
     */
    fun getCIType(typeId: String): CITypeListDto {
        return ciTypeService.getCIType(typeId)
    }

    /**
     * CI Type 상세 조회
     */
    fun getCITypeDetail(typeId: String): CITypeDto {
        return ciTypeService.getCITypeDetail(typeId)
    }

    /**
     * CI Type 등록
     */
    fun createCIType(ciTypeDto: CITypeDto): Boolean {
        val userEntity = userService.selectUser(ApiConstants.CREATE_USER)
        ciTypeDto.createUserKey = userEntity.userKey
        ciTypeDto.createDt = LocalDateTime.now()
        return ciTypeService.createCIType(ciTypeDto)
    }

    /**
     * CI Type 수정
     */
    fun updateCIType(typeId: String, ciTypeDto: CITypeDto): Boolean {
        val userEntity = userService.selectUser(ApiConstants.CREATE_USER)
        ciTypeDto.updateUserKey = userEntity.userKey
        ciTypeDto.updateDt = LocalDateTime.now()
        return ciTypeService.updateCIType(typeId, ciTypeDto)
    }

    /**
     * CI Type 삭제
     */
    fun deleteCIType(typeId: String): Boolean {
        return ciTypeService.deleteCIType(typeId)
    }
}
