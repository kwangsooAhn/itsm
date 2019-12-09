package co.brainz.itsm.role.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.entity.RoleEntity
import co.brainz.itsm.role.entity.AuthEntity
import co.brainz.itsm.settings.user.UserEntity
import co.brainz.itsm.role.respository.RoleRepository
import co.brainz.itsm.role.respository.AuthRepository
import co.brainz.itsm.settings.user.UserRepository
import co.brainz.itsm.settings.user.RoleDetailDto
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

    //전체 사용자정보를 가져온다.
    public fun getUserList(): MutableList<UserEntity> {
        return userRepository.findByOrderByUserIdAsc()
    }

    //역할별 역할 상세정보 조회
    public fun getRoleDetail(roleId: String): List<RoleEntity> {
        return roleRepository.findByRoleId(roleId)
    }

    //사용자 아이디를 조회한다.
    public fun getUserId(userid: String): UserEntity {
        try {
            var userEntityList = mutableListOf<UserEntity>()
            //getOne을 사용하는 이유는 조회가 되지 않을때 exception 처리하기 위해서 사용했다.
            //UserEntity로 보내게 되면...오류가 발생.
            userEntityList.add(userRepository.getOne(userid))
            return UserEntity(
                userId = userEntityList[0].userId,
                password = userEntityList[0].password,
                userName = userEntityList[0].userName,
                email = userEntityList[0].email,
                useYn = userEntityList[0].useYn,
                tryLoginCount = userEntityList[0].tryLoginCount,
                position = userEntityList[0].position,
                department = userEntityList[0].department,
                extensionNumber = userEntityList[0].extensionNumber,
                createId = userEntityList[0].createId,
                updateId = userEntityList[0].updateId,
                expiredDt = userEntityList[0].expiredDt,
                createDate = userEntityList[0].createDate,
                updateDate = userEntityList[0].updateDate,
                roleEntities = null
            )
        } catch (e: Exception) {
            var dummyDate = LocalDateTime.now()
            return UserEntity(
                userId = "",
                password = "",
                userName = "",
                email = "",
                useYn = false,
                tryLoginCount = 0,
                position = "",
                department = "",
                extensionNumber = "",
                createId = "",
                updateId = "",
                expiredDt = dummyDate,
                createDate = dummyDate,
                updateDate = dummyDate,
                roleEntities = null
            )
        }
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