package co.brainz.itsm.document.service

import co.brainz.framework.auth.constants.AuthConstants
import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.provider.dto.RestTemplateActionDto
import co.brainz.workflow.provider.dto.RestTemplateRequestDocumentDto
import co.brainz.workflow.token.constants.WfTokenConstants
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class DocumentActionService(
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    /**
     * [documentData]을 받아서 필요 버튼을 정리 한 후[String]으로 반환 한다.
     */
    fun makeDocumentAction(documentData: RestTemplateRequestDocumentDto): RestTemplateRequestDocumentDto {
        val actionsResult = mutableListOf<RestTemplateActionDto>()
        documentData.actions?.forEach { action ->
            val actionValue = action.value
            if (actionValue != WfElementConstants.Action.REJECT.value &&
                actionValue != WfElementConstants.Action.WITHDRAW.value &&
                actionValue != WfElementConstants.Action.CANCEL.value &&
                actionValue != WfElementConstants.Action.TERMINATE.value
            ) {
                actionsResult.add(action)
            }
        }
        if (actionsResult.isNotEmpty()) {
            documentData.actions = actionsResult
        }
        return documentData
    }

    /**
     * [tokensData] 받아서 필요 버튼을 정리 한 후[String]으로 반환 한다.
     */
    fun makeTokenAction(tokensData: String): String {
        val tokenObject = JsonParser().parse(tokensData).asJsonObject
        // 문서를 연 사용자 정보
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userRepository.findByUserKey(aliceUserDto.userKey)
        if (tokenObject.isJsonObject) {
            // 현재 진행중 문서 확인
            val isProgress = this.checkTokenStatus(tokenObject)
            // 담당자 인지 확인
            val isAssignee = this.checkAssignee(tokenObject, userEntity)
            // 반환할 버튼 정보
            val actionsResult = this.getActionList(tokenObject, userEntity, isProgress, isAssignee)
            // 문서에 회수가 존재하지 않으면, 모든 components에 대하여 displayType을 'readonly'로 수정한다.
            val components = tokenObject.get("form").asJsonObject.get("components").asJsonArray
            val revokeAssignee = tokenObject.get("stakeholders").asJsonObject.get("revokeAssignee")
            if (!isAssignee) {
                for (action in actionsResult) {
                    if (action.asJsonObject.get("value").asString == WfElementConstants.Action.CANCEL.value ||
                        action.asJsonObject.get("value").asString == WfElementConstants.Action.TERMINATE.value ||
                        action.asJsonObject.get("value").asString == WfElementConstants.Action.REJECT.value ||
                        action.asJsonObject.get("value").asString == WfElementConstants.Action.WITHDRAW.value
                    ) {
                        for (component in components) {
                            component.asJsonObject.get("dataAttribute").asJsonObject.addProperty(
                                "displayType",
                                "readonly"
                            )
                        }
                    }
                }
            }
            tokenObject.remove("actions")
            if (actionsResult.size() > 0) {
                tokenObject.add("actions", actionsResult)
            }
        }
        return Gson().toJson(tokenObject)
    }

    /**
     * 문서상태[tokenObject]에 따라서 현재 문서가 진행 중인지 확인 하여 [Boolean]로 반환 한다.
     */
    private fun checkTokenStatus(tokenObject: JsonObject): Boolean {
        return when (tokenObject.get("token").asJsonObject.get("status").asString) {
            WfTokenConstants.Status.RUNNING.code,
            WfTokenConstants.Status.WITHDRAW.code,
            WfTokenConstants.Status.REJECT.code -> true
            else -> false
        }
    }

    /**
     * 문서 이해관계자[tokenObject]와를 문서를 연 사용자가[userEntity]해당 토근의 처리자인지 확인 하여 [Boolean]을 반환 한다.
     */
    private fun checkAssignee(tokenObject: JsonObject, userEntity: AliceUserEntity): Boolean {
        // 문서 관련자 정보
        val stakeholders = tokenObject.get("stakeholders").asJsonObject
        val assigneeType = stakeholders.asJsonObject.get("type").asString
        val assignees = stakeholders.asJsonObject.get("assignee")
        var isAssignee = false
        if (assigneeType == WfTokenConstants.AssigneeType.ASSIGNEE.code) {
            if (stakeholders.asJsonObject.get("assignee").asString == userEntity.userKey) {
                isAssignee = true
            }
        } else if (assigneeType == WfTokenConstants.AssigneeType.USERS.code) {
            assignees.asJsonArray.forEach assigneesUserKeys@{ assigneesUserKeys ->
                if (assigneesUserKeys.asString == userEntity.userKey) {
                    isAssignee = true
                    return@assigneesUserKeys
                }
            }
        } else if (assigneeType == WfTokenConstants.AssigneeType.GROUPS.code) {
            userEntity.userRoleMapEntities.forEach aliceUserRoleMapEntity@{ aliceUserRoleMapEntity ->
                assignees.asJsonArray.forEach { assigneesRoleIds ->
                    if (assigneesRoleIds.asString == aliceUserRoleMapEntity.role.roleId) {
                        isAssignee = true
                        return@aliceUserRoleMapEntity
                    }
                }
            }
        }
        return isAssignee
    }

    /**
     * 문서정보[tokenObject]와를 문서를 연 사용자가[userEntity], 문서 상태[isProgress], 문서 담당자[isAssignee]
     * 여부를 확인하여 보여줄 버튼[JsonArray]을 보여준다.
     */
    private fun getActionList(
        tokenObject: JsonObject,
        userEntity: AliceUserEntity,
        isProgress: Boolean,
        isAssignee: Boolean
    ): JsonArray {
        // 버튼 보여주기
        var isSave = false
        var isProcess = false
        var isReject = false
        var isWithDraw = false
        var isCancel = false
        var isTerminate = false

        // 반환할 버튼 정보
        val actionsResult = JsonArray()
        // 버튼 정보를 구한다.
        val tokensActions = tokenObject.get("actions").asJsonArray
        // 회수자 정보
        val revokeAssignee = tokenObject.get("stakeholders").asJsonObject.get("revokeAssignee").asString
        // 취소 권한이 있는지 확인
        val isCancelAuth = this.checkUserAuth(userEntity, AuthConstants.AuthType.CANCEL.value)
        // 종결 권한이 있는지 확인
        val isTerminateAuth = this.checkUserAuth(userEntity, AuthConstants.AuthType.TERMINATE.value)

        // 각 버튼의 권한을 체크 한다.
        if (isProgress) {
            if (isAssignee) {
                isSave = true
                isProcess = true
                isReject = true
            }
            if (userEntity.userKey == revokeAssignee) {
                isWithDraw = true
            }
            if (isProgress && isCancelAuth) {
                isCancel = true
            }
            if (isProgress && isTerminateAuth) {
                isTerminate = true
            }
        }

        tokensActions.forEach { actions ->
            when (actions.asJsonObject.get("value").asString) {
                WfElementConstants.Action.SAVE.value -> if (isSave) {
                    actionsResult.add(actions)
                }
                WfElementConstants.Action.PROGRESS.value -> if (isProcess) {
                    actionsResult.add(actions)
                }
                WfElementConstants.Action.REJECT.value -> if (isReject) {
                    actionsResult.add(actions)
                }
                WfElementConstants.Action.WITHDRAW.value -> if (isWithDraw) {
                    actionsResult.add(actions)
                }
                WfElementConstants.Action.CANCEL.value -> if (isCancel) {
                    actionsResult.add(actions)
                }
                WfElementConstants.Action.TERMINATE.value -> if (isTerminate) {
                    actionsResult.add(actions)
                }
                WfElementConstants.Action.CLOSE.value -> {
                    actionsResult.add(actions)
                }
                else -> {
                    if (isProgress && isAssignee) {
                        actionsResult.add(actions)
                    }
                }
            }
        }
        return actionsResult
    }

    /**
     * 사용자 정보[userEntity]와 확인 할 권한[authId]을 받아서
     * 사용자가 해당 권한을 가지고 있는지 확인후 [Boolean]으로 반환 한다.
     */
    private fun checkUserAuth(userEntity: AliceUserEntity, authId: String): Boolean {
        var isAuth = false
        userEntity.userRoleMapEntities.forEach userRole@{ aliceUserRoleMapEntity ->
            aliceUserRoleMapEntity.role.roleAuthMapEntities.forEach { aliceRoleAuthMapEntity ->
                if (aliceRoleAuthMapEntity.auth.authId == authId) {
                    isAuth = true
                    return@userRole
                }
            }
        }
        return isAuth
    }
}
