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
import co.brainz.cmdb.dto.CIListReturnDto
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.cmdb.ci.dto.CISearchCondition
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.itsm.user.service.UserService
import java.time.LocalDateTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ApiCIService(
    private val ciService: CIService,
    private val userService: UserService,
    private val userRepository: UserRepository
) {

    /**
     * CI 목록 조회
     */
    fun getCIs(params: LinkedHashMap<String, Any>): CIListReturnDto {
        var search: String? = null
        var tags: String? = null
        var flag: String? = null
        if (params["search"] != null) search = params["search"].toString()
        if (params["tags"] != null) tags = params["tags"].toString()
        if (params["flag"] != null) flag = params["flag"].toString()
        return ciService.getCIs(
            CISearchCondition(
                searchValue = search,
                tagSearch = tags,
                flag = flag
            )
        )
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
    fun createCI(ciDto: CIDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        ciDto.createUserKey?.let {
            if (userRepository.findByIdOrNull(it) == null) {
                userService.selectUser(ApiConstants.CREATE_USER)
            }
        }
        ciDto.createDt = LocalDateTime.now()
        if (!ciService.createCI(ciDto).status) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code,
            data = status == ZResponseConstants.STATUS.SUCCESS
        )
    }

    /**
     * CI 수정
     */
    fun updateCI(ciId: String, ciDto: CIDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        ciDto.interlink = true
        ciDto.updateUserKey?.let {
            if (userRepository.findByIdOrNull(it) == null) {
                userService.selectUser(ApiConstants.CREATE_USER)
            }
        }
        ciDto.updateDt = LocalDateTime.now()
        if (!ciService.updateCI(ciDto).status) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code,
            data = status == ZResponseConstants.STATUS.SUCCESS
        )
    }

    /**
     * CI 삭제
     */
    fun deleteCI(ciId: String, ciDto: CIDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        ciDto.interlink = true
        ciDto.updateUserKey?.let {
            if (userRepository.findByIdOrNull(it) == null) {
                userService.selectUser(ApiConstants.CREATE_USER)
            }
        }
        ciDto.updateDt = LocalDateTime.now()
        if (ciDto.ciId.isEmpty()) {
            ciDto.ciId = ciId
        }
        if (!ciService.deleteCI(ciDto).status) {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }
}
