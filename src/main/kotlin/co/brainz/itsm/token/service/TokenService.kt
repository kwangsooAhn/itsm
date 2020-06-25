package co.brainz.itsm.token.service

import co.brainz.framework.auth.dto.AliceUserDto
import co.brainz.itsm.user.repository.UserRepository
import co.brainz.workflow.element.constants.WfElementConstants
import co.brainz.workflow.provider.RestTemplateProvider
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenSearchListDto
import co.brainz.workflow.provider.dto.RestTemplateUrlDto
import co.brainz.workflow.token.constants.WfTokenConstants
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap

@Service
class TokenService(
    private val restTemplate: RestTemplateProvider,
    private val userRepository: UserRepository
) {

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun postToken(restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateTokenDataUpdateDto.assigneeId = aliceUserDto.userKey
        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Token.POST_TOKEN.url)
        val responseEntity = restTemplate.create(url, restTemplateTokenDataUpdateDto)

        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * Put Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun putToken(tokenId: String, restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.PUT_TOKEN.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        restTemplateTokenDataUpdateDto.assigneeId = aliceUserDto.userKey
        val responseEntity = restTemplate.update(url, restTemplateTokenDataUpdateDto)

        return responseEntity.body.toString().isNotEmpty()
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @param restTemplateTokenSearchListDto
     * @return List<tokenDto>
     */
    fun getTokenList(
        restTemplateTokenSearchListDto: RestTemplateTokenSearchListDto
    ): List<RestTemplateInstanceViewDto> {
        val params = LinkedMultiValueMap<String, String>()
        val aliceUserDto = SecurityContextHolder.getContext().authentication.details as AliceUserDto
        params.add("userKey", aliceUserDto.userKey)
        params.add("tokenType", restTemplateTokenSearchListDto.searchTokenType)
        params.add("documentId", restTemplateTokenSearchListDto.searchDocumentId)
        params.add("searchValue", restTemplateTokenSearchListDto.searchValue)
        params.add("offset", restTemplateTokenSearchListDto.offset)
        params.add("fromDt", restTemplateTokenSearchListDto.searchFromDt)
        params.add("toDt", restTemplateTokenSearchListDto.searchToDt)

        val url = RestTemplateUrlDto(callUrl = RestTemplateConstants.Workflow.GET_INSTANCES.url, parameters = params)
        val responseBody = restTemplate.get(url)

        val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())
        return mapper.readValue(
            responseBody,
            mapper.typeFactory.constructCollectionType(List::class.java, RestTemplateInstanceViewDto::class.java)
        )
    }

    /**
     * 처리할 문서 상세 조회.
     *
     * @return List<tokenDto>
     */
    fun findToken(tokenId: String): String {
        val url = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN_DATA.url.replace(
                restTemplate.getKeyRegex(),
                tokenId
            )
        )

        return makeTokenAction(restTemplate.get(url))
    }

    /**
     *  TokenData를 받아서 버튼 정리해서 반환한다.
    1. 사용자가 가진 역할자이 취소, 종결 권한을 가지고 있는지
    2. 해당 Token의 담당자가 누구인지 : 유형, 값(user_key, role_id)
    3. 해당 Token의 회수자가 누군인지 : 유형, 값(user_key, role_id)
    4. Token의 상태
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

        //문서 처리자 정보
        val tokenAssigneeUrl = RestTemplateUrlDto(
            callUrl = RestTemplateConstants.Token.GET_TOKEN_ASSIGNEE.url.replace(restTemplate.getKeyRegex(), tokenId)
        )
        val tokenAssigneesJsonData: JsonObject = JsonParser().parse(restTemplate.get(tokenAssigneeUrl)).asJsonObject
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
            userEntity.userRoleMapEntities.forEach{aliceUserRoleMapEntity->
                assignees.forEach { assigneesRoleIds ->
                    if(assigneesRoleIds.asString == aliceUserRoleMapEntity.role.roleId) {
                        isAssignee = true
                    }
                }
            }
        }

        //각 버튼이 보여져야 하는지 조건 확인
        if (isTokenStatusIng && isAssignee) {
            isSave = true
            isProcess = true
            isReject = true
        }

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

        tokensActions.forEach { actions ->
            when (actions.asJsonObject.get("value").asString) {
                WfElementConstants.Action.SAVE.value -> {
                    if (isSave) {
                        print("SAVE")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.REJECT.value -> {
                    if (isReject) {
                        print("REJECT")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.PROCESS.value -> {
                    if (isProcess) {
                        print("PROCESS")
                        actionsResult.add(actions)
                    }
                }
                WfElementConstants.Action.WITHDRAW.value -> {
                    //미정
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
