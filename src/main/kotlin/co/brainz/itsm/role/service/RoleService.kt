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
import co.brainz.framework.auth.repository.AliceDocumentRoleMapRepository
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
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.AlicePagingData
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentDto
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
    private val documentRoleMapRepository: AliceDocumentRoleMapRepository,
    private val roleAuthMapRepository: AliceRoleAuthMapRepository,
    private val userRoleMapRepository: AliceUserRoleMapRepository,
    private val excelComponent: ExcelComponent,
    private val userRepository: UserRepository,
    private val organizationRoleMapRepository: OrganizationRoleMapRepository
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * ?????? ?????? ??????????????? ????????????.
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
     * ?????? ??????????????? ????????????.
     */
    fun selectAuthList(): List<AliceAuthEntity> {
        return authRepository.findAllByOrderByAuthName()
    }

    /**
     * ?????? ?????? ??????.
     */
    @Transactional
    fun deleteRole(roleId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        val roleInfo = roleRepository.findByRoleId(roleId)
        val userRoleMapCount = userRoleMapRepository.findByRole(roleInfo).count()
        if (userRoleMapCount == 0) {
            roleInfo.roleAuthMapEntities.forEach { roleAuthMap ->
                roleAuthMapRepository.deleteById(AliceRoleAuthMapPk(roleInfo.roleId, roleAuthMap.auth.authId))
            }
            roleRepository.deleteById(roleId)
        } else {
            status = ZResponseConstants.STATUS.ERROR_FAIL
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * ?????? ?????? ?????? ??????.
     */
    @Transactional
    fun insertRole(roleInfo: RoleDto): ZResponse {
        val role = AliceRoleEntity(
            roleId = roleInfo.roleId.toString(),
            roleName = roleInfo.roleName.toString(),
            roleDesc = roleInfo.roleDesc.toString()
        )
        val result = roleRepository.save(role)

        authRepository.findByAuthIdIn(roleInfo.arrAuthId!!).forEach { auth ->
            roleAuthMapRepository.save(AliceRoleAuthMapEntity(role, auth))
        }
        return ZResponse()
    }

    /**
     * ?????? ?????? ?????? ??????.
     */
    @Transactional
    fun updateRole(roleInfo: RoleDto): ZResponse {
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

        return ZResponse()
    }

    /**
     * ?????? ?????? ?????? ??????
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
     * ?????? ????????? ?????? (????????? ??????).
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
     * ?????? ?????? ?????? ??????
     */
    fun getAllRoleList(): MutableList<RoleListDto> {
        val allRoles = roleRepository.findAllByOrderByRoleNameAsc()
        val roleList: MutableList<RoleListDto> = mutableListOf()

        allRoles.forEach { role ->
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
     * ???????????? ?????? ??????
     */
    fun getUserRoleList(userKey: String): MutableList<RoleListDto> {
        return userRoleMapRepository.findUserRoleByUserKey(userKey)
    }

    /**
     * ???????????? ?????? ??????
     */
    fun getDocumentRoleList(document: DocumentDto): MutableList<RoleListDto> {
        return documentRoleMapRepository.findRoleByDocumentId(document.documentId, document.documentType)
    }

    /**
     * ????????? ????????? ?????? ??????
     */
    fun getDocumentLinkRoleList(documentId: String): MutableList<RoleListDto> {
        return documentRoleMapRepository.findRoleByDocumentId(documentId, DocumentConstants.DocumentType.APPLICATION_FORM_LINK.value)
    }

    /**
     * ?????? ?????? Excel ????????????
     */
    fun getRoleListExcelDownload(roleSearchCondition: RoleSearchCondition): ResponseEntity<ByteArray> {
        val roleList: List<RoleListDto> =
            mapper.convertValue(roleRepository.findRoleSearch(roleSearchCondition).dataList, object : TypeReference<List<RoleListDto>>() {})
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
     * ?????????(??????)?????? ????????? ????????? ?????? ?????? ?????? ??????
     *   1. ?????? ???????????? ????????? ????????? ????????? ??????????????? ??????
     *   2. ????????? ???????????? ????????? ???????????? ????????? ????????? ????????? ??????????????? ??????
     *   3. ?????? ?????? ????????? ????????? ????????? ????????? ?????? ????????? ????????? ??? ?????? ????????? ????????? ???????????? ??????????????? ??????
     *   4. EditSelf????????? ??? ???, ???????????? ????????? ????????? ????????? ?????? ????????? ??????
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
     * ??????(??????)?????? ????????? ????????? ?????? ?????? ?????? ??????
     *   1. ?????? ???????????? ????????? ????????? ????????? ??????????????? ??????
     *   2. ?????? ?????? ????????? ????????? ????????? ????????? ?????? ????????? ????????? ??? ?????? ????????? ????????? ???????????? ??????????????? ??????
     *   3. ????????? ???????????? ????????? ????????? ????????? ??????????????? ??????
     */
    fun isExistSystemRoleByOrganization(organizationId: String, roleIds: Set<String>?): Boolean {
        var isExist = roleIds?.contains(AliceConstants.SYSTEM_ROLE) ?: false
        // ?????? ???????????? ????????? ???????????? ????????? ????????? ?????? ????????? ???????????? ??????????????? ??????
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
     * ??????????????? ???????????? ????????? ????????? ????????? ??????????????? ??????
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
     * ???????????? ???????????? ????????? ????????? ????????? ????????? ????????? ???????????? ??????????????? ??????
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
