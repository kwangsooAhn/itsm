/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.api.cmdb.ci.service

import co.brainz.api.constants.ApiConstants
import co.brainz.cmdb.ci.service.CIService
import co.brainz.cmdb.dto.CIDetailDto
import co.brainz.cmdb.dto.CIDto
import co.brainz.cmdb.dto.CIListDto
import co.brainz.cmdb.dto.CIReturnDto
import co.brainz.itsm.user.service.UserService
import java.time.LocalDateTime
import org.springframework.stereotype.Service

@Service
class ApiCIService(
    private val ciService: CIService,
    private val userService: UserService
) {

    /**
     * CI 목록 조회
     */
    fun getCIs(params: LinkedHashMap<String, Any>): CIReturnDto {
        return ciService.getCIs(params)
    }

    /**
     * CI 단일 목록 조회
     */
    fun getCI(ciId: String): CIListDto {
        return ciService.getCI(ciId)
    }

    /**
     * CI 상세 조회
     */
    fun getCIDetail(ciId: String): CIDetailDto {
        return ciService.getCIDetail(ciId)
    }

    /**
     * CI 등록
     */
    fun createCI(ciDto: CIDto): Boolean {
        ciDto.createUser = userService.selectUser(ApiConstants.CREATE_USER)
        ciDto.createDt = LocalDateTime.now()
        val returnDto = ciService.createCI(ciDto)
        return returnDto.status
    }

    /**
     * CI 수정
     */
    fun updateCI(ciId: String, ciDto: CIDto): Boolean {
        ciDto.updateUser = userService.selectUser(ApiConstants.CREATE_USER)
        ciDto.updateDt = LocalDateTime.now()
        val returnDto = ciService.updateCI(ciId, ciDto)
        return returnDto.status
    }

    /**
     * CI 삭제
     */
    fun deleteCI(ciId: String): Boolean {
        val returnDto = ciService.deleteCI(ciId)
        return returnDto.status
    }
}
