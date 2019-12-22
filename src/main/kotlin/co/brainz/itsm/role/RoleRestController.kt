package co.brainz.itsm.user

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.ui.Model
import co.brainz.itsm.role.RoleService
import co.brainz.itsm.role.RoleEntity
import co.brainz.itsm.role.RoleDto
import co.brainz.itsm.auth.AuthEntity

/**
 * 역할 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/roles")
class RoleRestController {

    private val logger = LoggerFactory.getLogger(RoleRestController::class.java)
    
    var roleService: RoleService
    constructor(roleService: RoleService) {
        this.roleService = roleService
    }

    //역할 전체 목록을 조회한다.
    @GetMapping("/", "")
    fun getRoles(): MutableList<RoleEntity> {
        return roleService.selectRoleList()
    }

    //역할 상세 정보를 조회한다.
    @GetMapping("/{roleId}")
    fun getRoles(@PathVariable roleId: String): List<RoleDto> {
        return roleService.selectDetailRoles(roleId)
    }

    //역할 등록 한다.
    @PostMapping("/")
    fun insertRole(@RequestBody role: RoleDto): String {
        return roleService.saveRole(role)
    }

    //역할 수정한다.
    @PutMapping("/{roleId}")
    fun updateRole(@RequestBody role: RoleDto): String {
        return roleService.saveRole(role)
    }

    //역할 삭제한다.
    @DeleteMapping("/{roleId}")
    fun deleteRole(@PathVariable roleId: String): String {
        var result = roleService.deleteRole(roleId)
        return result
    }
}