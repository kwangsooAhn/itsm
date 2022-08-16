package co.brainz.framework.resourceManager.repository

import co.brainz.framework.resourceManager.entity.AliceFileNameExtensionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceFileNameExtensionRepository : JpaRepository<AliceFileNameExtensionEntity, String>
