/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.avatar.service

import co.brainz.framework.avatar.entity.AliceAvatarEntity
import co.brainz.framework.constants.AliceUserConstants
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AliceAvatarService {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Value("\${file.image.uri}")
    private val resourcesUriPath: String? = null

    fun makeAvatarPath(avatarEntity: AliceAvatarEntity): String {
        return when (avatarEntity.avatarType) {
            AliceUserConstants.USER_AVATAR_TYPE_FILE ->
                if (avatarEntity.uploaded) {
                    resourcesUriPath + "/avatar/" + avatarEntity.avatarValue
                } else {
                    AliceUserConstants.SAMPLE_FILE_PATH + AliceUserConstants.SAMPLE_FILE_NAME
                }
            AliceUserConstants.USER_AVATAR_TYPE_URL ->
                avatarEntity.avatarValue
            else -> avatarEntity.uploadedLocation
        }
    }
}
