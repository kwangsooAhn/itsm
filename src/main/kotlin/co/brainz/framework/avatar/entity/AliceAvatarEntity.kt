package co.brainz.framework.avatar.entity

import co.brainz.framework.auditor.AliceMetaEntity
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "awf_avatar")
data class AliceAvatarEntity(
    @Id @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "avatar_id", length = 128)
    var avatarId: String = "",

    @Column(name = "avatar_type", length = 128)
    var avatarType: String = "",

    @Column(name = "avatar_value", length = 512)
    var avatarValue: String = "",

    @Column(name = "uploaded")
    var uploaded: Boolean = false,

    @Column(name = "uploaded_location")
    var uploadedLocation: String = "",

    @Column(name = "random_name")
    var randomName: String = "",

    @Column(name = "file_size")
    var fileSize: Long = 0L

) : Serializable, AliceMetaEntity()
