/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.avatar.entity

import co.brainz.framework.auth.entity.AliceUserEntity
import co.brainz.framework.constants.AliceUserConstants
import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.FetchType
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@Table(name = "awf_avatar")
@IdClass(AliceAvatarPk::class)
data class AliceAvatarEntity(

    @Id
    @Column(name = "avatar_id")
    var avatarId: String = "",

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key")
    val user: AliceUserEntity,

    @Column(name = "avatar_type", length = 100)
    var avatarType: String = AliceUserConstants.AvatarType.FILE.code,

    @Column(name = "avatar_value", length = 512)
    var avatarValue: String = AliceUserConstants.AVATAR_BASIC_FILE_NAME,

    @Column(name = "uploaded")
    var uploaded: Boolean = false,

    @Column(name = "uploaded_location")
    var uploadedLocation: String = ""

) : Serializable

data class AliceAvatarPk(
    val user: String = "",
    val avatarId: String = ""
) : Serializable
