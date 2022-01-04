/*
 * Copyright 2020 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 *
 */

package co.brainz.itsm.group.repository

import co.brainz.itsm.group.entity.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GroupRepository: JpaRepository<GroupEntity,String> ,GroupRepositoryCustom