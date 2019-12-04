package co.brainz.itsm.role.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.entity.ITSMRoleEntity
import co.brainz.itsm.role.entity.AuthEntity
import co.brainz.itsm.role.respository.ITSMRoleRepository
import co.brainz.itsm.role.respository.AuthRepository
import co.brainz.itsm.role.respository.ITSMUserRepository
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.entity.UserEntity

@Service
public open class ITSMRoleService {

	companion object {
		private val logger = LoggerFactory.getLogger(ITSMRoleService::class.java)
	}

	fun Logging(): Unit {
		logger.info("INFO{ }", "RoleService")
	}

	@Autowired
	lateinit var roleRepository: ITSMRoleRepository

	@Autowired
	lateinit var authRepository: AuthRepository

	@Autowired
	lateinit var userRepository: ITSMUserRepository

	//상단 역할정보를 가져온다.
	public fun getRoleList(): MutableList<ITSMRoleEntity> {
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
	public fun getRoleDetail(roleId: String): List<ITSMRoleEntity> {
		return roleRepository.findByRoleId(roleId)
	}

	//사용자 아이디를 조회한다.
	public fun getUserId(userid: String): MutableList<UserEntity> {
		return userRepository.findByUserId(userid)
	}

	//역할 저장
	public fun insertRole(roleEntity: ITSMRoleEntity) : ITSMRoleEntity {
		return roleRepository.save(roleEntity)
	}

	//역할 삭제
	public fun deleteRole(roleId: String) {
		roleRepository.deleteById(roleId)
	}
}