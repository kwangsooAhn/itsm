package co.brainz.itsm.role

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.RoleEntity
import co.brainz.itsm.auth.AuthEntity
import co.brainz.itsm.user.UserRoleMapEntity
import co.brainz.itsm.role.RoleRepository
import co.brainz.itsm.auth.AuthRepository
import co.brainz.itsm.user.UserRoleMapRepository
import co.brainz.itsm.role.RoleDetailDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
public class RoleService(
    private val roleRepository: RoleRepository,
    private val authRepository: AuthRepository,
    private val userRoleMapRepository: UserRoleMapRepository
) {
    /**
     * 상단 전체 역할정보를 가져온다.
     */
    public fun selectRoleList(): MutableList<RoleEntity> {
        return roleRepository.findByOrderByRoleNameAsc()
    }

    /**
     * 전체 권한정보를 가져온다.
     */
    public fun selectAuthList(): MutableList<AuthEntity> {
        return authRepository.findByOrderByAuthIdAsc()
    }

    /**
     * 역할 삭제 한다.
     */
    public fun deleteRole(roleId: String): String {
        var result = ""

        var userRoleMapCount = userRoleMapRepository.findByRoleId(roleId).count()
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
        
        val authIdList = mutableListOf<String>()
        var authIdArray = roleInfo.arrAuthId
        for (auth in authIdArray!!.indices) {
            authIdList.add(authIdArray[auth])
        }
        var authEntity = authRepository.findByAuthIdIn(authIdList)

        var inputDate = LocalDateTime.now()
        var result = roleRepository.save(
            RoleEntity(
                roleId = roleInfo.roleId.toString(),
                roleName = roleInfo.roleName.toString(),
                roleDesc = roleInfo.roleDesc.toString(),
                createUserid = roleInfo.createUserid.toString(),
                createDt = inputDate,
                authEntityList = authEntity
            )
        )
        return result.roleId
    }

    /**
     * 역할 정보 수정 한다.
     */
    public fun updateRole(roleInfo: RoleDto): String {

        var roleDetailInfo = roleRepository.findByRoleId(roleInfo.roleId.toString()).get(0)
        val authIdList = mutableListOf<String>()
        var authIdArray = roleInfo.arrAuthId
        for (auth in authIdArray!!.indices) {
            authIdList.add(authIdArray[auth])
        }
        var authEntity = authRepository.findByAuthIdIn(authIdList)

        var inputDate = LocalDateTime.now()
        var result = roleRepository.save(
            RoleEntity(
                roleId = roleInfo.roleId.toString(),
                roleName = roleInfo.roleName.toString(),
                roleDesc = roleInfo.roleDesc.toString(),
                createUserid = roleDetailInfo.createUserid.toString(),
                createDt = roleDetailInfo.createDt,
                updateUserid = roleInfo.updateUserid.toString(),
                updateDt = inputDate,
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
        var roleInfo = roleRepository.findByRoleId(roleId).get(0)
        var roleAuthMapList = roleInfo.authEntityList
        dto.add(
            RoleDto(
                roleInfo.roleId,
                roleInfo.roleName,
                roleInfo.roleDesc,
                roleInfo.createUserid,
                roleInfo.createDt,
                roleInfo.updateUserid,
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
    fun getRoles(roleEntities: Set<RoleEntity>?): MutableList<RoleDetailDto> {
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