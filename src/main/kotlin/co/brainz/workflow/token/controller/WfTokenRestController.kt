package co.brainz.workflow.token.controller

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.fileTransaction.service.AliceFileService
import co.brainz.workflow.component.constants.WfComponentConstants
import co.brainz.workflow.component.service.WfComponentService
import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.provider.dto.RestTemplateTokenAssigneesViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDataUpdateDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import co.brainz.workflow.provider.dto.RestTemplateTokenViewDto
import co.brainz.workflow.token.service.WfTokenService
import javax.transaction.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/tokens")
class WfTokenRestController(
    private val wfEngine: WfEngine,
    private val wfTokenService: WfTokenService,
    private val wfComponentService: WfComponentService,
    private val aliceFileService: AliceFileService
) {

    /**
     * 토큰 목록 조회.
     *
     * @param parameters
     * @return List<RestTemplateTokenDto>
     */
    @GetMapping("")
    fun getTokens(@RequestParam parameters: LinkedHashMap<String, Any>): List<RestTemplateTokenDto> {
        return wfTokenService.getTokens(parameters)
    }

    /**
     * 토큰 일반정보 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenDto
     */
    @GetMapping("/{tokenId}")
    fun getToken(@PathVariable tokenId: String): RestTemplateTokenDto {
        return wfTokenService.getToken(tokenId)
    }

    /**
     * 토큰 상세정보 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenViewDto
     */
    @GetMapping("/{tokenId}/data")
    fun getTokenData(@PathVariable tokenId: String): RestTemplateTokenViewDto {
        return wfTokenService.getTokenData(tokenId)
    }

    /**
     * 토큰 담당자 조회.
     *
     * @param tokenId
     * @return RestTemplateTokenViewDto
     */
    @GetMapping("/{tokenId}/assignees")
    fun getTokenAssignees(@PathVariable tokenId: String): RestTemplateTokenAssigneesViewDto {
        return wfTokenService.getTokenAssignees(tokenId)
    }

    /**
     * Post Token Gate.
     *
     * @param restTemplateTokenDataUpdateDto
     * @return Any
     */
    @Transactional
    @PostMapping("")
    fun postTokenGate(@RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        // 2020-05-29 Jung Hee Chan
        // 기존 함수들의 영향도를 최소화하기 위해서 임시로 작성.
        // 이 부분은 현재 진행되고 있는 WF 구조 변경에 따라 사라질 부분이라...
        // 하지만 token쪽에 추가된 dto들은 계속 사용할 것 같음.
        val dummyTokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            instanceCreateUser = restTemplateTokenDataUpdateDto.assigneeId?.let { AliceUserEntity(userKey = it) },
            action = restTemplateTokenDataUpdateDto.action
        )

        // 2020-05-29 Jung Hee Chan
        // 아래 부분도 원래 Controller에서 처리할 부분은 아니지만, 현재 작업중인 WF 변경에 영향을 덜 주기 위해서 일단 여기 작성
        // 이후에는 UserTaskManager에서 처리할 예정.
        // fileupload 로직. 원래는 ITSM쪽에서 처리하고 있었으나... component type을 일일이 검증할 수 없어서 엔진에서 처리
        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (wfComponentService
                .getComponentTypeById(it.componentId) == (WfComponentConstants.ComponentType.FILEUPLOAD.code) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        return wfEngine.startWorkflow(wfEngine.toTokenDto(dummyTokenDto))
    }

    /**
     * Put Token Gate.
     */
    @Transactional
    @PutMapping("/{tokenId}")
    fun putTokenGate(@RequestBody restTemplateTokenDataUpdateDto: RestTemplateTokenDataUpdateDto): Boolean {
        // 2020-05-29 Jung Hee Chan
        // 기존 함수들의 영향도를 최소화하기 위해서 임시로 작성.
        // 이 부분은 현재 진행되고 있는 WF 구조 변경에 따라 사라질 부분이라...
        // 하지만 token쪽에 추가된 dto들은 계속 사용할 것 같음.
        val dummyTokenDto = RestTemplateTokenDto(
            assigneeId = restTemplateTokenDataUpdateDto.assigneeId.toString(),
            tokenId = restTemplateTokenDataUpdateDto.tokenId,
            documentId = restTemplateTokenDataUpdateDto.documentId,
            data = restTemplateTokenDataUpdateDto.componentData as List<RestTemplateTokenDataDto>,
            action = restTemplateTokenDataUpdateDto.action
        )

        // 2020-05-29 Jung Hee Chan
        // 아래 부분도 원래 Controller에서 처리할 부분은 아니지만, 현재 작업중인 WF 변경에 영향을 덜 주기 위해서 일단 여기 작성
        // 이후에는 UserTaskManager에서 처리할 예정.
        // fileupload 로직. 원래는 ITSM쪽에서 처리하고 있었으나... component type을 일일이 검증할 수 없어서 엔진에서 처리
        restTemplateTokenDataUpdateDto.componentData!!.forEach {
            when (wfComponentService
                .getComponentTypeById(it.componentId) == (WfComponentConstants.ComponentType.FILEUPLOAD.code) && it.value.isNotEmpty()) {
                true -> this.aliceFileService.uploadFiles(it.value)
            }
        }
        return wfEngine.progressWorkflow(wfEngine.toTokenDto(dummyTokenDto))
    }
}
