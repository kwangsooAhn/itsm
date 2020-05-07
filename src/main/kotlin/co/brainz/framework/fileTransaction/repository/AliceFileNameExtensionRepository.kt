package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceFileNameExtensionRepository : JpaRepository<AliceFileNameExtensionEntity, String>
