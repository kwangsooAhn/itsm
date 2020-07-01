package co.brainz.itsm.document.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.workflow.element.constants.WfElementConstants
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
     * [tokensData] 받아서 필요 버튼을 정리 한 후[String]으로 반환 한다.
     */
    fun makeTokenAction(tokensData: String): String {
        //버튼 보여주기
        var isSave = false
        var isProcess = false
        var isReject = false
        var isWithDraw = false
        var isCancel = false
        var isTerminate = false

        // 반환할 버튼 정보
        val actionsResult = JsonArray()
        // 처리를 간편하게 처리 하기 위해서 json으로 변경한다.
        val tokenData = JsonParser().parse(tokensData).asJsonObject
        // 버튼 정보를 구한다.
        val tokensActions = tokenData.get("actions").asJsonArray
        // 해당 문서의 상태 값
        val tokenStatus = tokenData.get("token").asJsonObject.get("status").asString
        // 문서 관련자 정보
        val stakeholders = tokenData.get("stakeholders").asJsonObject
        // 회수자 정보
        val revokeAssignee = tokenData.get("stakeholders").asJsonObject.get("revokeAssignee").asString
        // 현재 진행중 문서 확인
        val isProgress = this.checkTokenStatus(tokenStatus)
        // 문서를 연 사용자 정보
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        val userEntity = userRepository.findByUserKey(aliceUserDto.userKey)
        val isAssignee = this.checkAssignee(stakeholders, userEntity)
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
            if (isProgress && this.checkUserAuth(userEntity, "action.cancel")) {
                isCancel = true
            }
            if (isProgress && this.checkUserAuth(userEntity, "action.terminate")) {
                isTerminate = true
            }
        }

        tokensActions.forEach { actions ->
            when (actions.asJsonObject.get("value").asString) {
                WfElementConstants.Action.SAVE.value -> {
                    if (isSave) {
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.PROGRESS.value -> {
                    if (isProcess) {
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.REJECT.value -> {
                    if (isReject) {
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.WITHDRAW.value -> {
                    if (isWithDraw) {
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.CANCEL.value -> {
                    if (isCancel) {
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.TERMINATE.value -> {
                    if (isTerminate) {
                        actionsResult.add(actions)
                    }
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

        tokenData.remove("actions")
        if (actionsResult.size() > 0) {
            tokenData.add("actions", actionsResult)
        }

        return Gson().toJson(tokenData)
    }

    /**
     * [tokenStatus]상태에 따라서 현재 문서가 진행 중인지 확인 하여 [Boolean]로 반환 한다.
     */
    fun checkTokenStatus(tokenStatus: String): Boolean {
        return when (tokenStatus) {
            WfTokenConstants.Status.RUNNING.code,
            WfTokenConstants.Status.WITHDRAW.code,
            WfTokenConstants.Status.REJECT.code -> true
            WfTokenConstants.Status.WAITING.code,
            WfTokenConstants.Status.FINISH.code,
            WfTokenConstants.Status.CANCEL.code,
            WfTokenConstants.Status.TERMINATE.code -> false
            else -> true
        }
    }

    /**
     * 사용자 정보[userEntity]와 확인 할 권한[authId]을 받아서
     * 사용자가 해당 권한을 가지고 있는지 확인후 [Boolean]으로 반환 한다.
     */
    fun checkUserAuth(userEntity: AliceUserEntity, authId: String): Boolean {
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

    /**
     * 문서 관련자[stakeholders]와 문서를 연 사용자가[userEntity]해당 토근의 처리자인지 확인 하여 [Boolean]을 반환 한다.
     */
    fun checkAssignee(stakeholders: JsonObject, userEntity: AliceUserEntity): Boolean {
        var isAssignee = false
        val assigneeType = stakeholders.asJsonObject.get("type").asString
        val assignees = stakeholders.asJsonObject.get("assignee")

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
}