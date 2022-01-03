/*
 * Copyright 2021 Brainzcompany Co., Ltd.
 * https://www.brainz.co.kr
 */

package co.brainz.itsm.group.controller

import co.brainz.framework.response.ZAliceResponse
import co.brainz.framework.response.dto.ZResponse
import co.brainz.itsm.group.dto.GroupRoleDto
import co.brainz.itsm.group.service.GroupService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/rest/groups")
class GroupRestController(private val groupService: GroupService) {

    /**
     * 조직 전체 목록 조회
     */
    @GetMapping("/", "")
    fun getGroups(@RequestParam(value = "searchValue", defaultValue = "") searchValue: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(groupService.getGroupList(searchValue))
    }

    /**
     * 조직 상세 정보 조회
     */
    @GetMapping("/{groupId}")
    fun getDetailGroup(@PathVariable groupId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(groupService.getDetailGroup(groupId))
    }

    /**
     * 조직 등록
     */
    @PostMapping("/","")
    fun createGroup(@RequestBody groupRoleDto: GroupRoleDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(groupService.createGroup(groupRoleDto))
    }

    /**
     * 조직 정보 수정
     */
    @PutMapping("/{groupId}")
    fun updateGroup(@RequestBody groupRoleDto: GroupRoleDto): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(groupService.updateGroup(groupRoleDto))
    }

    /**
     * 조직 정보 삭제
     */
    @DeleteMapping("/{groupId}")
    fun deleteGroup(@PathVariable groupId: String): ResponseEntity<ZResponse> {
        return ZAliceResponse.response(groupService.deleteGroup(groupId))
    }
}
