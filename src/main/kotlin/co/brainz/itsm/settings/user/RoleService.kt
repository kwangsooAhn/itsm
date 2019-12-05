package co.brainz.itsm.settings.user

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService {

    @Autowired
    lateinit var roleRepository: RoleRepository

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