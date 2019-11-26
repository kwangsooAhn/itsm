package co.brainz.itsm.role.service

import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired
import org.slf4j.LoggerFactory
import co.brainz.itsm.role.respository.ITSMRoleRepository
import co.brainz.itsm.role.entity.RoleEntity

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

	//전체 역할정보를 가져온다.
	public fun getRoleList(): MutableList<RoleEntity> {
		return roleRepository.findAll()
	}

	//전체 역할정보를 가져온다.
	public fun getAuthList(): MutableList<RoleEntity> {
		return roleRepository.findAll()
	}
	
	//전체 역할정보를 가져온다.
	public fun getUserFindIdList(): MutableList<RoleEntity> {
		return roleRepository.findAll()
	}
	
	
}