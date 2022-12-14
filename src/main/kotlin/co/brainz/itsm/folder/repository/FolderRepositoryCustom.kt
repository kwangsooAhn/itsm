/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.folder.repository

import co.brainz.itsm.folder.entity.WfFolderEntity
import co.brainz.workflow.provider.dto.RestTemplateRelatedInstanceDto

interface FolderRepositoryCustom {
    fun findRelatedDocumentListByFolderId(folderId: String): List<RestTemplateRelatedInstanceDto>
    fun findFolderOriginByInstanceId(instanceId: String): WfFolderEntity
}
