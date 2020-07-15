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

    /**
     * 아바타 타입에 따른 경로를 반환한다. 이때 uploaded가 false를 기본값으로 본다.
     */
    fun makeAvatarPath(avatarEntity: AliceAvatarEntity): String {
        return when (avatarEntity.avatarType) {
            AliceUserConstants.AVATAR_TYPE_FILE ->
                if (avatarEntity.uploaded) {
                    resourcesUriPath + "/avatar/" + avatarEntity.avatarValue
                } else {
                    AliceUserConstants.AVATAR_BASIC_FILE_PATH + AliceUserConstants.AVATAR_BASIC_FILE_NAME
                }
            AliceUserConstants.AVATAR_TYPE_URL ->
                avatarEntity.avatarValue
            else -> avatarEntity.uploadedLocation
        }
    }
}
