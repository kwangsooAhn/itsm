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
import co.brainz.itsm.role.repository.RoleRepository
import co.brainz.itsm.user.repository.UserRepository
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val groupRoleMapRepository: GroupRoleMapRepository,
    private val roleRepository: RoleRepository,
    private val userRepository: UserRepository,
    private val currentSessionUser: CurrentSessionUser,
    private val aliceMessageSource: AliceMessageSource
) {

    /**
     * 조직 전체 목록 조회
     */
    fun getGroupList(searchValue: String): GroupListDto {
        val treeGroupList = mutableListOf<GroupDto>()
        val queryResults: QueryResults<GroupEntity>

        queryResults = groupRepository.findByGroupSearchList(searchValue)

        for (group in queryResults.results) {
            treeGroupList.add(
                GroupDto(
                    groupId = group.groupId,
                    pGroupId = group.pGroupId,
                    groupName = group.groupName,
                    groupDesc = group.groupDesc,
                    useYn = group.useYn,
                    level = group.level,
                    seqNum = group.seqNum,
                    createDt = group.createDt,
                    createUserKey = group.createUserKey,
                    updateDt = group.updateDt,
                    updateUserKey = group.updateUserKey
                )
            )
        }
        return GroupListDto (
            data = treeGroupList,
            totalCount = queryResults.total
        )
    }

    /**
     * 조직 정보 상세 조회
     */
    fun getDetailGroup(groupId: String): GroupDetailDto {
        val groupInfo = groupRepository.findByGroupId(groupId)
        val groupUseRoleList = groupRoleMapRepository.findGroupUseRoleByGroupId(groupId)
        val allRoleList = roleRepository.findByRoleAll()

        return GroupDetailDto(
            groupId = groupInfo.groupId,
            pGroupId = groupInfo.pGroupId,
            groupName = groupInfo.groupName,
            groupDesc = groupInfo.groupDesc,
            useYn = groupInfo.useYn,
            level = groupInfo.level,
            seqNum = groupInfo.seqNum,
            allRoles = allRoleList,
            groupUseRoles = groupUseRoleList
        )
    }

    /**
     * 조직 등록
     */
    @Transactional
    fun createGroup(groupRoleDto: GroupRoleDto) : Boolean {
        if (groupRepository.existsByGroupName(groupRoleDto.groupName)) {
            return false
        }
        val group = groupRepository.save(
            GroupEntity(
                pGroupId = groupRoleDto.pGroupId,
                groupName = groupRoleDto.groupName,
                groupDesc = groupRoleDto.groupDesc,
                useYn = groupRoleDto.useYn,
                level = groupRoleDto.level,
                seqNum = groupRoleDto.seqNum,
                createUserKey = currentSessionUser.getUserKey(),
                createDt = LocalDateTime.now()
            )
        )
        if (!groupRoleDto.roles.isEmpty()) {
            groupRoleDto.roles.forEach { role ->
                groupRoleMapRepository.save(GroupRoleMapEntity(group, role))
            }
        }
        return true
    }

    /**
     * 조직 정보 수정
     */
    @Transactional
    fun updateGroup(groupRoleDto: GroupRoleDto) : Boolean {

        val group = groupRepository.save(
            GroupEntity(
                groupId = groupRoleDto.groupId,
                pGroupId = groupRoleDto.pGroupId,
                groupName = groupRoleDto.groupName,
                groupDesc = groupRoleDto.groupDesc,
                useYn = groupRoleDto.useYn,
                level = groupRoleDto.level,
                seqNum = groupRoleDto.seqNum,
                updateUserKey = currentSessionUser.getUserKey(),
                updateDt = LocalDateTime.now()
            )
        )
        if (!groupRoleDto.roles.isEmpty()) {
            groupRoleMapRepository.deleteByGroupId(group)
            groupRoleMapRepository.flush()

            groupRoleDto.roles.forEach { role ->
                groupRoleMapRepository.save(GroupRoleMapEntity(group, role))
            }
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
