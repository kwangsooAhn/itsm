/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.role.controller

import co.brainz.itsm.role.dto.RoleSearchCondition
import co.brainz.itsm.role.service.RoleService
import javax.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/roles")
@Controller
class RoleController(private val roleService: RoleService) {

    private val logger = LoggerFactory.getLogger(RoleController::class.java)
    private val roleSearchPage: String = "role/roleSearch"
    private val rolePage: String = "role/role"
    private val roleListPage: String = "role/roleList"

    /**
     * 역할 검색 화면
     */
    @GetMapping("/search")
    fun getRoleSearch(request: HttpServletRequest, model: Model): String {
        return roleSearchPage
    }

    /**
     * 역할 검색 결과 리스트 화면
     */
    @GetMapping("")
    fun getRoleList(roleSearchCondition: RoleSearchCondition, model: Model): String {
        val result = roleService.getRoleSearchList(roleSearchCondition)
        model.addAttribute("roleList", result.data)
        model.addAttribute("paging", result.paging)
        return roleListPage
    }

    /**
     * 역할신규 등록 화면
     */
    @GetMapping("/new")
    fun getRoleNew(request: HttpServletRequest, model: Model): String {
        model.addAttribute("authList", roleService.selectAuthList())
        model.addAttribute("view", false)
        return rolePage
    }

    /**
     * 역할 편집 화면
     */
    @GetMapping("/{roleId}/edit")
    fun getRoleEdit(@PathVariable roleId: String, model: Model): String {
        model.addAttribute("role", roleService.getRoleDetail(roleId))
        model.addAttribute("authList", roleService.selectAuthList())
        model.addAttribute("view", false)
        return rolePage
    }

    /**
     * 역할 상세 조회 화면
     */
    @GetMapping("/{roleId}/view")
    fun getRoleView(@PathVariable roleId: String, model: Model): String {
        model.addAttribute("role", roleService.getRoleDetail(roleId))
        model.addAttribute("authList", roleService.selectAuthList())
        model.addAttribute("view", true)
        return rolePage
    }
}
