package co.brainz.workflow.engine.instance.controller

import co.brainz.workflow.engine.WfEngine
import co.brainz.workflow.provider.dto.RestTemplateCommentDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceCountDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceHistoryDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceListDto
import co.brainz.workflow.provider.dto.RestTemplateInstanceViewDto
import co.brainz.workflow.provider.dto.RestTemplateTagViewDto
import co.brainz.workflow.provider.dto.RestTemplateTokenDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/wf/instances")
class WfInstanceRestController(private val wfEngine: WfEngine) {

    /**
     * Process Instance List.
     *
     * @param parameters
     * @return List<InstanceViewDto>
     */
    @GetMapping("")
    fun getProcessInstances(@RequestParam parameters: LinkedHashMap<String, Any>): List<RestTemplateInstanceViewDto> {
        return wfEngine.instance().instances(parameters)
    }

    /**
     * Process Instance.
     *
     * @param instanceId
     * @return WfInstanceViewDto
     */
    @GetMapping("/{instanceId}")
    fun getProcessInstance(@PathVariable instanceId: String): RestTemplateInstanceViewDto {
        return wfEngine.instance().instance(instanceId)
    }

    /**
     * Process Instance Status Count.
     *
     * @param param
     * @return List<RestTemplateInstanceCountDto>
     */
    @GetMapping("/count")
    fun getProcessInstancesStatusCount(@RequestParam param: LinkedHashMap<String, Any>): List<RestTemplateInstanceCountDto> {
        return wfEngine.instance().instancesStatusCount(param)
    }

    /**
     * Instance history.
     */
    @GetMapping("/{instanceId}/history")
    fun getInstancesHistory(@PathVariable instanceId: String): List<RestTemplateInstanceHistoryDto> {
        return wfEngine.instance().getInstancesHistory(instanceId)
    }

    /**
     * 인스턴스ID [instanceId]로 마지막 토큰 정보를 조회한다.
     */
    @GetMapping("/{instanceId}/latest")
    fun getInstanceLatestToken(@PathVariable instanceId: String): RestTemplateTokenDto {
        return wfEngine.instance().getInstanceLatestToken(instanceId)
    }

    @GetMapping("/{instanceId}/comments")
    fun getInstanceComments(@PathVariable instanceId: String): MutableList<RestTemplateCommentDto> {
        return wfEngine.instance().getInstanceComments(instanceId)
    }

    @GetMapping("/{instanceId}/tags")
    fun getInstanceTags(@PathVariable instanceId: String): List<RestTemplateTagViewDto> {
        return wfEngine.instance().getInstanceTags(instanceId)
    }

    @GetMapping("/search")
    fun getAllInstanceListAndSearch(@RequestParam instanceId: String, @RequestParam searchValue: String): MutableList<RestTemplateInstanceListDto> {
        return wfEngine.instance().getAllInstanceListAndSearch(instanceId, searchValue)
    }
}
