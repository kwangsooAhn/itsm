package co.brainz.itsm.role

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.RoleEntity
import co.brainz.itsm.auth.AuthEntity
import co.brainz.itsm.user.UserEntity
import co.brainz.itsm.role.RoleRepository
import co.brainz.itsm.auth.AuthRepository
import co.brainz.itsm.user.UserRepository
import co.brainz.itsm.role.RoleDetailDto
import java.time.LocalDateTime

@Service
public class RoleService {

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var authRepository: AuthRepository

    @Autowired
    lateinit var userRepository: UserRepository

    //상단 역할정보를 가져온다.
    public fun getRoleList(): MutableList<RoleEntity> {
        return roleRepository.findByOrderByRoleNameAsc()
    }

    //전체 권한정보를 가져온다.
    public fun getAuthList(): MutableList<AuthEntity> {
        return authRepository.findByOrderByAuthIdAsc()
    }

    //역할별 역할 상세정보 조회
    public fun getRoleDetail(roleId: String): List<RoleEntity> {
        return roleRepository.findByRoleId(roleId)
    }

    //역할 저장
    public fun insertRole(roleEntity: RoleEntity): RoleEntity {
        return roleRepository.save(roleEntity)
    }

    //역할 삭제
    public fun deleteRole(roleId: String) {
        roleRepository.deleteById(roleId)
    }

    /**
     * 모든 역할 목록을 조회한다.
     */
    fun getRoles(roleEntities: Set<RoleEntity>?): MutableList<RoleDetailDto> {
        val dto = mutableListOf<RoleDetailDto>()
        val getRoles = roleRepository.findAll()
        getRoles.forEach {
            val rslt = roleEntities?.contains(it) ?: false
            dto.add(RoleDetailDto(it.roleId, it.roleName, rslt))
        }
        return dto
    }
}