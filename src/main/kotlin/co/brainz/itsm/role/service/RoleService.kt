package co.brainz.itsm.role.service

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceRoleEntity
import co.brainz.itsm.auth.repository.AuthRepository
import org.springframework.stereotype.Service
import co.brainz.itsm.role.dto.RoleDetailDto
import co.brainz.itsm.role.dto.RoleDto
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.repository.UserRoleMapRepository

@Service
public class RoleService(
        private val roleRepository: RoleRepository,
        private val authRepository: AuthRepository,
        private val userRoleMapRepository: UserRoleMapRepository
) {
    /**
     * 상단 전체 역할정보를 가져온다.
     */
    public fun selectRoleList(): MutableList<AliceRoleEntity> {
        return roleRepository.findByOrderByRoleNameAsc()
    }

    /**
     * 전체 권한정보를 가져온다.
     */
    public fun selectAuthList(): MutableList<AliceAuthEntity> {
        return authRepository.findByOrderByAuthIdAsc()
    }

    /**
     * 역할 삭제 한다.
     */
    public fun deleteRole(roleId: String): String {
        var result = ""

        val userRoleMapCount = userRoleMapRepository.findByRoleId(roleId).count()
        if (userRoleMapCount == 0) {
            roleRepository.deleteById(roleId)
            result = "true"
        } else {
            result = "PlaseDeleteMapperUser"
        }

        return result
    }

    /**
     * 역할 정보 등록 한다.
     */
    public fun insertRole(roleInfo: RoleDto): String {
        val authEntity = authRepository.findByAuthIdIn(roleInfo.arrAuthId!!)
        val roleEntity = AliceRoleEntity(
                roleId = roleInfo.roleId.toString(),
                roleName = roleInfo.roleName.toString(),
                roleDesc = roleInfo.roleDesc.toString(),
                authEntityList = authEntity
        )
        val result = roleRepository.save(roleEntity)
        return result.roleId
    }

    /**
     * 역할 정보 수정 한다.
     */
    public fun updateRole(roleInfo: RoleDto): String {
        val authEntity = authRepository.findByAuthIdIn(roleInfo.arrAuthId!!)
        val result = roleRepository.save(
                AliceRoleEntity(
                        roleId = roleInfo.roleId.toString(),
                        roleName = roleInfo.roleName.toString(),
                        roleDesc = roleInfo.roleDesc.toString(),
                        authEntityList = authEntity
                )
        )
        return result.roleId
    }

    /**
     * 역할 상세 정보 조회
     */
    fun selectDetailRoles(roleId: String): List<RoleDto> {
        val dto = mutableListOf<RoleDto>()
        val roleInfo = roleRepository.findByRoleId(roleId).get(0)
        val roleAuthMapList = roleInfo.authEntityList
        dto.add(
                RoleDto(
                        roleInfo.roleId,
                        roleInfo.roleName,
                        roleInfo.roleDesc,
                        roleInfo.createUserkey,
                        roleInfo.createDt,
                        roleInfo.updateUserkey,
                        roleInfo.updateDt,
                        null,
                        roleAuthMapList
                )
        )
        return dto
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
            /*getRoles.forEach {
                val rslt = roleEntities.
                //val rslt = roleEntities?.contains(it) ?: false
                dto.add(RoleDetailDto(it.roleId, it.roleName, rslt))
            }*/
        }
        return dto
    }

}
