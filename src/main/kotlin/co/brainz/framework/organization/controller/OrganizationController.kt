/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.organization.controller

import co.brainz.itsm.role.dto.RoleSearchCondition
import co.brainz.itsm.role.service.RoleService
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/organizations")
class OrganizationController(
    private val roleService: RoleService
) {

    private val organizationEditPage: String = "organization/organizationEdit"

    /**
     * 조직 관리 화면 호출
     */
    @GetMapping("/edit")
    fun getOrganizationList(request: HttpServletRequest, model: Model): String {
        model.addAttribute("roleList", roleService.getRoleSearchList(RoleSearchCondition()).data)
        return organizationEditPage
    }
}
