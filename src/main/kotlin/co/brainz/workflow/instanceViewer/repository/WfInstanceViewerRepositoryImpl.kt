package co.brainz.workflow.instanceViewer.repository

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import co.brainz.workflow.instanceViewer.entity.WfInstanceViewerEntity

@Repository
class WfInstanceViewerRepositoryImpl: QuerydslRepositorySupport(WfInstanceViewerEntity::class.java) {

}