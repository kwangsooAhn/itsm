package co.brainz.framework.avatar.dto

import java.io.Serializable

data class AliceAvatarDto(
    var avatarId: String,
    var avatarType: String?,
    var avatarValue: String?,
    var uploaded: Boolean?,
    var uploadedLocation: String?,
    var randomName: String?,
    var fileSize: Long?
) : Serializable
