/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.token.service

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.dashboard.dto.DashboardStatisticDto
import co.brainz.itsm.document.service.DocumentActionService
import co.brainz.itsm.token.dto.TokenSearchConditionDto
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.instance.service.WfInstanceService
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.provider.dto.RestTemplateInstanceListReturnDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.service.WfTokenService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.stereotype.Service

@Service
class TokenService(
    private val documentActionService: DocumentActionService,
    private val wfInstanceService: WfInstanceService,
    private val wfTokenService: WfTokenService,
    private val wfComponentService: WfComponentService,
    private val aliceFileService: AliceFileService,
    private val currentSessionUser: CurrentSessionUser,
    private val wfEngine: WfEngine
) {

    private val mapper = ObjectMapper().registerModules(KotlinModule(), JavaTimeModule())

    /**
     * Post Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun postToken(restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        restTemplateTokenDataUpdateDto.assigneeId = currentSessionUser.getUserKey()

        val tokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            instanceId = restTemplateTokenDataUpdateDto.instanceId,
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            instanceCreateUser = restTemplateTokenDataUpdateDto.assigneeId?.let { AliceUserEntity(userKey = it) },
            action = restTemplateTokenDataUpdateDto.action,
            instancePlatform = RestTemplateConstants.InstancePlatform.ITSM.code
        )

        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (wfComponentService
                .getComponentTypeById(it.componentId) ==
                    (WfComponentConstants.ComponentType.FILEUPLOAD.code) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        return wfEngine.startWorkflow(wfEngine.toTokenDto(tokenDto))
    }

    /**
     * Put Token 처리.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Boolean
     */
    fun putToken(tokenId: String, restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        restTemplateTokenDataUpdateDto.assigneeId = currentSessionUser.getUserKey()

        val tokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            action = restTemplateTokenDataUpdateDto.action,
            instancePlatform = RestTemplateConstants.InstancePlatform.ITSM.code
        )

        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (wfComponentService
                .getComponentTypeById(it.componentId) ==
                    (WfComponentConstants.ComponentType.FILEUPLOAD.code) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        return wfEngine.progressWorkflow(wfEngine.toTokenDto(tokenDto))
    }

    /**
     * 처리할 문서 리스트 조회.
     *
     * @param tokenSearchConditionDto
     * @return List<tokenDto>
     */
    fun getTokenList(
        tokenSearchConditionDto: TokenSearchConditionDto
    ): RestTemplateInstanceListReturnDto {
        tokenSearchConditionDto.userKey = currentSessionUser.getUserKey()
        return wfInstanceService.instances(tokenSearchConditionDto)
    }

    /**
     * [tokenId]를 받아서 처리할 문서 상세 조회 하여 [String]반환 한다.
     */
    fun findToken(tokenId: String): String =
        documentActionService.makeTokenAction(mapper.writeValueAsString(wfTokenService.getTokenData(tokenId)))

    fun getTodoTokenCount(): Long = getTokenList(
        TokenSearchConditionDto(
            userKey = currentSessionUser.getUserKey(),
            searchTokenType = WfTokenConstants.SearchType.TODO.code
        )
    ).totalCount

    /**
     *  업무통계 데이터 조회
     */
    fun getDashboardStatistic(): List<DashboardStatisticDto> {
        val dashboardStatisticDtoList = mutableListOf<DashboardStatisticDto>()
        val typeList = listOf(WfTokenConstants.StatistichType.TODO.code, WfTokenConstants.StatistichType.RUNNING.code, WfTokenConstants.StatistichType.MONTHDONE.code, WfTokenConstants.StatistichType.DONE.code)

        val tokenSearchConditionDto = TokenSearchConditionDto(
            userKey = currentSessionUser.getUserKey()
        )
        var dashboardStatisticDto: DashboardStatisticDto

        val today = LocalDate.now()

        for (i in 0 until typeList.size) {
            dashboardStatisticDto = DashboardStatisticDto()
            when (typeList[i]) {
                WfTokenConstants.StatistichType.TODO.code -> {
                    tokenSearchConditionDto.searchTokenType = WfTokenConstants.SearchType.TODO.code
                }
                WfTokenConstants.StatistichType.RUNNING.code -> {
                    tokenSearchConditionDto.searchTokenType = WfTokenConstants.SearchType.PROGRESS.code
                }
                WfTokenConstants.StatistichType.MONTHDONE.code -> {
                    tokenSearchConditionDto.searchTokenType = WfTokenConstants.SearchType.COMPLETED.code
                    tokenSearchConditionDto.searchFromDt =
                        LocalDateTime.parse(LocalDateTime.of(today.year, today.month, 1, 0, 0, 0).toString(),
                            DateTimeFormatter.ISO_DATE_TIME).toString()
                    tokenSearchConditionDto.searchToDt =
                        LocalDateTime.parse(LocalDateTime.of(today.year, today.month, today.lengthOfMonth(), 23, 59, 59)
                            .toString(), DateTimeFormatter.ISO_DATE_TIME).toString()
                }
                WfTokenConstants.StatistichType.DONE.code -> {
                    tokenSearchConditionDto.searchFromDt = ""
                    tokenSearchConditionDto.searchToDt = ""
                    tokenSearchConditionDto.searchTokenType = WfTokenConstants.SearchType.COMPLETED.code
                }
            }
            dashboardStatisticDto.type = typeList[i]
            tokenSearchConditionDto.documentGroup = ""
            dashboardStatisticDto.total = getTokenList(tokenSearchConditionDto).totalCount
            tokenSearchConditionDto.documentGroup = "servicedesk.incident"
            dashboardStatisticDto.incident = getTokenList(tokenSearchConditionDto).totalCount
            tokenSearchConditionDto.documentGroup = "servicedesk.inquiry"
            dashboardStatisticDto.inquiry = getTokenList(tokenSearchConditionDto).totalCount
            tokenSearchConditionDto.documentGroup = "servicedesk.request"
            dashboardStatisticDto.request = getTokenList(tokenSearchConditionDto).totalCount
            tokenSearchConditionDto.documentGroup = "servicedesk.etc"
            dashboardStatisticDto.etc = getTokenList(tokenSearchConditionDto).totalCount

            dashboardStatisticDtoList.add(dashboardStatisticDto)
        }
        return dashboardStatisticDtoList
    }
}
