package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceAuthEntity
import co.brainz.framework.auth.entity.AliceUrlAuthMapPk
import co.brainz.framework.auth.entity.AliceUrlAuthMapEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AliceUrlAuthMapRepository : JpaRepository<AliceUrlAuthMapEntity, AliceUrlAuthMapPk> {
    fun findByAuth(AuthInfo: AliceAuthEntity): MutableList<AliceUrlAuthMapEntity>
}
