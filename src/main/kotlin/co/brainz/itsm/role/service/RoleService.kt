/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.service

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.entity.AliceUserRoleMapEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.constants.AliceConstants
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.organization.entity.OrganizationRoleMapEntity
import co.brainz.framework.organization.repository.OrganizationRoleMapRepository
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.role.dto.RoleDto
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.dto.RoleListReturnDto
import co.brainz.itsm.role.dto.RoleSearchCondition
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.repository.UserRepository
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import javax.transaction.Transactional
import kotlin.math.ceil
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val aliceMessageSource: AliceMessageSource,
    private val roleRepository: RoleRepository,
    private val authRepository: AliceAuthRepository,
    private val roleAuthMapRepository: AliceRoleAuthMapRepository,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val excelComponent: ExcelComponent,
    private val userRepository: UserRepository,
    private val organizationRoleMapRepository: OrganizationRoleMapRepository
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * 상단 전체 역할정보를 가져온다.
     */
    fun selectRoleList(): RoleListReturnDto {
        val roleList = roleRepository.findRoleSearch(RoleSearchCondition(""))
        return RoleListReturnDto(
            data = mapper.convertValue(roleList.dataList, object : TypeReference<List<RoleListDto>>() {}),
            paging = AlicePagingData(
                totalCount = roleList.totalCount,
                totalCountWithoutCondition = roleRepository.count(),
                currentPageNum = 0L,
                totalPageNum = 0L,
                orderType = ""
            )
        )
    }

    /**
     * 전체 권한정보를 가져온다.
     */
    fun selectAuthList(): List<AliceAuthEntity> {
        return authRepository.findAllByOrderByAuthName()
    }

    /**
     * 역할 삭제 한다.
     */
    @Transactional
    fun deleteRole(roleId: String): String {
        val roleInfo = roleRepository.findByRoleId(roleId)
        val userRoleMapCount = userRoleMapRepository.findByRole(roleInfo).count()
        return if (userRoleMapCount == 0) {
            roleInfo.roleAuthMapEntities.forEach { roleAuthMap ->
                roleAuthMapRepository.deleteById(AliceRoleAuthMapPk(roleInfo.roleId, roleAuthMap.auth.authId))
            }
            roleRepository.deleteById(roleId)
            "Z-0000"
        } else {
            "E-0000"
        }
    }

    /**
     * 역할 정보 등록 한다.
     */
    @Transactional
    fun insertRole(roleInfo: RoleDto): String {
        val role = AliceRoleEntity(
            roleId = roleInfo.roleId.toString(),
            roleName = roleInfo.roleName.toString(),
            roleDesc = roleInfo.roleDesc.toString()
        )
        val result = roleRepository.save(role)

        authRepository.findByAuthIdIn(roleInfo.arrAuthId!!).forEach { auth ->
            roleAuthMapRepository.save(AliceRoleAuthMapEntity(role, auth))
        }

        return result.roleId
    }

    /**
     * 역할 정보 수정 한다.
     */
    @Transactional
    fun updateRole(roleInfo: RoleDto): String {
        val role = AliceRoleEntity(
            roleId = roleInfo.roleId.toString(),
            roleName = roleInfo.roleName.toString(),
            roleDesc = roleInfo.roleDesc.toString()
        )
        val result = roleRepository.save(role)

        roleRepository.findByRoleId(role.roleId).roleAuthMapEntities.forEach { roleAuthMap ->
            roleAuthMapRepository.deleteById(AliceRoleAuthMapPk(role.roleId, roleAuthMap.auth.authId))
        }
        authRepository.findByAuthIdIn(roleInfo.arrAuthId!!).forEach { auth ->
            roleAuthMapRepository.save(AliceRoleAuthMapEntity(role, auth))
        }

        return result.roleId
    }

    /**
     * 역할 상세 정보 조회
     */
    fun getRoleDetail(roleId: String): RoleDto {
        val roleInfo = roleRepository.findByRoleId(roleId)
        val userRoleMapCount = userRoleMapRepository.countByRole(roleInfo)
        val roleIds = mutableSetOf<String>()
        roleIds.add(roleId)
        val roleAuthMapList = roleAuthMapRepository.findAuthByRoles(roleIds)

        return RoleDto(
            roleInfo.roleId,
            roleInfo.roleName,
            roleInfo.roleDesc,
            roleInfo.createUser?.userName,
            roleInfo.createDt,
            roleInfo.updateUser?.userName,
            roleInfo.updateDt,
            null,
            roleAuthMapList,
            userRoleMapCount
        )
    }

    /**
     * 역할 목록을 조회 (검색어 포함).
     */
    fun getRoleSearchList(roleSearchCondition: RoleSearchCondition): RoleListReturnDto {
        val pagingResult = roleRepository.findRoleSearch(roleSearchCondition)

        return RoleListReturnDto(
            data = mapper.convertValue(pagingResult.dataList, object : TypeReference<List<RoleListDto>>() {}),
            paging = AlicePagingData(
                totalCount = pagingResult.totalCount,
                totalCountWithoutCondition = roleRepository.count(),
                currentPageNum = roleSearchCondition.pageNum,
                totalPageNum = ceil(pagingResult.totalCount.toDouble() / roleSearchCondition.contentNumPerPage.toDouble()).toLong(),
                orderType = PagingConstants.ListOrderTypeCode.CREATE_DESC.code
            )
        )
    }

    /**
     * 전체 역할 목록 조회
     */
    fun getAllRoleList(): MutableList<RoleListDto> {
        val allRoles = roleRepository.findAll()
        val roleList: MutableList<RoleListDto> = mutableListOf()

        allRoles.forEach {role ->
            val roleDto = RoleListDto(
                roleId = role.roleId,
                roleName = role.roleName,
                roleDesc = role.roleDesc
            )
            roleList.add(roleDto)
        }
        return roleList
    }

    /**
     * 사용자의 역할 조회
     */
    fun getUserRoleList(userKey: String): MutableList<RoleListDto> {
        return userRoleMapRepository.findUserRoleByUserKey(userKey)
    }

    /**
     * 역할 목록 Excel 다운로드
     */
    fun getRoleListExcelDownload(roleSearchCondition: RoleSearchCondition): ResponseEntity<ByteArray> {
        val roleList: List<RoleListDto> = mapper.convertValue(roleRepository.findRoleSearch(roleSearchCondition).dataList, object : TypeReference<List<RoleListDto>>() {})
        val excelVO = ExcelVO(
            sheets = mutableListOf(
                ExcelSheetVO(
                    rows = mutableListOf(
                        ExcelRowVO(
                            cells = listOf(
                                ExcelCellVO(value = aliceMessageSource.getMessage("role.label.name"), cellWidth = 5000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("role.label.id"), cellWidth = 8000),
                                ExcelCellVO(value = aliceMessageSource.getMessage("role.label.desc"), cellWidth = 5000)
                            )
                        )
                    )
                )
            )
        )
        roleList.forEach { result ->
            excelVO.sheets[0].rows.add(
                ExcelRowVO(
                    cells = mutableListOf(
                        ExcelCellVO(value = result.roleName),
                        ExcelCellVO(value = result.roleId),
                        ExcelCellVO(value = result.roleDesc ?: "")
                    )
                )
            )
        }
        return excelComponent.download(excelVO)
    }

    /**
     * 사용자(수정)에서 시스템 관리자 역할 존재 여부 판단
     *   1. 현재 데이터에 시스템 관리자 역할이 존재하는지 확인
     *   2. 사용자 전체에서 본인을 제외하고 시스템 관리자 역할이 존재하는지 확인
     *   3. 조직 중에 시스템 관리자 역할을 가지고 있는 조직을 조회한 후 해당 조직에 소속된 사용자가 존재하는지 확인
     *   4. EditSelf페이지 일 때, 사용자가 시스템 관리자 역할을 갖고 있는지 확인
     */
    fun isExistSystemRoleByUser(userKey: String, roleIds: Set<String>?): Boolean {
        var isExist = true
        roleIds?.let {
            isExist = roleIds.contains(AliceConstants.SYSTEM_ROLE)
            if (!isExist) {
                val userRoleListByNotSelfRole = mutableListOf<AliceUserRoleMapEntity>()
                userRoleMapRepository.findAll().forEach { userRole ->
                    if (userRole.user.userKey != userKey) {
                        userRoleListByNotSelfRole.add(userRole)
                    }
                }
                isExist = this.isExistSystemRoleByUserList(userRoleListByNotSelfRole)
                if (!isExist) {
                    isExist = isExistSystemRoleByOrganizationList(organizationRoleMapRepository.findAll())
                }
            }
        }
        return isExist
    }

    /**
     * 조직(수정)에서 시스템 관리자 역할 존재 여부 판단
     *   1. 현재 데이터에 시스템 관리자 역할이 존재하는지 확인
     *   2. 조직 중에 시스템 관리자 역할을 가지고 있는 조직을 조회한 후 해당 조직에 소속된 사용자가 존재하는지 확인
     *   3. 사용자 전체에서 시스템 관리자 역할이 존재하는지 확인
     */
    fun isExistSystemRoleByOrganization(organizationId: String, roleIds: Set<String>?): Boolean {
        var isExist = roleIds?.contains(AliceConstants.SYSTEM_ROLE) ?: false
        // 현재 데이터에 시스템 관리자가 역할이 존재할 경우 조직에 사용자가 존재하는지 체크
        if (isExist) {
            if (userRepository.getUserListInOrganization(setOf(organizationId)).isNullOrEmpty()) {
                isExist = false
            }
        }
        if (!isExist) {
            val organizationRoleListByNotSelfRole = mutableListOf<OrganizationRoleMapEntity>()
            organizationRoleMapRepository.findAll().forEach { organizationRole ->
                if (organizationRole.organization.organizationId != organizationId) {
                    organizationRoleListByNotSelfRole.add(organizationRole)
                }
            }
            isExist = isExistSystemRoleByOrganizationList(organizationRoleListByNotSelfRole)
            if (!isExist) {
                isExist = isExistSystemRoleByUserList(userRoleMapRepository.findAll())
            }
        }
        return isExist
    }

    /**
     * 사용자역할 목록에서 시스템 관리자 역할이 존재하는지 확인
     */
    private fun isExistSystemRoleByUserList(userRoleList: List<AliceUserRoleMapEntity>): Boolean {
        var isExist = false
        run loop@{
            userRoleList.forEach { userRole ->
                if (userRole.role.roleId == AliceConstants.SYSTEM_ROLE) {
                    isExist = true
                    return@loop
                }
            }
        }
        return isExist
    }

    /**
     * 조직역할 목록에서 시스템 관리자 역할이 포함된 조직에 사용자가 존재하는지 확인
     */
    private fun isExistSystemRoleByOrganizationList(organizationRoleList: List<OrganizationRoleMapEntity>): Boolean {
        var isExist = false
        val organizationIds = mutableSetOf<String>()
        organizationRoleList.forEach { organizationRole ->
            if (organizationRole.role.roleId == AliceConstants.SYSTEM_ROLE) {
                organizationIds.add(organizationRole.organization.organizationId)
            }
        }
        if (organizationIds.isNotEmpty()) {
            val organizationUserList = userRepository.getUserListInOrganization(organizationIds)
            if (organizationUserList.isNotEmpty()) {
                isExist = true
            }
        }
        return isExist
    }
}
