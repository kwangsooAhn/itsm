/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.service

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.organization.repository.OrganizationRoleMapRepository
import co.brainz.framework.constants.PagingConstants
import co.brainz.framework.download.excel.ExcelComponent
import co.brainz.framework.download.excel.dto.ExcelCellVO
import co.brainz.framework.download.excel.dto.ExcelRowVO
import co.brainz.framework.download.excel.dto.ExcelSheetVO
import co.brainz.framework.download.excel.dto.ExcelVO
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.role.dto.RoleDetailDto
import co.brainz.itsm.role.dto.RoleDto
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.dto.RoleListReturnDto
import co.brainz.itsm.role.dto.RoleSearchCondition
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.repository.UserRepository
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
    private val organizationRepository: OrganizationRepository,
    private val organizationRoleMapRepository: OrganizationRoleMapRepository
) {
    /**
     * 상단 전체 역할정보를 가져온다.
     */
    fun selectRoleList(): RoleListReturnDto {
        val roleList = roleRepository.findRoleSearch(RoleSearchCondition(""))
        return RoleListReturnDto(
            data = roleList.results,
            paging = AlicePagingData(
                totalCount = roleList.total,
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
            "true"
        } else {
            "false"
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
        val queryResult = roleRepository.findRoleSearch(roleSearchCondition)

        return RoleListReturnDto(
            data = queryResult.results,
            paging = AlicePagingData(
                totalCount = queryResult.total,
                totalCountWithoutCondition = roleRepository.count(),
                currentPageNum = roleSearchCondition.pageNum,
                totalPageNum = ceil(queryResult.total.toDouble() / PagingConstants.COUNT_PER_PAGE.toDouble()).toLong(),
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
     * 부서에 부여된 권한이 있을 시 부여된 권한 조회
     */
    fun getOrganizationRolesList(userId: String): MutableList<RoleListDto> {
        val userOrganization = userRepository.findByUserId(userId).department.toString()
        val organizationIds = organizationRepository.findAll().map { it.organizationId }.toSet()
        val organizationRoleList: MutableList<RoleListDto> = mutableListOf()

        when (organizationIds.contains(userOrganization)) {
            true -> {
                val roleList: MutableList<RoleListDto> =
                    organizationRoleMapRepository.findRoleListByOrganizationId(userOrganization)
                for (organizationRole in roleList) {
                    organizationRoleList += organizationRole
                }
            }
        }
        return organizationRoleList
    }

    /**
     * 역할 목록 Excel 다운로드
     */
    fun getRoleListExcelDownload(roleSearchCondition: RoleSearchCondition): ResponseEntity<ByteArray> {
        val queryResult = roleRepository.findRoleSearch(roleSearchCondition)
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
        queryResult.results.forEach { result ->
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
}
