package co.brainz.framework.auth.repository

import co.brainz.framework.auth.entity.AliceUrlAuthMapPk
import co.brainz.framework.auth.entity.AliceUrlAuthMapEntity
import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.framework.auth.entity.AliceUrlEntity
import co.brainz.framework.auth.entity.AliceAuthEntity

interface AliceUrlAuthMapRepository: JpaRepository<AliceUrlAuthMapEntity, AliceUrlAuthMapPk> {
    fun findByAuth(AuthInfo : AliceAuthEntity): MutableList<AliceUrlAuthMapEntity>
    
    fun findByUrlAndMethod(UrlInfo : MutableList<AliceUrlEntity>):  MutableList<AliceUrlAuthMapEntity>
}