/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.repository

import co.brainz.itsm.group.entity.GroupEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GroupRepository : JpaRepository<GroupEntity, String>, GroupRepositoryCustom {

    fun findByGroupId(groupId: String): GroupEntity

    fun existsByGroupName(groupName: String?): Boolean

    fun deleteByGroupId(groupId: String)
}
