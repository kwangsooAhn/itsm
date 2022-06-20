package co.brainz.framework.notification.mapper

import co.brainz.framework.notification.dto.NotificationDto
import co.brainz.framework.notification.entity.NotificationEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper
interface NotificationMapper {

    @Mappings(
        Mapping(target = "receivedUser", ignore = true),
        Mapping(target = "target", ignore = true),
        Mapping(target = "createDt", ignore = true),
        Mapping(target = "updateDt", ignore = true),
        Mapping(target = "createUser", ignore = true),
        Mapping(target = "updateUser", ignore = true)
    )
    fun toNotificationEntity(notificationDto: NotificationDto): NotificationEntity
}
