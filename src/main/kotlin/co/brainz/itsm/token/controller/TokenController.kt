/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.document.constants.DocumentConstants
import co.brainz.itsm.document.dto.DocumentSearchCondition
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.folder.service.FolderService
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.itsm.role.service.RoleService
import co.brainz.itsm.token.dto.TokenSearchCondition
import co.brainz.itsm.token.service.TokenService
import co.brainz.itsm.user.constants.UserConstants
import co.brainz.itsm.user.service.UserService
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/tokens")
class TokenController(
    private val userService: UserService,
    private val instanceService: InstanceService,
    private val folderService: FolderService,
    private val tokenService: TokenService,
    private val documentService: DocumentService,
    private val roleService: RoleService,
    private val currentSessionUser: CurrentSessionUser
) {
    private val statusPage: String = "redirect:/certification/status"
    private val tokenSearchPage: String = "token/tokenSearch"
    private val tokenListPage: String = "token/tokenList"
    private val tokenListFragment: String = "token/tokenList :: list"
    private val tokenEditPage: String = "token/tokenEdit"
    private val tokenViewPage: String = "token/tokenView"
    private val tokenPrintPage: String = "token/tokenPrint"
    private val tokenPopUpPage: String = "token/tokenPopUp"
    private val tokenInstanceListPage: String = "token/tokenInstanceList"
    private val tokenTabPage: String = "token/tokenTab"

    /**
     * ????????? ?????? ????????? ?????? ??????.
     * ???, ????????? ????????? SIGNUP??? ?????? ?????? ??????.
     *
     * @param request
     * @return String
     */
    @GetMapping("/search")
    fun getTokenSearch(request: HttpServletRequest, model: Model): String {
        val userKey = currentSessionUser.getUserKey()
        val userDto: AliceUserEntity = userService.selectUserKey(userKey)
        when (userDto.status) {
            UserConstants.UserStatus.SIGNUP.code,
            UserConstants.UserStatus.EDIT.code -> return statusPage
        }
        val documentSearchCondition = DocumentSearchCondition()
        val userRoles = roleService.getUserRoleList(userKey)
        run loop@{
            userRoles.forEach { role ->
                if (role.roleId == UserConstants.ADMIN_ID) {
                    documentSearchCondition.viewType = DocumentConstants.DocumentViewType.ADMIN.value
                    return@loop
                }
            }
        }
        model.addAttribute("documentList", documentService.getDocumentAll(documentSearchCondition))
        return tokenSearchPage
    }

    @GetMapping("")
    fun getTokenList(tokenSearchCondition: TokenSearchCondition, model: Model): String {
        val result = tokenService.getTokenList(tokenSearchCondition)
        model.addAttribute("tokenList", result.data)
        model.addAttribute("paging", result.paging)
        return tokenListPage
    }

    /**
     * ????????? ?????? ?????? ??????.
     *
     * @param tokenId
     * @return String
     */
    @GetMapping("{tokenId}/edit")
    fun getDocumentEdit(@PathVariable tokenId: String, model: Model): String {
        val instanceId = instanceService.getInstanceId(tokenId)!!
        model.addAttribute("tokenId", tokenId)
        model.addAttribute("folderId", folderService.getFolderId(tokenId))
        model.addAttribute("instanceId", instanceId)
        model.addAttribute("documentNo", instanceService.getInstance(instanceId).documentNo)
        return tokenEditPage
    }

    /**
     * ????????? ?????? ?????? ?????? : ?????? ??????.
     *
     * @param tokenId
     * @return String
     */
    @GetMapping("{tokenId}/view")
    fun getDocumentView(@PathVariable tokenId: String, model: Model): String {
        model.addAttribute("tokenId", tokenId)
        val instanceId = instanceService.getInstanceId(tokenId)!!
        model.addAttribute("folderId", folderService.getFolderId(tokenId))
        model.addAttribute("instanceId", instanceId)
        model.addAttribute("documentNo", instanceService.getInstance(instanceId).documentNo)
        return tokenViewPage
    }

    /**
     * ????????? ?????? ?????? ??????.
     */
    @GetMapping("/{tokenId}/print")
    fun getDocumentPrint(@PathVariable tokenId: String, model: Model, request: HttpServletRequest): String {
        model.addAttribute("time", LocalDateTime.now())
        return tokenPrintPage
    }

    /**
     * ???????????? ?????? ??????
     */
    @GetMapping("/{tokenId}/view-pop")
    fun getTokenPopUp(@PathVariable tokenId: String, model: Model): String {
        val folderId = folderService.getFolderId(tokenId)
        model.addAttribute("tokenId", tokenId)
        model.addAttribute("folderId", folderId)
        return tokenPopUpPage
    }

    /**
     * ?????? ?????? ????????? ??????
     */
    @GetMapping("/view-pop/documents")
    fun getAllInstanceListAndSearch(
        @RequestParam(value = "instanceId", defaultValue = "") instanceId: String,
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String,
        model: Model
    ): String {
        model.addAttribute(
            "instanceList",
            instanceService.findAllInstanceListByRelatedCheck(instanceId, searchValue)
        )
        return tokenInstanceListPage
    }

    @GetMapping("/tokenTab")
    fun getDocumentEditTab(model: Model): String {
        return tokenTabPage
    }
}
