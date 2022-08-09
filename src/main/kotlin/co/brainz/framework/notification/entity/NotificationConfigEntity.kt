package co.brainz.framework.notification.entity

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "notification_config")
data class NotificationConfigEntity(
    @Id
    @Column(name = "notification_code", length = 128)
    var notificationCode: String,

    @Column(name = "notification_name", length = 128)
    var notificationName: String
) : Serializable {
    @OneToMany(mappedBy = "notificationConfig", fetch = FetchType.LAZY)
    val notificationConfigDetail = mutableListOf<NotificationConfigDetailEntity>()
}
