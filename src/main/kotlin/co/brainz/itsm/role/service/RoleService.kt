package co.brainz.itsm.role.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.respository.ITSMRoleRepository
import co.brainz.itsm.role.respository.ITSMAuthRepository
import co.brainz.itsm.role.respository.ITSMUserRepository
import co.brainz.itsm.role.respository.ITSMRoleAuthMapRepository
import co.brainz.itsm.role.respository.ITSMUserRoleRepository
import co.brainz.itsm.role.entity.RoleEntity
import co.brainz.framework.auth.entity.UserEntity
import co.brainz.framework.auth.entity.AuthEntity
import co.brainz.itsm.role.entity.RoleAuthMapEntity
import co.brainz.itsm.role.entity.UserRoleMapEntity

@Service
public open class RoleService {

	companion object {
		private val logger = LoggerFactory.getLogger(RoleService::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "RoleService")
	}

	@Autowired
	lateinit var roleRepository: ITSMRoleRepository

	@Autowired
	lateinit var authRepository: ITSMAuthRepository

	@Autowired
	lateinit var userRepository: ITSMUserRepository

	@Autowired
	lateinit var roleAuthRepository: ITSMRoleAuthMapRepository

	@Autowired
	lateinit var userRoleRepository: ITSMUserRoleRepository

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
	public fun getRoleDetail(roleId: String): MutableList<RoleEntity> {
		return roleRepository.findByRoleId(roleId)
	}

	//역할별 권한 아이디를 가져온다.
	public fun getRoleAuthMapList(roleId: String): MutableList<RoleAuthMapEntity> {
		return roleAuthRepository.findByRoleIdOrderByAuthIdAsc(roleId)
	}

	//역할별 사용자 아이디를 가져온다.
	public fun getUserRoleMapList(roleId: String): MutableList<UserRoleMapEntity> {
		return userRoleRepository.findByRoleIdOrderByRoleIdAsc(roleId)
	}
}