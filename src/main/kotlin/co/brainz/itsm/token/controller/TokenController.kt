package co.brainz.itsm.token.controller

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.itsm.document.service.DocumentService
import co.brainz.itsm.folder.service.FolderService
import co.brainz.itsm.instance.service.InstanceService
import co.brainz.itsm.user.service.UserService
import co.brainz.workflow.provider.dto.RestTemplateDocumentSearchListDto
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
@RequestMapping("/tokens")
class TokenController(
    private val userService: UserService,
    private val instanceService: InstanceService,
    private val folderService: FolderService,
    private val documentService: DocumentService
) {

    private val statusPage: String = "redirect:/certification/status"
    private val tokenSearchPage: String = "token/tokenSearch"
    private val tokenEditPage: String = "token/tokenEdit"
    private val tokenPrintPage: String = "token/tokenPrint"
    private val tokenPopUpPage: String = "/token/tokenPopUp"
    private val tokenInstanceListPage: String = "/token/tokenInstanceList"

    /**
     * 처리할 문서 리스트 호출 화면.
     * 단, 사용자 상태가 SIGNUP인 경우 인증 화면.
     *
     * @param request
     * @return String
     */
    @GetMapping("/search")
    fun getTokenSearch(request: HttpServletRequest, model: Model): String {
        // 사용자 상태가 SIGNUP 인 경우 인증 화면으로 이동
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userKey = aliceUserDto.userKey
        val userDto: AliceUserEntity = userService.selectUserKey(userKey)
        if (userDto.status == AliceUserConstants.Status.SIGNUP.code || userDto.status == AliceUserConstants.Status.EDIT.code) {
            return statusPage
        }
        model.addAttribute("fromDt", LocalDateTime.now().minusMonths(1))
        model.addAttribute("toDt", LocalDateTime.now())
        val restTemplateDocumentSearchListDto = RestTemplateDocumentSearchListDto()
        model.addAttribute("documentList", documentService.getDocumentList(restTemplateDocumentSearchListDto))
        return tokenSearchPage
    }

    /**
     * 처리할 문서 상세 화면.
     *
     * @param tokenId
     * @return String
     */
    @GetMapping("{tokenId}/edit")
    fun getDocumentEdit(@PathVariable tokenId: String, model: Model): String {
        model.addAttribute("tokenId", tokenId)
        model.addAttribute("instanceHistory", instanceService.getInstanceHistory(tokenId))
        model.addAttribute("relatedInstance", folderService.getRelatedInstance(tokenId))
        val instanceId = instanceService.getInstanceId(tokenId)!!
        val folderId = folderService.getFolderId(tokenId)
        model.addAttribute("folderId", folderId)
        model.addAttribute("instanceId", instanceId)
        model.addAttribute("commentList", instanceService.getInstanceComments(instanceId))
        model.addAttribute("tagList", instanceService.getInstanceTags(instanceId))
        return tokenEditPage
    }

    /**
     * 처리할 문서 인쇄 화면.
     */
    @PostMapping("/{tokenId}/print")
    fun getDocumentPrint(@PathVariable tokenId: String, model: Model, request: HttpServletRequest): String {
        model.addAttribute("data", request.getParameter("data") ?: "")
        model.addAttribute("instanceHistory", instanceService.getInstanceHistory(tokenId))
        return tokenPrintPage
    }

    /**
     * 관련문서 팝업 생성
     */
    @GetMapping("/{tokenId}/view-pop")
    fun getTokenPopUp(@PathVariable tokenId: String, model: Model): String {
        val folderId = folderService.getFolderId(tokenId)
        model.addAttribute("tokenId", tokenId)
        model.addAttribute("folderId", folderId)
        return tokenPopUpPage
    }

    /**
     * 관련문서 팝업 문서 리스트 출력
     */
    @GetMapping("/view-pop/list")
    fun getAllInstanceListAndSearch(
        @RequestParam(value = "tokenId", defaultValue = "") tokenId: String,
        @RequestParam(value = "searchValue", defaultValue = "") searchValue: String,
        model: Model
    ): String {
        val instanceId = instanceService.getInstanceId(tokenId)!!

        model.addAttribute("instanceList", instanceService.getAllInstanceListAndSearch(instanceId, searchValue))
        return tokenInstanceListPage
    }
}
