package co.brainz.itsm.role.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.entity.RoleEntity
import co.brainz.itsm.role.entity.AuthEntity
import co.brainz.itsm.role.entity.RoleAuthMapEntity
import co.brainz.itsm.role.entity.UserRoleMapEntity
import co.brainz.itsm.role.respository.RoleRepository
import co.brainz.itsm.role.respository.AuthRepository
import co.brainz.itsm.role.respository.UserRepository
import co.brainz.itsm.role.respository.RoleAuthMapRepository
import co.brainz.itsm.role.respository.UserRoleRepository
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.entity.UserEntity


@Service
public open class RoleService {

	companion object {
		private val logger = LoggerFactory.getLogger(RoleService::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "RoleService")
	}

	@Autowired
	lateinit var roleRepository: RoleRepository

	@Autowired
	lateinit var authRepository: AuthRepository

	@Autowired
	lateinit var userRepository: UserRepository

	@Autowired
	lateinit var userRoleRepository: UserRoleRepository

	@Autowired
	lateinit var roleAuthMapRepository: RoleAuthMapRepository

	//상단 역할정보를 가져온다.
	public fun getRoleList(): MutableList<RoleEntity> {
		return roleRepository.findByOrderByRoleNameAsc()
	}

	//전체 권한정보를 가져온다.
	public fun getAuthList(): MutableList<AuthEntity> {
		return authRepository.findByOrderByAuthIdAsc()
	}

	//역할별 권한를 가져온다.
	public fun getAuthRoleMapList(roleId: String): MutableList<RoleAuthMapEntity> {
		return roleAuthMapRepository.findByRoleIdOrderByRoleIdAsc(roleId)
	}

	//전체 사용자정보를 가져온다.
	public fun getUserList(): MutableList<AliceUserEntity> {
		return userRepository.findByOrderByUserIdAsc()
	}

	//역할별 역할 상세정보 조회
	public fun getRoleDetail(roleId: String): List<RoleEntity> {
		return roleRepository.findByRoleId(roleId)
	}

	//역할별 사용자 아이디를 가져온다.
	public fun getUserRoleMapList(roleId: String): MutableList<UserRoleMapEntity> {
		return userRoleRepository.findByRoleIdOrderByRoleIdAsc(roleId)
	}

	//사용자 아이디를 조회한다.
	public fun getUserId(userid: String): MutableList<UserEntity> {
		return userRepository.findByUserId(userid)
	}

	public fun insertRole(roleEntity: RoleEntity) {
		roleRepository.save(roleEntity)
	}
}