/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.service

import co.brainz.framework.organization.dto.OrganizationDetailDto
import co.brainz.framework.organization.dto.OrganizationListDto
import co.brainz.framework.organization.dto.OrganizationListReturnDto
import co.brainz.framework.organization.dto.OrganizationRoleDto
import co.brainz.framework.organization.dto.OrganizationSearchCondition
import co.brainz.framework.organization.entity.OrganizationEntity
import co.brainz.framework.organization.entity.OrganizationRoleMapEntity
import co.brainz.framework.organization.repository.OrganizationRepository
import co.brainz.framework.organization.repository.OrganizationRoleMapRepository
import co.brainz.framework.response.ZResponseConstants
import co.brainz.framework.response.dto.ZResponse
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.user.repository.UserRepository
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class OrganizationService(
    private val organizationRepository: OrganizationRepository,
    private val organizationRoleMapRepository: OrganizationRoleMapRepository,
    private val userRepository: UserRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val aliceMessageSource: AliceMessageSource,
    private val roleRepository: RoleRepository,
    private val roleService: RoleService
) {

    /**
     * 조직 전체 목록 조회
     */
    fun getOrganizationList(organizationSearchCondition: OrganizationSearchCondition): ZResponse {
        val treeOrganizationList = mutableListOf<OrganizationListDto>()
        val pOrganizationList = mutableListOf<OrganizationEntity>()
        val pagingResult: List<OrganizationEntity>
        if (organizationSearchCondition.searchValue != null) {
            pagingResult = organizationRepository.findByOrganizationSearchList(organizationSearchCondition)
        } else {
            pagingResult = organizationRepository.findOrganizationsByUseYn()
        }
        var organizationSearchList = pagingResult
        val count: Long = organizationSearchList.size.toLong()
        for (organization in organizationSearchList) {
            var tempOrganization = organization.pOrganization
            do {
                if (tempOrganization !== null) {
                    pOrganizationList.add(tempOrganization)
                    tempOrganization = tempOrganization.pOrganization
                }
            } while (tempOrganization !== null)
        }
        if (pOrganizationList.isNotEmpty()) {
            organizationSearchList += pOrganizationList
            organizationSearchList = organizationSearchList.distinct()
        }
        for (organization in organizationSearchList) {
            treeOrganizationList.add(
                OrganizationListDto(
                    organizationId = organization.organizationId,
                    pOrganizationId = organization.pOrganization?.organizationId,
                    organizationName = organization.organizationName,
                    organizationDesc = organization.organizationDesc,
                    useYn = organization.useYn,
                    level = organization.level,
                    seqNum = organization.seqNum,
                    editable = organization.editable,
                    createDt = organization.createDt,
                    createUserKey = organization.createUserKey,
                    updateDt = organization.updateDt,
                    updateUserKey = organization.updateUserKey
                )
            )
        }

        return ZResponse(
            data = OrganizationListReturnDto (
                data = treeOrganizationList,
                totalCount = count
            )
        )
    }

    /**
     * 조직 정보 상세 조회
     */
    fun getDetailOrganization(organizationId: String): OrganizationDetailDto {
        val organizationEntity = organizationRepository.findByOrganizationId(organizationId)
        return OrganizationDetailDto(
            organizationId = organizationEntity.organizationId,
            organizationName = organizationEntity.organizationName,
            pOrganizationId = organizationEntity.pOrganization?.let {
                organizationEntity.pOrganization!!.organizationId
            },
            pOrganizationName = organizationEntity.pOrganization?.let {
                organizationEntity.pOrganization!!.organizationName
            },
            organizationDesc = organizationEntity.organizationDesc,
            useYn = organizationEntity.useYn,
            level = organizationEntity.level,
            seqNum = organizationEntity.seqNum,
            editable = organizationEntity.editable,
            roles = organizationRoleMapRepository.findRoleListByOrganizationId(organizationId)
        )
    }

    /**
     * 조직 등록
     */
    @Transactional
    fun createOrganization(organizationRoleDto: OrganizationRoleDto): ZResponse {
        var organizationEntity = OrganizationEntity(
            organizationName = organizationRoleDto.organizationName,
            organizationDesc = organizationRoleDto.organizationDesc,
            useYn = organizationRoleDto.useYn,
            seqNum = organizationRoleDto.seqNum,
            createUserKey = currentSessionUser.getUserKey(),
            createDt = LocalDateTime.now()
        )

        if (!organizationRoleDto.pOrganizationId.isNullOrEmpty()) {
            val pOrganizationEntity = organizationRepository.findByOrganizationId(organizationRoleDto.pOrganizationId!!)
            organizationEntity.level = pOrganizationEntity.level?.plus(1)
            organizationEntity.pOrganization = pOrganizationEntity
        }
        organizationEntity = organizationRepository.save(organizationEntity)

        this.saveOrganizationRoleMap(organizationEntity, organizationRoleDto.roleIds)

        return ZResponse()
    }

    /**
     * 조직 정보 수정
     */
    @Transactional
    fun updateOrganization(organizationRoleDto: OrganizationRoleDto): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        if (!roleService.isExistSystemRoleByOrganization(
                organizationRoleDto.organizationId,
                organizationRoleDto.roleIds.toSet())) {
                    status = ZResponseConstants.STATUS.ERROR_ACCESS_DENY
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            val organizationEntity = organizationRepository.findByOrganizationId(organizationRoleDto.organizationId)
            organizationEntity.pOrganization =
                organizationRoleDto.pOrganizationId?.let { organizationRepository.findById(it).get() }
            organizationEntity.organizationName = organizationRoleDto.organizationName
            organizationEntity.organizationDesc = organizationRoleDto.organizationDesc
            organizationEntity.useYn = organizationRoleDto.useYn
            organizationEntity.seqNum = organizationRoleDto.seqNum
            organizationEntity.updateUserKey = currentSessionUser.getUserKey()
            organizationEntity.updateDt = LocalDateTime.now()

            if (organizationEntity.pOrganization == null) {
                organizationEntity.level = 0
            } else {
                organizationEntity.level = organizationEntity.pOrganization!!.level?.plus(1)
            }
            organizationRepository.save(organizationEntity)

            organizationRoleMapRepository.deleteByOrganization(organizationEntity)
            organizationRoleMapRepository.flush()

            this.saveOrganizationRoleMap(organizationEntity, organizationRoleDto.roleIds)
        }

        return ZResponse(
            status = status.code
        )
    }

    /**
     * 조직 정보 삭제
     */
    @Transactional
    fun deleteOrganization(organizationId: String): ZResponse {
        var status = ZResponseConstants.STATUS.SUCCESS
        // 조직에 구성원이 있는지 확인
        if (userRepository.existsByDepartment(organizationId)) {
            status = ZResponseConstants.STATUS.ERROR_EXIST
        }
        // 하위 부서가 있는지 확인
        if (organizationRepository.existsByPOrganizationId(organizationId)) {
            status = ZResponseConstants.STATUS.ERROR_DUPLICATE_ORGANIZATION
        }
        if (status == ZResponseConstants.STATUS.SUCCESS) {
            // 조직에 설정된 역할 삭제
            organizationRoleMapRepository.deleteByOrganization(OrganizationEntity(organizationId))
            // 조직 삭제
            organizationRepository.deleteByOrganizationId(organizationId)
        }
        return ZResponse(
            status = status.code
        )
    }

    /**
     * 조직 역할 저장
     */
    private fun saveOrganizationRoleMap(organizationEntity: OrganizationEntity, roles: List<String>) {
        if (roles.isNotEmpty()) {
            val organizationRoleList = mutableListOf<OrganizationRoleMapEntity>()
            val roleEntityList = roleRepository.findByRoleIdIn(roles)
            roleEntityList.forEach { role ->
                organizationRoleList.add(
                    OrganizationRoleMapEntity(organizationEntity, role)
                )
            }
            if (organizationRoleList.isNotEmpty()) {
                organizationRoleMapRepository.saveAll(organizationRoleList)
            }
        }
    }

    /**
     *  상위부서 조직명 recursive 조회
     */
    fun getOrganizationParent(
        organization: OrganizationEntity,
        organizationList: List<OrganizationEntity>,
        organizationName: MutableList<String>
    ): MutableList<String> {
        organizationName.add(organization.organizationName.toString())
        if (organization.pOrganization != null) {
            val pOrganization = organizationList.firstOrNull {
                it.organizationId == organization.pOrganization!!.organizationId
            }
            if (pOrganization != null) {
                this.getOrganizationParent(pOrganization, organizationList, organizationName)
            }
        }
        return organizationName
    }

    /**
     *  하위부서 조직명 recursive 조회
     */
    fun getOrganizationChildren(
        organization: OrganizationEntity,
        organizationList: List<OrganizationEntity>,
        organizationName: MutableList<String>
    ): List<String> {
        organizationName.add(organization.organizationName.toString())
        val cOrganization = organizationRepository.findByPOrganization(organization)
        if (cOrganization.isNotEmpty()) {
            cOrganization.forEach {
                organizationName.add(it.organizationName.toString())
                this.getOrganizationChildren(it, cOrganization, organizationName)
            }
        }
        return organizationName.distinct()
    }
}
