package co.brainz.workflow.engine.manager.service

import co.brainz.framework.auth.repository.AliceUserRoleMapRepository
import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.service.NotificationService
import co.brainz.workflow.element.repository.WfElementRepository
import co.brainz.workflow.engine.manager.dto.WfTokenDto
import co.brainz.workflow.instance.repository.WfInstanceRepository
import co.brainz.workflow.provider.constants.RestTemplateConstants
import co.brainz.workflow.token.constants.WfTokenConstants
import co.brainz.workflow.token.entity.WfCandidateEntity
import co.brainz.workflow.token.entity.WfTokenEntity
import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.stereotype.Service

@Service
class WfTokenManagerService(
    private val wfInstanceRepository: WfInstanceRepository,
    private val wfElementRepository: WfElementRepository,
    private val notificationService: NotificationService,
    private val aliceUserRoleMapRepository: AliceUserRoleMapRepository
) {

    //필요한 함수를 여기에 모두 작성한다.

    fun makeTokenEntity(wfTokenDto: WfTokenDto): WfTokenEntity {
        return WfTokenEntity(
            tokenId = "",
            tokenStatus = RestTemplateConstants.TokenStatus.RUNNING.value,
            tokenStartDt = LocalDateTime.now(ZoneId.of("UTC")),
            instance = wfInstanceRepository.findByInstanceId(wfTokenDto.instanceId)!!,
            element = wfElementRepository.findWfElementEntityByElementId(wfTokenDto.elementId)
        )
    }

    /**
     * Save Notification.
     *
     * @param token
     * @param candidates
     */
    fun saveNotification(token: WfTokenEntity, candidates: List<WfCandidateEntity>? = null) {
        if (token.element.notification) {
            val notifications = mutableListOf<NotificationDto>()
            val commonNotification = NotificationDto(
                title = token.instance.document?.documentName!!,
                message = "[" + token.element.elementName + "] " + token.instance.document.documentDesc,
                instanceId = token.instance.instanceId
            )

            if (candidates != null) {
                candidates.forEach { candidate ->
                    when (candidate.candidateType) {
                        WfTokenConstants.AssigneeType.USERS.code -> {
                            val notification = commonNotification.copy()
                            notification.receivedUser = candidate.candidateValue
                            notifications.add(notification)
                        }
                        WfTokenConstants.AssigneeType.GROUPS.code -> {
                            val users = aliceUserRoleMapRepository.findUserRoleMapByRoleId(candidate.candidateValue)
                            users?.forEach {
                                val notification = commonNotification.copy()
                                notification.receivedUser = it.user.userKey
                                notifications.add(notification)
                            }
                        }
                    }
                }
            } else {
                commonNotification.receivedUser = token.assigneeId!!
                notifications.add(commonNotification)
            }
            notificationService.insertNotificationList(notifications)
        }
    }
}
