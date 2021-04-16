/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.framework.tag.repository

import co.brainz.framework.tag.entity.AliceTagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AliceTagRepository : JpaRepository<AliceTagEntity, String>, AliceTagRepositoryCustom
