package co.brainz.framework.notification.controller

import co.brainz.framework.notification.dto.NotificationConfigDto
import co.brainz.framework.notification.service.NotificationService
import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/notifications")
class NotificationRestController(private val notificationService: NotificationService) {

    /**
     * 알림 수정.
     */
    @PutMapping("/{notificationId}/{target}")
    fun updateNotification(@PathVariable notificationId: String, @PathVariable target: String) {
        notificationService.updateNotification(notificationId, target)
    }

    /**
     * 알림 삭제.
     */
    @DeleteMapping("/{notificationId}")
    fun deleteNotification(@PathVariable notificationId: String) {
        notificationService.deleteNotification(notificationId)
    }

    /**
     *  알람 발송 관리 설정 정보 변경
     */
    @PutMapping("/config")
    fun updateNotificationConfig(@RequestBody config: NotificationConfigDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(notificationService.updateNotificationConfig(config))
    }
}
