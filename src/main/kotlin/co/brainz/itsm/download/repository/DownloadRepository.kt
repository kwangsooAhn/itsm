package co.brainz.itsm.download.repository

import co.brainz.itsm.download.entity.DownloadEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DownloadRepository : JpaRepository<DownloadEntity, String>, DownloadRepositoryCustom {

}

