/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.cmdb.ci.repository

import co.brainz.cmdb.ci.entity.CIInstanceRelationEntity
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class CIInstanceRelationRepositoryImpl : QuerydslRepositorySupport(CIInstanceRelationEntity::class.java),
    CIInstanceRelationRepositoryCustom
