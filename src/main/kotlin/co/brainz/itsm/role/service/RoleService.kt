package co.brainz.itsm.role.service

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleAuthMapEntity
import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.framework.auth.repository.AliceAuthRepository
import co.brainz.framework.auth.repository.AliceRoleAuthMapRepository
import org.springframework.stereotype.Service
import co.brainz.itsm.role.dto.RoleDetailDto
import co.brainz.itsm.role.dto.RoleDto
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.framework.auth.dto.AliceAuthSimpleDto
import co.brainz.framework.auth.entity.AliceRoleAuthMapPk
import co.brainz.framework.auth.repository.AliceUserRoleMapRepository

@Service
class RoleService(
        private val roleRepository: RoleRepository,
        private val authRepository: AliceAuthRepository,
        private val roleAuthMapRepository: AliceRoleAuthMapRepository,
        private val userRoleMapRepository: AliceUserRoleMapRepository
) {
    /**
     * 상단 전체 역할정보를 가져온다.
     */
    fun selectRoleList(): MutableList<AliceRoleEntity> {
        return roleRepository.findByOrderByRoleNameAsc()
    }

    /**
     * 전체 권한정보를 가져온다.
     */
    fun selectAuthList(): MutableList<AliceAuthEntity> {
        return authRepository.findByOrderByAuthIdAsc()
    }

    /**
     * 역할 삭제 한다.
     */
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
    fun insertRole(roleInfo: RoleDto): String {
        val role = AliceRoleEntity(
                roleId = roleInfo.roleId.toString(),
                roleName = roleInfo.roleName.toString(),
                roleDesc = roleInfo.roleDesc.toString()
        )
        val result = roleRepository.save(role)

        authRepository.findByAuthIdIn(roleInfo.arrAuthId!!).forEach {auth ->
            roleAuthMapRepository.save(AliceRoleAuthMapEntity(role, auth))
        }

        return result.roleId
    }

    /**
     * 역할 정보 수정 한다.
     */
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
    fun selectDetailRoles(roleId: String): RoleDto {
        val roleInfo = roleRepository.findByRoleId(roleId)
        val authList = mutableListOf<AliceAuthSimpleDto>()
        val userRoleMapCount = userRoleMapRepository.findByRole(roleInfo).count()

        roleInfo.roleAuthMapEntities.forEach { roleAuthMap ->
            authList.add(AliceAuthSimpleDto(roleAuthMap.auth.authId, roleAuthMap.auth.authName, roleAuthMap.auth.authDesc))
        }

        return RoleDto(
            roleInfo.roleId,
            roleInfo.roleName,
            roleInfo.roleDesc,
            roleInfo.createUser?.userName,
            roleInfo.createDt,
            roleInfo.updateUser?.userName,
            roleInfo.updateDt,
            null,
            authList,
            userRoleMapCount
        )
    }

    /**
     * 모든 역할 목록을 조회한다.
     */
    fun getRoles(roleEntities: Set<AliceRoleEntity>?): MutableList<RoleDetailDto> {
        val dto = mutableListOf<RoleDetailDto>()
        if (roleEntities != null) {
            val getRoles = roleRepository.findAll()
            var i = 0
            for (allRole in getRoles) {
                for (dtoRole in roleEntities) {
                    if (dtoRole.roleId == allRole.roleId) {
                        i = 1
                        break
                    } else {
                        i = 0
                    }
                }
                if (i == 1) {
                    dto.add(RoleDetailDto(allRole.roleId, allRole.roleName, true))
                } else {
                    dto.add(RoleDetailDto(allRole.roleId, allRole.roleName, false))
                }
            }
        }
        return dto
    }

}
