/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ciClass.service

import co.brainz.api.constants.ApiConstants
import co.brainz.cmdb.ciClass.service.CIClassService
import co.brainz.cmdb.dto.CIClassDetailDto
import co.brainz.cmdb.dto.CIClassDetailValueDto
import co.brainz.cmdb.dto.CIClassDto
import co.brainz.cmdb.dto.CIClassListDto
import co.brainz.cmdb.dto.CIClassReturnDto
import co.brainz.itsm.user.service.UserService
import java.time.LocalDateTime
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ApiCIClassService(
    private val ciClassService: CIClassService,
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * CI Class 목록 조회
     */
    fun getCIClasses(params: LinkedHashMap<String, Any>): CIClassReturnDto {
        return ciClassService.getCIClasses(params)
    }

    /**
     * CI Class 목록 단일 조회
     */
    fun getCIClass(classId: String): CIClassListDto? {
        return ciClassService.getCIClass(classId)
    }

    /**
     * CI Class 상세 조회
     */
    fun getCIClassDetail(classId: String): CIClassDetailDto {
        return ciClassService.getCIClassDetail(classId)
    }

    /**
     * CI Class 에서 적용된 Attribute 목록 조회
     */
    fun getCIClassAttributes(ciId: String, classId: String): List<CIClassDetailValueDto> {
        return ciClassService.getCIClassAttributes(ciId, classId)
    }

    /**
     * CI Class 등록
     */
    fun createCIClass(ciClassDto: CIClassDto): Boolean {
        val userEntity = userService.selectUser(ApiConstants.CREATE_USER)
        ciClassDto.createUserKey = userEntity.userKey
        ciClassDto.createDt = LocalDateTime.now()
        return ciClassService.createCIClass(ciClassDto)
    }

    /**
     * CI Class 수정
     */
    fun updateCIClass(classId: String, ciClassDto: CIClassDto): Boolean {
        val userEntity = userService.selectUser(ApiConstants.CREATE_USER)
        ciClassDto.updateUserKey = userEntity.userKey
        ciClassDto.updateDt = LocalDateTime.now()
        return ciClassService.updateCIClass(ciClassDto)
    }

    /**
     * CI Class 삭제
     */
    fun deleteCIClass(classId: String): Boolean {
        return ciClassService.deleteCIClass(classId)
    }
}
