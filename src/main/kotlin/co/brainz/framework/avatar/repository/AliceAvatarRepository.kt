package co.brainz.framework.avatar.repository

import co.brainz.framework.avatar.entity.AliceAvatarEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceAvatarRepository : JpaRepository<AliceAvatarEntity, String> {

}