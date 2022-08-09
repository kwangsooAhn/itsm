package co.brainz.framework.notification.entity

import co.brainz.framework.auditor.AliceMetaEntity
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "notification_config_detail")
@IdClass(NotificationConfigDetailPk::class)
data class NotificationConfigDetailEntity(
    @Id
    @Column(name = "channel", length = 128)
    var channel: String,

    @Column(name = "use_yn")
    var useYn: Boolean,

    @Column(name = "title_format", length = 512)
    var titleFormat: String,

    @Column(name = "message_format")
    var messageFormat: String,

    @Column(name = "template")
    var template: String?,

    @Column(name = "url", length = 512)
    var url: String?,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_code")
    var notificationConfig: NotificationConfigEntity

) : Serializable, AliceMetaEntity()

data class NotificationConfigDetailPk(
    val channel: String = "",
    val notificationConfig: String = ""
) : Serializable
