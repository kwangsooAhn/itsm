package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.provider.RestTemplateProvider
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DocumentActionService(
    private val restTemplate: RestTemplateProvider,
    private val userRepository: UserRepository
) {

    /**
     *  documentData 받아서 버튼 정리해서 반환한다.
     * @return String
     */
    fun makeDocumentAction(documentData: String): String {
        val documentJsonData: JsonObject = JsonParser().parse(documentData).asJsonObject
        val documentActions = documentJsonData.get("actions").asJsonArray
        val actionsResult = JsonArray()

        documentActions.forEach { actions ->
            val actionsValue = actions.asJsonObject.get("value").asString
            if (actionsValue != WfElementConstants.Action.REJECT.value &&
                actionsValue != WfElementConstants.Action.WITHDRAW.value &&
                actionsValue != WfElementConstants.Action.CANCEL.value &&
                actionsValue != WfElementConstants.Action.TERMINATE.value
            ) {
                actionsResult.add(actions)
            }
        }

        if (actionsResult.size() > 0) {
            documentJsonData.remove("actions")
            documentJsonData.add("actions", actionsResult)
        }

        return Gson().toJson(documentJsonData)
    }

    /**
     *  TokenData 받아서 버튼 정리해서 반환한다.
     * @return String
     */
    fun makeTokenAction(tokensData: String): String {
        //버튼 보여주기
        var isSave = false
        var isProcess = false
        var isReject = false
        var isWithDraw = false
        var isCancel = false
        var isTerminate = false
        //담당자 확인
        var isAssignee = false
        //현재 진행중 문서 확인
        var isTokenStatusIng = false
        //반환할 버튼 정보
        val actionsResult = JsonArray()
        //처리를 간편하게 처리 하기 위해서 json으로 변경한다.
        val tokensJsonData: JsonObject = JsonParser().parse(tokensData).asJsonObject
        //버튼 정보를 구한다.
        val tokensActions = tokensJsonData.get("actions").asJsonArray

        //token에 대한 정보를 구한다.
        val tokenId = tokensJsonData.get("tokenId").asString
        val tokenDataUrl = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN.url.replace(restTemplate.getKeyRegex(), tokenId)
        )

        val tokenDetailJsonData: JsonObject = JsonParser().parse(restTemplate.get(tokenDataUrl)).asJsonObject
        //해당 문서의 상태 값
        val tokenStatus = tokenDetailJsonData.get("tokenStatus").asString
        /*if (tokenStatus == "token.status.running" || tokenStatus == "token.status.reject" ||tokenStatus == "token.status.withdraw") {*/
        if (tokenStatus == RestTemplateConstants.TokenStatus.RUNNING.value) {
            isTokenStatusIng = true
        }
        //문서를 연 사용자 정보
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userRepository.findByUserKey(aliceUserDto.userKey)
        //처리자 취소, 종결 권한 확인
        userEntity.userRoleMapEntities.forEach { aliceUserRoleMapEntity ->
            aliceUserRoleMapEntity.role.roleAuthMapEntities.forEach { aliceRoleAuthMapEntity ->
                if (isTokenStatusIng) {
                    if (aliceRoleAuthMapEntity.auth.authId == "action.cancel") {
                        isCancel = true
                    }
                    if (aliceRoleAuthMapEntity.auth.authId == "action.terminate") {
                        isTerminate = true
                    }
                }
            }
        }

        //Token ElementData
        val tokenElementDataUrl = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN_ELEMENT_DATA.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        val tokenElementDataJsonData: JsonObject =
            JsonParser().parse(restTemplate.get(tokenElementDataUrl)).asJsonObject
        tokenElementDataJsonData.get("attributeData").asJsonArray.forEach { element ->
            when (element.asJsonObject.get("attributeId").asString) {
                WfElementConstants.AttributeId.ASSIGNEE_TYPE.value -> {
                    //문서 처리자 정보
                    val tokenAssigneeUrl = RestTemplateUrlDto(
                        callUrl = RestTemplateConstants.Token.GET_TOKEN_ASSIGNEE.url.replace(
                            restTemplate.getKeyRegex(),
                            tokenId
                        )
                    )
                    val tokenAssigneesJsonData: JsonObject =
                        JsonParser().parse(restTemplate.get(tokenAssigneeUrl)).asJsonObject
                    val assignees = tokenAssigneesJsonData.get("assignees").asJsonArray
                    val assigneeType = tokenAssigneesJsonData.get("type").asString
                    if (assigneeType == WfTokenConstants.AssigneeType.ASSIGNEE.code) {
                        if (assignees.asString == userEntity.userKey) {
                            isAssignee = true
                        }
                    } else if (assigneeType == WfTokenConstants.AssigneeType.USERS.code) {
                        assignees.forEach { assigneesUserKeys ->
                            if (assigneesUserKeys.asString == userEntity.userKey) {
                                isAssignee = true
                            }
                        }
                    } else if (assigneeType == WfTokenConstants.AssigneeType.GROUPS.code) {
                        userEntity.userRoleMapEntities.forEach { aliceUserRoleMapEntity ->
                            assignees.forEach { assigneesRoleIds ->
                                if (assigneesRoleIds.asString == aliceUserRoleMapEntity.role.roleId) {
                                    isAssignee = true
                                }
                            }
                        }
                    }
                    if (isTokenStatusIng && isAssignee) {
                        isSave = true
                        isProcess = true
                    }
                }
                WfElementConstants.AttributeId.REJECT_ID.value -> {
                    if (isTokenStatusIng && isAssignee && !element.asJsonObject.get("attributeValue").isJsonNull) {
                        isReject = true
                    }
                }
                WfElementConstants.AttributeId.WITHDRAW.value -> {
                    if (isTokenStatusIng && isAssignee && (element.asJsonObject.get("attributeValue").asString == "Y")) {
                        isWithDraw = true
                    }
                }
            }
        }

        tokensActions.forEach { actions ->
            when (actions.asJsonObject.get("value").asString) {
                WfElementConstants.Action.SAVE.value -> {
                    if (isSave) {
                        print("SAVE")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.PROCESS.value -> {
                    if (isProcess) {
                        print("PROCESS")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.REJECT.value -> {
                    if (isReject) {
                        print("REJECT")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.WITHDRAW.value -> {
                    if (isWithDraw) {
                        print("WITHDRAW")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.CANCEL.value -> {
                    if (isCancel) {
                        print("CANCEL")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.TERMINATE.value -> {
                    if (isTerminate) {
                        print("TERMINATE")
                        actionsResult.add(actions)
                    }
                }
                else -> {
                    if (isTokenStatusIng && isAssignee) {
                        actionsResult.add(actions)
                    }
                }
            }
        }

        if (actionsResult.size() > 0) {
            tokensJsonData.remove("actions")
            tokensJsonData.add("actions", actionsResult)
        }

        return Gson().toJson(tokensJsonData)
    }

}
