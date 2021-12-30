package co.brainz.itsm.group.service

import co.brainz.framework.util.CurrentSessionUser
import co.brainz.itsm.group.dto.GroupDetailDto
import co.brainz.itsm.group.dto.GroupDto
import co.brainz.itsm.group.dto.GroupListDto
import co.brainz.itsm.group.entity.GroupEntity
import co.brainz.itsm.group.entity.GroupRoleMapEntity
import co.brainz.itsm.group.repository.GroupRepository
import co.brainz.itsm.group.repository.GroupRoleMapRepository
import co.brainz.itsm.role.repository.RoleRepository
import com.querydsl.core.QueryResults
import java.time.LocalDateTime
import javax.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class GroupService(
    private val groupRepository: GroupRepository,
    private val groupRoleMapRepository: GroupRoleMapRepository,
    private val roleRepository: RoleRepository,
    private val currentSessionUser: CurrentSessionUser
) {

    /**
     * 조직 전체 목록 조회
     */
    fun getGroupList(searchValue: String): GroupListDto {
        val treeGroupList = mutableListOf<GroupDto>()
        val queryResults: QueryResults<GroupEntity>

        when (searchValue.isEmpty()) {
            true -> { queryResults = groupRepository.findByGroupAll() }
            false -> { queryResults = groupRepository.findByGroupList(searchValue) }
        }
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
        val groupUseRoles = groupRoleMapRepository.findGroupRoleMapByGroupId(groupId)
        //val allRoles = roleRepository.findRolesAll()

        return GroupDetailDto(
            groupId = groupInfo.groupId,
            pGroupId = groupInfo.pGroupId,
            groupName = groupInfo.groupName,
            groupDesc = groupInfo.groupDesc,
            useYn = groupInfo.useYn,
            level = groupInfo.level,
            seqNum = groupInfo.seqNum,
            //allRoles = allRoles,
            groupUseRoles = groupUseRoles
        )
    }

    /**
     * 조직 등록
     */
    @Transactional
    fun createGroup(groupDetailDto: GroupDetailDto) : Boolean {
        var isSuccess = true
        if (groupRepository.existsByGroupName(groupDetailDto.groupName)) {
            isSuccess = false
        }
        val group = groupRepository.save(
            GroupEntity(
                pGroupId = groupDetailDto.pGroupId,
                groupName = groupDetailDto.groupName,
                groupDesc = groupDetailDto.groupDesc,
                useYn = groupDetailDto.useYn,
                level = groupDetailDto.level,
                seqNum = groupDetailDto.seqNum,
                createUserKey = currentSessionUser.getUserKey(),
                createDt = LocalDateTime.now()
            )
        )
        if (!groupDetailDto.groupUseRoles.isEmpty()) {
            groupDetailDto.groupUseRoles.forEach { role ->
                groupRoleMapRepository.save(GroupRoleMapEntity(group, role))
            }
        }
        return isSuccess
    }

    /**
     * 조직 정보 수정
     */
    @Transactional
    fun updateGroup(groupDetailDto: GroupDetailDto) : Boolean {

        val group = groupRepository.save(
            GroupEntity(
                groupId = groupDetailDto.groupId,
                pGroupId = groupDetailDto.pGroupId,
                groupName = groupDetailDto.groupName,
                groupDesc = groupDetailDto.groupDesc,
                useYn = groupDetailDto.useYn,
                level = groupDetailDto.level,
                seqNum = groupDetailDto.seqNum,
                updateUserKey = currentSessionUser.getUserKey(),
                updateDt = LocalDateTime.now()
            )
        )
        if (!groupDetailDto.groupUseRoles.isEmpty()) {
            groupRoleMapRepository.deleteByGroupId(group)
            groupRoleMapRepository.flush()

            groupDetailDto.groupUseRoles.forEach { role ->
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

        // TODO : 조직에 구성원이 있으면 알림창으로 전달.

        // 조직에 설정된 역할 삭제
        groupRoleMapRepository.deleteByGroupId(GroupEntity(groupId))
        // 조직 삭제
        groupRepository.deleteByGroupId(groupId)
        return true
    }

}
