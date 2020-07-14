package co.brainz.framework.avatar.service

import co.brainz.framework.avatar.entity.AliceAvatarEntity
import co.brainz.framework.constants.AliceUserConstants
import co.brainz.framework.util.AliceFileUtil
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.nio.file.Paths
import javax.imageio.ImageIO

@Component
class AliceAvatarService(
    private val aliceFileUtil: AliceFileUtil
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun makeAvatarPath(avatarEntity: AliceAvatarEntity): String {
        return when (avatarEntity.avatarType) {
            AliceUserConstants.USER_AVATAR_TYPE_FILE ->
                avatarEntity.uploadedLocation
            /*{
                val file = Paths.get(avatarEntity.uploadedLocation).toFile()
                val bufferedImage = ImageIO.read(file)
                val resizedBufferedImage = aliceFileUtil.resizeBufferedImage(bufferedImage)
                return aliceFileUtil.encodeToString(resizedBufferedImage, file.extension)
            }*/
            AliceUserConstants.USER_AVATAR_TYPE_URL ->
                avatarEntity.avatarValue
            else -> avatarEntity.uploadedLocation
        }
    }
}
