package co.brainz.itsm.role.controller

import co.brainz.itsm.role.dto.RoleDto
import co.brainz.itsm.role.dto.RoleListDto
import co.brainz.itsm.role.service.RoleService
import javax.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 역할 관리 데이터 처리 클래스
 */
@RestController
@RequestMapping("/rest/roles")
class RoleRestController(private val roleService: RoleService) {

    private val logger = LoggerFactory.getLogger(RoleRestController::class.java)

    /**
     * 역할 전체 목록을 조회한다.
     */
    @GetMapping("/", "")
    fun getRoles(): MutableList<RoleListDto> {
        return roleService.selectRoleList()
    }

    /**
     * 역할 상세 정보를 조회한다.
     */
    @GetMapping("/{roleId}")
    fun getRoles(@PathVariable roleId: String): RoleDto {
        return roleService.selectDetailRoles(roleId)
    }

    /**
     * 역할 등록 한다.
     */
    @PostMapping("/", "")
    fun insertRole(@RequestBody @Valid role: RoleDto): String {
        return roleService.insertRole(role)
    }

    /**
     * 역할 수정 한다.
     */
    @PutMapping("/{roleId}")
    fun updateRole(@RequestBody @Valid role: RoleDto): String {
        return roleService.updateRole(role)
    }

    /**
     * 역할 삭제 한다.
     */
    @DeleteMapping("/{roleId}")
    fun deleteRole(@PathVariable roleId: String): String {
        return roleService.deleteRole(roleId)
    }
}
