package co.brainz.framework.notification.mapper

import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.entity.NotificationEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface NotificationMapper {

    @Mappings(
        Mapping(source = "receivedUser.userKey", target = "receivedUser"),
        Mapping(source = "createUser.userName", target = "createUserName"),
        Mapping(source = "updateUser.userName", target = "updateUserName")
    )
    fun toNotificationDto(notificationEntity: NotificationEntity): NotificationDto

    @Mappings(
        Mapping(target = "receivedUser", ignore = true),
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "updateDt", ignore = true)
    )
    fun toNotificationEntity(notificationDto: NotificationDto): NotificationEntity
}
