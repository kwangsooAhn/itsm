package co.brainz.framework.notification.entity

import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.framework.auth.entity.AliceUserEntity
import java.io.Serializable
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
@Table(name = "awf_notification")
data class NotificationEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "notification_id", length = 128)
    var notificationId: String = "",

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_user")
    var receivedUser: AliceUserEntity = AliceUserEntity(),

    @Column(name = "title", length = 128)
    var title: String = "",

    @Column(name = "message", length = 1024)
    var message: String? = null,

    @Column(name = "instance_id", length = 128)
    var instanceId: String? = null,

    @Column(name = "confirm_yn")
    var confirmYn: Boolean = false,

    @Column(name = "display_yn")
    var displayYn: Boolean = false

) : Serializable, AliceMetaEntity()
