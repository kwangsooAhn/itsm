/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.avatar.entity

import co.brainz.framework.auditor.AliceMetaEntity
import co.brainz.framework.constants.AliceUserConstants
import org.hibernate.annotations.GenericGenerator
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "awf_avatar")
data class AliceAvatarEntity(
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(name = "avatar_id", length = 128)
    var avatarId: String = "",

    @Column(name = "avatar_type", length = 128)
    var avatarType: String = AliceUserConstants.USER_AVATAR_TYPE_FILE,

    @Column(name = "avatar_value", length = 512)
    var avatarValue: String = AliceUserConstants.SAMPLE_FILE_NAME,

    @Column(name = "uploaded")
    var uploaded: Boolean = false,

    @Column(name = "uploaded_location")
    var uploadedLocation: String = ""

) : Serializable, AliceMetaEntity()
