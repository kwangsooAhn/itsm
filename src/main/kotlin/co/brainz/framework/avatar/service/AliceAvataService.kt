package co.brainz.framework.avatar.service

import co.brainz.framework.avatar.entity.AliceAvatarEntity
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AliceAvataService(
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun makeAvataPath(avatarEntity: AliceAvatarEntity): String {
        return when (avatarEntity.avatarType) {
            "file" ->
                avatarEntity.uploadedLocation + avatarEntity.avatarValue
            "url" ->
                avatarEntity.avatarValue
            else -> "test"
        }
    }
}
