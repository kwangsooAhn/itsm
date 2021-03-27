/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.framework.fileTransaction.repository

import co.brainz.framework.fileTransaction.entity.AliceFileOwnMapEntity
import co.brainz.framework.fileTransaction.entity.QAliceFileOwnMapEntity
import com.querydsl.core.types.Projections
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class AliceFileOwnMapRepositoryImpl : QuerydslRepositorySupport(AliceFileOwnMapEntity::class.java),
    AliceFileOwnMapRepositoryCustom {
    override fun findFileOwnIdAndFileLocUploaded(ownId: String, uploaded: Boolean): List<AliceFileOwnMapEntity> {
        val fileOwnMap = QAliceFileOwnMapEntity.aliceFileOwnMapEntity
        val query = from(fileOwnMap)
            .select(
                Projections.fields(
                    AliceFileOwnMapEntity::class.java,
                    fileOwnMap.ownId,
                    fileOwnMap.fileLocEntity
                )
            )
            .where(fileOwnMap.ownId.eq(ownId).and(fileOwnMap.fileLocEntity.uploaded.eq(uploaded)))
        return query.fetch()
    }
}
