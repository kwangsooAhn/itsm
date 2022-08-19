/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.controller

import co.brainz.framework.notification.service.NotificationService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/notifications")
class NotificationController(private val notificationService: NotificationService) {

    private val notificationListPage: String = "notification/notificationList"
    private val notificationEditPage: String = "notification/notificationEdit"
    private val notificationRecordSearch: String = "notification/notificationRecordSearch"

    @GetMapping("")
    fun getNotificationList(model: Model): String {
        model.addAttribute("notificationList", notificationService.getNotificationList())
        return notificationListPage
    }

    // 알람 발송 관리 화면 이동
    @GetMapping("/edit")
    fun getNotificationEdit(model: Model): String {
        model.addAttribute("notificationConfigs", notificationService.getNotificationConfig())
        return notificationEditPage
    }

    // 알람 발송 이력 화면 이동
    @GetMapping("/record")
    fun getNotificationRecord(model: Model): String {
        return notificationRecordSearch
    }
}
