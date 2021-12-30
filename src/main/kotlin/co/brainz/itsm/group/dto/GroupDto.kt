package co.brainz.itsm.group.dto

import java.io.Serializable
import java.time.LocalDateTime

data class GroupDto(
    var groupId: String? = null,
    var pGroupId: String? = null,
    var groupName: String? = null,
    var groupDesc: String? = null,
    var useYn: Boolean? = true,
    var level: Int? = null,
    var seqNum: Int? = null,
    var createUserKey: String? = null,
    var createDt: LocalDateTime? = null,
    var updateUserKey: String? = null,
    var updateDt: LocalDateTime? = null
) : Serializable
