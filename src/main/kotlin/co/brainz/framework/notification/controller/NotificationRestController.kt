package co.brainz.framework.notification.controller

import co.brainz.framework.notification.service.NotificationService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
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
}
