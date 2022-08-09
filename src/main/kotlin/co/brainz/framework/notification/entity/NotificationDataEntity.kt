/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.notification.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table
import org.hibernate.annotations.GenericGenerator

@Entity
@Table(name = "notification_data")
data class NotificationDataEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "notification_id", length = 128)
    var notificationId: String = "",

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_user")
    var receivedUser: AliceUserEntity = AliceUserEntity(),

    @Column(name = "title", length = 128)
    var title: String,

    @Column(name = "message")
    var message: String,

    @Column(name = "send_dt")
    var sendDt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "channel", length = 128)
    var channel: String,

    @Column(name = "display_dt")
    var displayDt: LocalDateTime?,

    @Column(name = "confirm_dt")
    var confirmDt: LocalDateTime?,

    @Column(name = "display_yn")
    var listYn: Boolean?,

    @Column(name = "url", length = 512)
    var url: String?

) : Serializable
