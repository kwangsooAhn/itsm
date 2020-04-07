package co.brainz.framework.fileTransaction.repository

import org.springframework.data.jpa.repository.JpaRepository
import co.brainz.framework.fileTransaction.entity.AliceFileNameExtensionEntity
import org.springframework.stereotype.Repository

@Repository
interface AliceFileNameExtensionRepository: JpaRepository<AliceFileNameExtensionEntity, String> {
}