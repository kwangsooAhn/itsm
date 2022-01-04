/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.service

import co.brainz.framework.exception.AliceErrorConstants
import co.brainz.framework.exception.AliceException
import co.brainz.framework.util.AliceMessageSource
import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.group.dto.GroupDetailDto
import co.brainz.itsm.group.dto.GroupDto
import co.brainz.itsm.group.dto.GroupListDto
import co.brainz.itsm.group.dto.GroupRoleDto
import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.GroupRoleMapEntity
import co.brainz.itsm.group.repository.GroupRepository
import co.brainz.itsm.group.repository.GroupRoleMapRepository
import co.brainz.itsm.user.repository.UserRepository
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val groupRoleMapRepository: GroupRoleMapRepository,
    private val userRepository: UserRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val aliceMessageSource: AliceMessageSource
) {

    /**
     * 조직 전체 목록 조회
     */
    fun getGroupList(searchValue: String): GroupListDto {
        val treeGroupList = mutableListOf<GroupDto>()
        val pGroupList = mutableListOf<GroupEntity>()
        val queryResults: QueryResults<GroupEntity> = groupRepository.findByGroupSearchList(searchValue)
        var groupSearchList = queryResults.results

        for (group in groupSearchList) {
            var tempGroup = group.pGroup
            do {
                if (tempGroup !== null) {
                    pGroupList.add(tempGroup)
                    tempGroup = tempGroup.pGroup
                }
            } while (tempGroup !== null)
        }
        if (pGroupList.isNotEmpty()) {
            groupSearchList.addAll(pGroupList)
            groupSearchList = groupSearchList.distinct()
        }
        val count: Long = groupSearchList.size.toLong()
        for (group in groupSearchList) {
            treeGroupList.add(
                GroupDto(
                    groupId = group.groupId,
                    pGroupId = group.pGroup?.groupId,
                    groupName = group.groupName,
                    groupDesc = group.groupDesc,
                    useYn = group.useYn,
                    level = group.level,
                    seqNum = group.seqNum,
                    editable = group.editable,
                    createDt = group.createDt,
                    createUserKey = group.createUserKey,
                    updateDt = group.updateDt,
                    updateUserKey = group.updateUserKey
                )
            )
        }
        return GroupListDto (
            data = treeGroupList,
            totalCount = count
        )
    }

    /**
     * 조직 정보 상세 조회
     */
    fun getDetailGroup(groupId: String): GroupDetailDto {
        val groupInfo = groupRepository.findByGroupId(groupId)
        val groupUseRoleList = groupRoleMapRepository.findGroupUseRoleByGroupId(groupId)

        return GroupDetailDto(
            groupId = groupInfo.groupId,
            groupName = groupInfo.groupName,
            pGroupId = groupInfo.pGroup?.let { groupInfo.pGroup!!.groupId },
            pGroupName = groupInfo.pGroup?.let { groupInfo.pGroup!!.groupName },
            groupDesc = groupInfo.groupDesc,
            useYn = groupInfo.useYn,
            level = groupInfo.level,
            seqNum = groupInfo.seqNum,
            editable= groupInfo.editable,
            roles = groupUseRoleList
        )
    }

    /**
     * 조직 등록
     */
    @Transactional
    fun createGroup(groupRoleDto: GroupRoleDto) : Boolean {
        val groupRoleList = mutableListOf<GroupRoleMapEntity>()

        if (groupRepository.existsByGroupName(groupRoleDto.groupName)) {
            return false
        }
        val group = groupRepository.save(
            GroupEntity(
                pGroup = groupRepository.findById(groupRoleDto.pGroupId).get(),
                groupName = groupRoleDto.groupName,
                groupDesc = groupRoleDto.groupDesc,
                useYn = groupRoleDto.useYn,
                level = groupRoleDto.level,
                seqNum = groupRoleDto.seqNum,
                createUserKey = currentSessionUser.getUserKey(),
                createDt = LocalDateTime.now()
            )
        )
        groupRoleDto.roles.forEach { role ->
            groupRoleList.add(
                GroupRoleMapEntity(group, role)
            )
        }
        if (groupRoleList.isNotEmpty()) {
            groupRoleMapRepository.saveAll(groupRoleList)
        }
        return true
    }

    /**
     * 조직 정보 수정
     */
    @Transactional
    fun updateGroup(groupRoleDto: GroupRoleDto) : Boolean {
        val group = groupRepository.findByGroupId(groupRoleDto.groupId)
        val groupRoleList = mutableListOf<GroupRoleMapEntity>()

        group.groupId = groupRoleDto.groupId
        group.pGroup = groupRepository.findById(groupRoleDto.pGroupId).get()
        group.groupName = groupRoleDto.groupName
        group.groupDesc = groupRoleDto.groupDesc
        group.useYn = groupRoleDto.useYn
        group.level = groupRoleDto.level
        group.seqNum = groupRoleDto.seqNum
        group.updateUserKey = currentSessionUser.getUserKey()
        group.updateDt = LocalDateTime.now()

        groupRoleMapRepository.deleteByGroupId(group)
        groupRoleMapRepository.flush()
        groupRoleDto.roles.forEach { role ->
            groupRoleList.add(
                GroupRoleMapEntity(group, role)
            )
        }
        if (groupRoleList.isNotEmpty()) {
            groupRoleMapRepository.saveAll(groupRoleList)
        }
        return true
    }

    /**
     * 조직 정보 삭제
     */
    @Transactional
    fun deleteGroup(groupId: String) : Boolean {
        // 조직에 구성원이 있는지 확인
        when (userRepository.existsByDepartment(groupId)) {
            true -> {
                throw AliceException(
                    AliceErrorConstants.ERR_00004,
                    aliceMessageSource.getMessage("group.msg.failedGroupDelete")
                )
            }
            false -> {
                // 조직에 설정된 역할 삭제
                groupRoleMapRepository.deleteByGroupId(GroupEntity(groupId))
                // 조직 삭제
                groupRepository.deleteByGroupId(groupId)
                return true
            }
        }
    }
}
